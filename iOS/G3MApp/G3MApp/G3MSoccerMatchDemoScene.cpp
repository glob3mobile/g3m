//
//  G3MSoccerMatchDemoScene.cpp
//  G3MApp
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 11/25/18.
//  Copyright © 2018 Igo Software SL. All rights reserved.
//

#include "G3MSoccerMatchDemoScene.hpp"

#include <G3MiOSSDK/BingMapsLayer.hpp>
#include <G3MiOSSDK/LayerSet.hpp>
#include <G3MiOSSDK/G3MContext.hpp>
#include <G3MiOSSDK/IDownloader.hpp>
#include <G3MiOSSDK/URL.hpp>
#include <G3MiOSSDK/DownloadPriority.hpp>
#include <G3MiOSSDK/IBufferDownloadListener.hpp>
#include <G3MiOSSDK/IThreadUtils.hpp>
#include <G3MiOSSDK/GAsyncTask.hpp>
#include <G3MiOSSDK/DirectMesh.hpp>
#include <G3MiOSSDK/JSONObject.hpp>
#include <G3MiOSSDK/JSONArray.hpp>
#include <G3MiOSSDK/IJSONParser.hpp>
#include <G3MiOSSDK/JSONNumber.hpp>
#include <G3MiOSSDK/FloatBufferBuilderFromCartesian3D.hpp>
#include <G3MiOSSDK/FloatBufferBuilderFromColor.hpp>
#include <G3MiOSSDK/G3MWidget.hpp>
#include <G3MiOSSDK/ShapesRenderer.hpp>
#include <G3MiOSSDK/MeshShape.hpp>

#include "G3MDemoModel.hpp"


class G3MSoccerMatchDemoScene_ParserAsyncTask : public GAsyncTask {
private:
  G3MSoccerMatchDemoScene* _scene;
  const URL                _url;
  IByteBuffer*             _buffer;

  Mesh* _mesh;

  static double normalize(double value,
                          double max,
                          double min) {
    return ((value - min) / (max - min));
  }


public:
  G3MSoccerMatchDemoScene_ParserAsyncTask(G3MSoccerMatchDemoScene* scene,
                                          const URL&               url,
                                          IByteBuffer*             buffer) :
  _scene(scene),
  _url(url),
  _buffer(buffer),
  _mesh(NULL)
  {
  }

  ~G3MSoccerMatchDemoScene_ParserAsyncTask() {
    delete _buffer;
    delete _mesh;
  }

  void parse(const JSONObject* jsonObject) {

    const JSONObject* ballJSONObject = jsonObject->getAsObject("ball");

    const JSONObject* ballMetadataJSONObject = ballJSONObject->getAsObject("metadata");

    const double minZ = ballMetadataJSONObject->getAsArray("min")->getAsNumber(3)->value();
    const double maxZ = ballMetadataJSONObject->getAsArray("max")->getAsNumber(3)->value();
    const double deltaZ = maxZ - minZ;

    const JSONArray* ballPositionsJSONArray = ballJSONObject->getAsArray("positions");

    FloatBufferBuilderFromCartesian3D* vertices = FloatBufferBuilderFromCartesian3D::builderWithoutCenter();

    FloatBufferBuilderFromColor colors;
    const size_t ballPositionsCount = ballPositionsJSONArray->size();

    for (size_t i = 0; i < ballPositionsCount; i++) {
      const JSONArray* ballPositionJSONArray = ballPositionsJSONArray->getAsArray(i);
      const double x = ballPositionJSONArray->getAsNumber(0)->value();
      const double y = ballPositionJSONArray->getAsNumber(1)->value();
      const double z = ballPositionJSONArray->getAsNumber(2)->value();
      vertices->add(x, y, z);

      const double a = (z - minZ) / deltaZ;
      colors.add(a, a, a, 1);
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

  }

  void runInBackground(const G3MContext* context) {
    const JSONBaseObject* jsonBaseObject = context->getJSONParser()->instance()->parse(_buffer,
                                                                                       false // nullAsObject
                                                                                       );

    if (jsonBaseObject == NULL) {
      ILogger::instance()->logError("Can't parse \"%s\" (1)", _url._path.c_str());
    }
    else {
      const JSONObject* object = jsonBaseObject->asObject();
      parse(object);
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
      _scene->setMesh(_mesh);
      _mesh = NULL;
    }
  }
};


class G3MSoccerMatchDemoScene_BufferDownloadListener : public IBufferDownloadListener {
private:
  const IThreadUtils* _threadUtils;
  G3MSoccerMatchDemoScene* _scene;

public:
  G3MSoccerMatchDemoScene_BufferDownloadListener(G3MSoccerMatchDemoScene* scene,
                                                 const IThreadUtils*     threadUtils) :
  _scene(scene),
  _threadUtils(threadUtils)
  {
  }

  void onDownload(const URL& url,
                  IByteBuffer* buffer,
                  bool expired) {
    _threadUtils->invokeAsyncTask(new G3MSoccerMatchDemoScene_ParserAsyncTask(_scene, url, buffer),
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


void G3MSoccerMatchDemoScene::rawSelectOption(const std::string& option,
                                              int optionIndex) {
}

void G3MSoccerMatchDemoScene::rawActivate(const G3MContext* context) {
  G3MDemoModel* model     = getModel();
  G3MWidget*    g3mWidget = model->getG3MWidget();

  g3mWidget->setForceBusyRenderer(true);

  BingMapsLayer* layer = new BingMapsLayer(BingMapType::Aerial(),
                                           "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc",
                                           TimeInterval::fromDays(30));
  model->getLayerSet()->addLayer(layer);


  IDownloader* downloader = context->getDownloader();
  downloader->requestBuffer(URL("file:///869491-rawdata.dat.gz_points.json"),
                            DownloadPriority::HIGHEST,
                            TimeInterval::forever(),
                            true,
                            new G3MSoccerMatchDemoScene_BufferDownloadListener(this,
                                                                               context->getThreadUtils()),
                            true);

}

void G3MSoccerMatchDemoScene::setMesh(Mesh* mesh) {
  G3MDemoModel* model     = getModel();
  G3MWidget*    g3mWidget = model->getG3MWidget();

  model->getShapesRenderer()->addShape(new MeshShape(new Geodetic3D(Geodetic3D::fromDegrees(43.181706, -2.475803, 0)),
                                                     AltitudeMode::ABSOLUTE,
                                                     mesh));

  g3mWidget->setForceBusyRenderer(false);

  g3mWidget->setAnimatedCameraPosition(TimeInterval::fromSeconds(5),
                                       Geodetic3D::fromDegrees(43.181706, -2.475803, 5000));
}
