//
//  G3MXPointCloudDemoScene.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 1/15/21.
//

#include "G3MXPointCloudDemoScene.hpp"

#include <G3M/BingMapsLayer.hpp>
#include <G3M/LayerSet.hpp>
#include <G3M/G3MWidget.hpp>
#include <G3M/URL.hpp>
#include <G3M/DownloadPriority.hpp>
#include <G3M/Geodetic3D.hpp>
#include <G3M/SingleBILElevationDataProvider.hpp>
#include <G3M/PlanetRenderer.hpp>

#include <G3M/XPCRenderer.hpp>
#include <G3M/XPCMetadataListener.hpp>
#include <G3M/XPCMetadata.hpp>
#include <G3M/XPCTree.hpp>
#include <G3M/XPCRGBPointColorizer.hpp>
#include <G3M/XPCIntensityPointColorizer.hpp>
#include <G3M/XPCClassificationPointColorizer.hpp>

#include "G3MDemoModel.hpp"


class G3MXPointCloudDemoScene_PointCloudMetadataListener : public XPCMetadataListener {
private:
  G3MWidget* _g3mWidget;

public:
  G3MXPointCloudDemoScene_PointCloudMetadataListener(G3MWidget* g3mWidget) :
  _g3mWidget(g3mWidget)
  {
  }

  void onMetadata(const XPCMetadata* metadata) const {
    if (metadata->getTreesCount() > 0) {
      const XPCTree* tree = metadata->getTree(0);

      const Geodetic2D center = tree->getSector()->_center;
      
      _g3mWidget->setAnimatedCameraPosition( Geodetic3D(center._latitude, center._longitude, 500) );
    }
  }

};


void G3MXPointCloudDemoScene::rawActivate(const G3MContext *context) {
  G3MDemoModel* model     = getModel();
  G3MWidget*    g3mWidget = model->getG3MWidget();

  const float  pointSize            = 1;
  const bool   dynamicPointSize     = true;
  const float  verticalExaggeration = 1;

//  const std::string cloudName   = "Leica_FFCC_COMPLETE_LOD";
//  const float       deltaHeight = -210;

//  const std::string cloudName   = "Leica_FFCC_SMALL_LOD";
//  const float       deltaHeight = -210;

  const std::string cloudName   = "Leica_M40_LOD";
  const float       deltaHeight = -580;

//  const std::string cloudName   = "NEON_LOD";
//  const float       deltaHeight = -385;

  const double minProjectedArea = 50000; //50000;

  PlanetRenderer* planetRenderer = model->getPlanetRenderer();
  planetRenderer->setVerticalExaggeration(verticalExaggeration);

  //  ElevationDataProvider* elevationDataProvider = new SingleBILElevationDataProvider(URL("file:///full-earth-2048x1024.bil"),
  //                                                                                    Sector::fullSphere(),
  //                                                                                    Vector2I(2048, 1024));
  //  planetRenderer->setElevationDataProvider(elevationDataProvider, true);

  BingMapsLayer* layer = new BingMapsLayer(BingMapType::AerialWithLabels(),
                                           "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc",
                                           TimeInterval::fromDays(30),
                                           true, // readExpired
                                           2,    // initialLevel
                                           19    // maxLevel     = 25
                                           );
  model->getLayerSet()->addLayer(layer);


#warning TODO cache
  model->getXPCRenderer()->addPointCloud(URL("http://192.168.1.69:8080/INROAD_visor/xpc/"),
                                         cloudName,
                                         DownloadPriority::LOWER,
                                         TimeInterval::zero(),
                                         false,
                                         new XPCRGBPointColorizer(),         // pointColorizer
                                         // new XPCIntensityPointColorizer(),   // pointColorizer
                                         // new XPCClassificationPointColorizer(), // pointColorizer
                                         true,                                  // deletePointColorizer,
                                         minProjectedArea,
                                         pointSize,
                                         dynamicPointSize,
                                         true, // depthTest
                                         verticalExaggeration,
                                         deltaHeight,
                                         new G3MXPointCloudDemoScene_PointCloudMetadataListener(g3mWidget),
                                         true,
                                         true);

}
