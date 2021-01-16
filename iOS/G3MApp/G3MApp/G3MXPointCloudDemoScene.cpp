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
    // _g3mWidget->setAnimatedCameraPosition( Geodetic3D::fromDegrees(39.068479748852752209, -77.602316923351310152, 70000) );

    //    _g3mWidget->setAnimatedCameraPosition(Geodetic3D::fromDegrees(39.051416089546606258,
    //                                                                  -77.517952264331185575,
    //                                                                  1218.9966501063768192),
    //                                          Angle::fromDegrees(-5),
    //                                          Angle::fromDegrees(-42));
  }

};


void G3MXPointCloudDemoScene::rawActivate(const G3MContext *context) {
  G3MDemoModel* model     = getModel();
  G3MWidget*    g3mWidget = model->getG3MWidget();

  const float  pointSize            = 1;
  const bool   dynamicPointSize     = true;
  const float  verticalExaggeration = 1;
  const double deltaHeight          = 0;

  PlanetRenderer* planetRenderer = model->getPlanetRenderer();
  planetRenderer->setVerticalExaggeration(verticalExaggeration);

  //  ElevationDataProvider* elevationDataProvider = new SingleBILElevationDataProvider(URL("file:///full-earth-2048x1024.bil"),
  //                                                                                    Sector::fullSphere(),
  //                                                                                    Vector2I(2048, 1024));
  //  planetRenderer->setElevationDataProvider(elevationDataProvider, true);

  BingMapsLayer* layer = new BingMapsLayer(BingMapType::Aerial(),
                                           "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc",
                                           TimeInterval::fromDays(30));
  model->getLayerSet()->addLayer(layer);

  //#warning TODO cache
  model->getXPCRenderer()->addPointCloud(URL("http://192.168.1.69:8082"),
                                         "Leica_M40",
                                         DownloadPriority::LOWER,
                                         TimeInterval::zero(),
                                         false,
                                         PointCloudsRenderer::MIN_AVERAGE3_HEIGHT,
                                         pointSize,
                                         dynamicPointSize,
                                         verticalExaggeration,
                                         deltaHeight,
                                         new G3MXPointCloudDemoScene_PointCloudMetadataListener(g3mWidget),
                                         true);

}
