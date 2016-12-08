//
//  G3M3DLandDemoScene.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 10/4/16.
//

#include "G3M3DLandDemoScene.hpp"

#include "G3MDemoModel.hpp"

#include <G3MiOSSDK/LayerSet.hpp>
#include <G3MiOSSDK/URLTemplateLayer.hpp>
#include <G3MiOSSDK/PlanetRenderer.hpp>
#include <G3MiOSSDK/BILDownloader.hpp>
#include <G3MiOSSDK/URL.hpp>
#include <G3MiOSSDK/DownloadPriority.hpp>
#include <G3MiOSSDK/ShortBufferDEMGrid.hpp>
#include <G3MiOSSDK/MeshRenderer.hpp>
#include <G3MiOSSDK/G3MWidget.hpp>
#include <G3MiOSSDK/MapzenDEMProvider.hpp>
#include <G3MiOSSDK/Geodetic3D.hpp>


void G3M3DLandDemoScene::rawSelectOption(const std::string& option,
                                         int optionIndex) {
  // no options
}


//class G3M3DLandDemoSceneBILHandler : public BILDownloader::Handler {
//private:
//  G3MDemoModel* _model;
//
//public:
//  G3M3DLandDemoSceneBILHandler(G3MDemoModel* model) :
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
//             ShortBufferDEMGrid* result) {
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


void G3M3DLandDemoScene::rawActivate(const G3MContext* context) {

  PlanetRenderer* planetRenderer = getModel()->getPlanetRenderer();
  planetRenderer->setShowStatistics(true);
  planetRenderer->setIncrementalTileQuality(true);

  planetRenderer->setVerticalExaggeration(1);
  planetRenderer->setDEMProvider( new MapzenDEMProvider("mapzen-ZB6FqMg",
                                                        DownloadPriority::HIGHER,
                                                        TimeInterval::fromDays(0),
                                                        false, /* readExpired */
                                                        0      /* deltaHeight */) );

  // https://mapzen.com/blog/elevation/
  URLTemplateLayer* layer = URLTemplateLayer::newMercator("https://tile.mapzen.com/mapzen/terrain/v1/normal/{z}/{x}/{y}.png?api_key=mapzen-ZB6FqMg",
                                                          Sector::FULL_SPHERE,
                                                          false,                     // isTransparent
                                                          2,                         // firstLevel
                                                          15,                        // maxLevel
                                                          TimeInterval::fromDays(30) // timeToCache
                                                          );

  getModel()->getLayerSet()->addLayer( layer );

#warning Diego at work!
  getModel()->getG3MWidget()->setCameraPosition( Geodetic3D::fromDegrees(-31.952754850831528444,
                                                                         -70.640222465417764397,
                                                                         3821842.2834936883301) );

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
  //                         new G3M3DLandDemoSceneBILHandler(getModel()),
  //                         true);
}
