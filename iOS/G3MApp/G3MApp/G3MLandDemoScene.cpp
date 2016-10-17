//
//  G3MLandDemoScene.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 10/4/16.
//

#include "G3MLandDemoScene.hpp"

#include "G3MDemoModel.hpp"

#include <G3MiOSSDK/LayerSet.hpp>
#include <G3MiOSSDK/URLTemplateLayer.hpp>
#include <G3MiOSSDK/PlanetRenderer.hpp>
#include <G3MiOSSDK/BILDownloader.hpp>
#include <G3MiOSSDK/URL.hpp>
#include <G3MiOSSDK/DownloadPriority.hpp>
#include <G3MiOSSDK/ShortBufferTerrainElevationGrid.hpp>
#include <G3MiOSSDK/MeshRenderer.hpp>
#include <G3MiOSSDK/G3MWidget.hpp>
#include <G3MiOSSDK/MapzenTerrainElevationProvider.hpp>


void G3MLandDemoScene::rawSelectOption(const std::string& option,
                                       int optionIndex) {
  // no options
}


//class G3MLandDemoSceneBILHandler : public BILDownloader::Handler {
//private:
//  G3MDemoModel* _model;
//
//public:
//  G3MLandDemoSceneBILHandler(G3MDemoModel* model) :
//  _model(model)
//  {
//  }
//
//  //  virtual ~Handler() {
//  //  }
//
//  void onDownloadError(const G3MContext* context,
//                       const URL& url) {
//    // do nothing
//  }
//
//  void onParseError(const G3MContext* context) {
//    // do nothing
//  }
//
//  void onBIL(const G3MContext* context,
//             ShortBufferTerrainElevationGrid* result) {
//    Mesh* mesh = result->createDebugMesh(context->getPlanet(),
//                                         1,                    // verticalExaggeration
//                                         Geodetic3D::zero(),   // offset
//                                         1                     // pointSize
//                                         );
//    _model->getMeshRenderer()->addMesh(mesh);
//
//    const Geodetic3D cameraPosition(result->getSector()._center, 40000);
//    _model->getG3MWidget()->setAnimatedCameraPosition(cameraPosition);
//  }
//};


void G3MLandDemoScene::rawActivate(const G3MContext* context) {

  PlanetRenderer* planetRenderer = getModel()->getPlanetRenderer();
  //  planetRenderer->setShowStatistics(true);
  planetRenderer->setIncrementalTileQuality(true);

#warning Diego at work!
  planetRenderer->setVerticalExaggeration(100);
  planetRenderer->setTerrainElevationProvider(new MapzenTerrainElevationProvider("mapzen-ZB6FqMg",
                                                                                 DownloadPriority::HIGHER,
                                                                                 TimeInterval::fromDays(0),
                                                                                 false /* readExpired */));

  // https://mapzen.com/blog/elevation/
  URLTemplateLayer* layer = URLTemplateLayer::newMercator("https://tile.mapzen.com/mapzen/terrain/v1/normal/{z}/{x}/{y}.png?api_key=mapzen-ZB6FqMg",
                                                          Sector::FULL_SPHERE,
                                                          false,                     // isTransparent
                                                          2,                         // firstLevel
                                                          15,                        // maxLevel
                                                          TimeInterval::fromDays(30) // timeToCache
                                                          );

  getModel()->getLayerSet()->addLayer( layer );


  //  const double deltaHeight = -700.905;
  //  const short  noDataValue = -32768;
  //  BILDownloader::request(context,
  //                         URL("file:///0576.bil"),
  //                         DownloadPriority::MEDIUM,
  //                         TimeInterval::fromDays(0),
  //                         false,  // readExpired
  //                         Sector::fromDegrees(40.1665739916489, -5.85449532145337,
  //                                             40.3320215899527, -5.5116079822178570),
  //                         Vector2I(2516, 1335),
  //                         deltaHeight,
  //                         noDataValue,
  //                         new G3MLandDemoSceneBILHandler(getModel()),
  //                         true);
}

