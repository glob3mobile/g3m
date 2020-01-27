//
//  G3MPointCloudDemoScene.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/18/13.
//

#include "G3MPointCloudDemoScene.hpp"

#include <G3MSharedSDK/G3MWidget.hpp>
#include <G3MSharedSDK/BingMapsLayer.hpp>
#include <G3MSharedSDK/LayerSet.hpp>
#include <G3MSharedSDK/IDownloader.hpp>
#include <G3MSharedSDK/DownloadPriority.hpp>
#include <G3MSharedSDK/IBufferDownloadListener.hpp>
#include <G3MSharedSDK/BSONParser.hpp>
#include <G3MSharedSDK/JSONBaseObject.hpp>
#include <G3MSharedSDK/JSONObject.hpp>
#include <G3MSharedSDK/JSONArray.hpp>
#include <G3MSharedSDK/JSONNumber.hpp>
#include <G3MSharedSDK/FloatBufferBuilderFromColor.hpp>
#include <G3MSharedSDK/FloatBufferBuilderFromGeodetic.hpp>
#include <G3MSharedSDK/DirectMesh.hpp>
#include <G3MSharedSDK/GLConstants.hpp>
#include <G3MSharedSDK/MeshRenderer.hpp>
#include <G3MSharedSDK/IThreadUtils.hpp>
#include <G3MSharedSDK/G3MContext.hpp>
#include <G3MSharedSDK/GAsyncTask.hpp>
#include <G3MSharedSDK/Geodetic3D.hpp>
#include <G3MSharedSDK/Color.hpp>
#include <G3MSharedSDK/IMathUtils.hpp>
#include <G3MSharedSDK/ILogger.hpp>

#include "G3MDemoModel.hpp"



class G3MPointCloudDemoScene_ParserAsyncTask : public GAsyncTask {
private:
  G3MPointCloudDemoScene* _scene;
  const Planet*           _planet;
  const URL               _url;
  IByteBuffer*            _buffer;

  Mesh* _mesh;

  static double normalize(double value,
                          double max,
                          double min) {
    return ((value - min) / (max - min));
  }


public:
  G3MPointCloudDemoScene_ParserAsyncTask(G3MPointCloudDemoScene* scene,
                                         const Planet*           planet,
                                         const URL&              url,
                                         IByteBuffer*            buffer) :
  _scene(scene),
  _planet(planet),
  _url(url),
  _buffer(buffer),
  _mesh(NULL)
  {
  }

  ~G3MPointCloudDemoScene_ParserAsyncTask() {
    delete _buffer;
    delete _mesh;
  }

  void runInBackground(const G3MContext* context) {
    const JSONBaseObject* jsonBaseObject = BSONParser::parse(_buffer);

    if (jsonBaseObject == NULL) {
      ILogger::instance()->logError("Can't parse \"%s\" (1)", _url._path.c_str());
    }
    else {
      const JSONObject* object = jsonBaseObject->asObject();
      const JSONArray* pointsJson = object->getAsArray("points");

      FloatBufferBuilderFromGeodetic* vertices = FloatBufferBuilderFromGeodetic::builderWithFirstVertexAsCenter(_planet);

      FloatBufferBuilderFromColor colors;

      const size_t size = pointsJson->size();

      double minHeight = IMathUtils::instance()->maxDouble();
      double maxHeight = IMathUtils::instance()->minDouble();
      double totalHeight = 0;
      for (size_t i = 0; i < size; i = i + 3) {
        const double height = pointsJson->get(i + 2)->asNumber()->value();
        totalHeight += height;
        if (height < minHeight) {
          minHeight = height;
        }
        if (height > maxHeight) {
          maxHeight = height;
        }
      }
      const double averageHeight = totalHeight / (size / 3.0);

      const Color fromColor   = Color::RED;
      const Color middleColor = Color::GREEN;
      const Color toColor     = Color::BLUE;

      for (size_t i = 0; i < size; i = i + 3) {
        const double latDegrees = pointsJson->getAsNumber(i + 1, 0);
        const double lonDegrees = pointsJson->getAsNumber(i, 0);
        const double height = pointsJson->get(i + 2)->asNumber()->value();

        vertices->add(Angle::fromDegrees(latDegrees),
                      Angle::fromDegrees(lonDegrees),
                      height - 150);

        const Color interpolatedColor = Color::interpolateColor(fromColor,
                                                                middleColor,
                                                                toColor,
                                                                normalize(height, minHeight, (averageHeight * 1.5))
                                                                );
        colors.add(interpolatedColor);
      }

      const float lineWidth = 1;
      const float pointSize = 4;

      _mesh = new DirectMesh(GLPrimitive::points(),
                             true,
                             vertices->getCenter(),
                             vertices->create(),
                             lineWidth,
                             pointSize,
                             NULL, // flatColor
                             colors.create(),
                             true);

      delete vertices;

      delete jsonBaseObject;
    }

    delete _buffer;
    _buffer = NULL;
  }

