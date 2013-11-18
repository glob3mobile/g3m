//
//  G3MPointCloudDemoScene.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/18/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#include "G3MPointCloudDemoScene.hpp"

#include <G3MiOSSDK/G3MWidget.hpp>
#include <G3MiOSSDK/MapBoxLayer.hpp>
#include <G3MiOSSDK/LayerSet.hpp>
#include <G3MiOSSDK/IDownloader.hpp>
#include <G3MiOSSDK/DownloadPriority.hpp>
#include <G3MiOSSDK/IBufferDownloadListener.hpp>
#include <G3MiOSSDK/BSONParser.hpp>
#include <G3MiOSSDK/JSONBaseObject.hpp>
#include <G3MiOSSDK/JSONObject.hpp>
#include <G3MiOSSDK/JSONArray.hpp>
#include <G3MiOSSDK/JSONNumber.hpp>
#include <G3MiOSSDK/FloatBufferBuilderFromColor.hpp>
#include <G3MiOSSDK/FloatBufferBuilderFromGeodetic.hpp>
#include <G3MiOSSDK/DirectMesh.hpp>
#include <G3MiOSSDK/GLConstants.hpp>
#include <G3MiOSSDK/MeshRenderer.hpp>

#include "G3MDemoModel.hpp"

class G3MPointCloudDemoScene_BufferDownloadListener : public IBufferDownloadListener {
private:
  G3MPointCloudDemoScene* _scene;
  const Planet* _planet;

  static double normalize(double value,
                          double max,
                          double min,
                          double new_max,
                          double new_min) {
    return (((value - min) / (max - min)) * (new_max - new_min)) + new_min;
  }

  static Color interpolateColor(const Color& from,
                                const Color& middle,
                                const Color& to,
                                double d) {
    if (d <= 0) {
      return from;
    }
    if (d >= 1) {
      return to;
    }
    if (d <= 0.5) {
      return from.mixedWith(middle, d * 2);
    }
    return middle.mixedWith(to, (d - 0.5f) * 2);
  }

public:
  G3MPointCloudDemoScene_BufferDownloadListener(G3MPointCloudDemoScene* scene,
                                                const Planet* planet) :
  _scene(scene),
  _planet(planet)
  {
  }

  void onDownload(const URL& url,
                  IByteBuffer* buffer,
                  bool expired) {

    const JSONBaseObject* jsonBaseObject = BSONParser::parse(buffer);

    if (jsonBaseObject == NULL) {
      ILogger::instance()->logError("Can't parse \"%s\" (1)", url.getPath().c_str());
    }
    else {
      const JSONObject* object = jsonBaseObject->asObject();
      const JSONArray* pointsJson = object->getAsArray("points");

      FloatBufferBuilderFromGeodetic vertices = FloatBufferBuilderFromGeodetic::builderWithFirstVertexAsCenter(_planet);

      FloatBufferBuilderFromColor colors;

      const int size = pointsJson->size();

      double minHeight = IMathUtils::instance()->maxDouble();
      double maxHeight = IMathUtils::instance()->minDouble();
      double totalHeight = 0;
      for (int i = 0; i < size; i = i + 3) {
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

      const Color fromColor = Color::red();
      const Color middleColor = Color::green();
      const Color toColor = Color::blue();

      for (int i = 0; i < size; i = i + 3) {
        const double latDegrees = pointsJson->getAsNumber(i + 1, 0);
        const double lonDegrees = pointsJson->getAsNumber(i, 0);
        const double height = pointsJson->get(i + 2)->asNumber()->value();

        vertices.add(Angle::fromDegrees(latDegrees),
                     Angle::fromDegrees(lonDegrees),
                     height - 150);

        const Color interpolatedColor = interpolateColor(fromColor,
                                                         middleColor,
                                                         toColor,
                                                         normalize(height, minHeight, (averageHeight * 1.5), 1, 0)
                                                         );
        colors.add(interpolatedColor);
      }

      const float lineWidth = 1;
      const float pointSize = 4;
      _scene->setPointsCloudMesh(new DirectMesh(GLPrimitive::points(),
                                                true,
                                                vertices.getCenter(),
                                                vertices.create(),
                                                lineWidth,
                                                pointSize,
                                                NULL, // flatColor
                                                colors.create(),
                                                1, // colorsIntensity
                                                false));
    }
    
    delete buffer;
  }

  void onError(const URL& url) {
    ILogger::instance()->logError("Error downloading \"%s\"", url.getPath().c_str());
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

  g3mWidget->setAnimatedCameraPosition(TimeInterval::fromSeconds(5),
                                       Geodetic3D::fromDegrees(39.12787093899339, -77.59965772558118, 5000));

}

void G3MPointCloudDemoScene::rawActivate(const G3MContext* context) {
  G3MDemoModel* model     = getModel();
  G3MWidget*    g3mWidget = model->getG3MWidget();

  g3mWidget->setBackgroundColor(Color::fromRGBA255(175, 221, 233, 255));

  MapBoxLayer* layer = new MapBoxLayer("examples.map-m0t0lrpu",
                                       TimeInterval::fromDays(30),
                                       true,
                                       2);
  model->getLayerSet()->addLayer(layer);


  IDownloader* downloader = context->getDownloader();
  downloader->requestBuffer(URL("file:///18STJ7435_2000_4326.bson"),
                            DownloadPriority::HIGHEST,
                            TimeInterval::forever(),
                            true,
                            new G3MPointCloudDemoScene_BufferDownloadListener(this, context->getPlanet()),
                            true);
}
