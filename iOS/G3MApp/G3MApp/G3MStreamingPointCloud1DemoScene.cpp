//
//  G3MStreamingPointCloud1DemoScene.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 8/19/14.
//

#include "G3MStreamingPointCloud1DemoScene.hpp"
#include <G3MiOSSDK/BingMapsLayer.hpp>
#include <G3MiOSSDK/LayerSet.hpp>
#include <G3MiOSSDK/G3MWidget.hpp>
#include <G3MiOSSDK/PointCloudsRenderer.hpp>
#include <G3MiOSSDK/URL.hpp>
#include <G3MiOSSDK/DownloadPriority.hpp>
#include <G3MiOSSDK/Geodetic3D.hpp>
#include <G3MiOSSDK/SingleBILElevationDataProvider.hpp>
#include <G3MiOSSDK/PlanetRenderer.hpp>

#include "G3MDemoModel.hpp"


class G3MStreamingPointCloud1DemoScene_PointCloudMetadataListener : public PointCloudsRenderer::PointCloudMetadataListener {
private:
  G3MWidget* _g3mWidget;
public:
  G3MStreamingPointCloud1DemoScene_PointCloudMetadataListener(G3MWidget* g3mWidget) :
  _g3mWidget(g3mWidget)
  {
  }

  void onMetadata(long long pointsCount,
                  const Sector& sector,
                  double minHeight,
                  double maxHeight,
                  double averageHeight) {
   // _g3mWidget->setAnimatedCameraPosition( Geodetic3D::fromDegrees(39.068479748852752209, -77.602316923351310152, 70000) );


    _g3mWidget->setAnimatedCameraPosition(Geodetic3D::fromDegrees(39.051416089546606258,
                                                                  -77.517952264331185575,
                                                                  1218.9966501063768192),
                                          Angle::fromDegrees(-5),
                                          Angle::fromDegrees(-42));

    //2016-07-04 12:14:28.089 G3MApp[8123:2714951] Camera position=(lat=39.051416089546606258d, lon=-77.517952264331185575d, height=1218.9966501063768192) heading=-5.070267 pitch=-42.344693

  }

};


void G3MStreamingPointCloud1DemoScene::rawActivate(const G3MContext *context) {
  G3MDemoModel* model     = getModel();
  G3MWidget*    g3mWidget = model->getG3MWidget();

  const float pointSize = 2;
  const float verticalExaggeration = 1;
  const double deltaHeight = 0;

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
  model->getPointCloudsRenderer()->addPointCloud(//URL("http://glob3mobile.dyndns.org:8080"),
                                                 //URL("http://aerog3m.cloudapp.net:8082"),
                                                 URL("http://192.168.1.35:8082"),
                                                 "Loudoun-VA_simplified2_LOD",
                                                 //"Wallonia-Belgium_simplified2_LOD",
                                                 DownloadPriority::LOWER,
                                                 TimeInterval::zero(),
                                                 false,
                                                 PointCloudsRenderer::MIN_AVERAGE3_HEIGHT,
                                                 //PointCloudsRenderer::MIN_MAX_HEIGHT,
                                                 pointSize,
                                                 verticalExaggeration,
                                                 deltaHeight,
                                                 new G3MStreamingPointCloud1DemoScene_PointCloudMetadataListener(g3mWidget),
                                                 true);
}