  void onPostExecute(const G3MContext* context) {
    if (_mesh == NULL) {
      ILogger::instance()->logError("Can't parse \"%s\" (2)", _url._path.c_str());
    }
    else {
      _scene->setPointsCloudMesh(_mesh);
      _mesh = NULL;
    }
  }
};


class G3MPointCloudDemoScene_BufferDownloadListener : public IBufferDownloadListener {
private:
  const IThreadUtils* _threadUtils;
  G3MPointCloudDemoScene* _scene;
  const Planet* _planet;

public:
  G3MPointCloudDemoScene_BufferDownloadListener(G3MPointCloudDemoScene* scene,
                                                const Planet*           planet,
                                                const IThreadUtils*     threadUtils) :
  _scene(scene),
  _planet(planet),
  _threadUtils(threadUtils)
  {
  }

  void onDownload(const URL& url,
                  IByteBuffer* buffer,
                  bool expired) {
    _threadUtils->invokeAsyncTask(new G3MPointCloudDemoScene_ParserAsyncTask(_scene, _planet, url, buffer),
                                  true);
  }

  void onError(const URL& url) {
    ILogger::instance()->logError("Error downloading \"%s\"", url._path.c_str());
  }

  void onCancel(const URL& url) {
    // do nothing
  }

  void onCanceledDownload(const URL& url,
                          IByteBuffer* buffer,
                          bool expired) {
    // do nothing
  }
};

void G3MPointCloudDemoScene::rawSelectOption(const std::string& option,
                                             int optionIndex) {
}

void G3MPointCloudDemoScene::setPointsCloudMesh(Mesh* mesh) {
  G3MDemoModel* model     = getModel();
  G3MWidget*    g3mWidget = model->getG3MWidget();

  model->getMeshRenderer()->addMesh(mesh);

  g3mWidget->setForceBusyRenderer(false);

  g3mWidget->setAnimatedCameraPosition(TimeInterval::fromSeconds(5),
                                       Geodetic3D::fromDegrees(39.12787093899339, -77.59965772558118, 5000));
}

void G3MPointCloudDemoScene::rawActivate(const G3MContext* context) {
  G3MDemoModel* model     = getModel();
  G3MWidget*    g3mWidget = model->getG3MWidget();

  g3mWidget->setForceBusyRenderer(true);

  g3mWidget->setBackgroundColor(Color::fromRGBA255(175, 221, 233, 255));

  BingMapsLayer* layer = new BingMapsLayer(BingMapType::Aerial(),
                                           "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc",
                                           TimeInterval::fromDays(30));
  model->getLayerSet()->addLayer(layer);


  IDownloader* downloader = context->getDownloader();
  downloader->requestBuffer(URL("file:///18STJ7435_2000_4326.bson"),
                            DownloadPriority::HIGHEST,
                            TimeInterval::forever(),
                            true,
                            new G3MPointCloudDemoScene_BufferDownloadListener(this,
                                                                              context->getPlanet(),
                                                                              context->getThreadUtils()),
                            true);
}
