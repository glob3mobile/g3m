//
//  G3MStreamingPointCloud2DemoScene.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 8/19/14.
//

#include "G3MStreamingPointCloud2DemoScene.hpp"

#include <G3MiOSSDK/BingMapsLayer.hpp>
#include <G3MiOSSDK/LayerSet.hpp>
#include <G3MiOSSDK/G3MWidget.hpp>
#include <G3MiOSSDK/PointCloudsRenderer.hpp>
#include <G3MiOSSDK/URL.hpp>
#include <G3MiOSSDK/DownloadPriority.hpp>
#include <G3MiOSSDK/SingleBILElevationDataProvider.hpp>
#include <G3MiOSSDK/PlanetRenderer.hpp>
#include <G3MiOSSDK/Geodetic3D.hpp>

#include "G3MDemoModel.hpp"


class G3MStreamingPointCloud2DemoScene_PointCloudMetadataListener : public PointCloudsRenderer::PointCloudMetadataListener {
private:
  G3MWidget* _g3mWidget;
public:
  G3MStreamingPointCloud2DemoScene_PointCloudMetadataListener(G3MWidget* g3mWidget) :
  _g3mWidget(g3mWidget)
  {
  }

  void onMetadata(long long pointsCount,
                  const Sector& sector,
                  double minHeight,
                  double maxHeight,
                  double averageHeight) {
    // _g3mWidget->setAnimatedCameraPosition( Geodetic3D::fromDegrees(39.084024168630392637, -77.643438514919708382, 11000) );

    _g3mWidget->setAnimatedCameraPosition(TimeInterval::fromSeconds(5),
//                                          Geodetic3D::fromDegrees(44.950996472837502438,
//                                                                  -93.098090960367656521,
//                                                                  301.5960958814906121),
                                          Geodetic3D::fromDegrees(53.7194561048,
                                                                  3.955078125,
                                                                  2000),
                                          Angle::zero(),
                                          Angle::minusHalfPi()
                                          //Angle::fromDegrees(-26)
                                          );
  }

};


void G3MStreamingPointCloud2DemoScene::rawActivate(const G3MContext *context) {
  G3MDemoModel* model     = getModel();
  G3MWidget*    g3mWidget = model->getG3MWidget();

  const float pointSize = 1;
  const bool dynamicPointSize = true;

  const float verticalExaggeration = 1;
//  const double deltaHeight = -202;
//const double deltaHeight = -3.7930956;
  const double deltaHeight = 13;

  PlanetRenderer* planetRenderer = model->getPlanetRenderer();
  planetRenderer->setVerticalExaggeration(verticalExaggeration);

//  ElevationDataProvider* elevationDataProvider = new SingleBILElevationDataProvider(URL("file:///full-earth-2048x1024.bil"),
//                                                                                    Sector::fullSphere(),
//                                                                                    Vector2I(2048, 1024));
//  planetRenderer->setElevationDataProvider(elevationDataProvider, true);

//  ElevationDataProvider* elevationDataProvider = new SingleBILElevationDataProvider(URL("file:///loudoun_4326_300.bil", false),
//                                                                                    Sector::fromDegrees(38.8423858972, -77.3224512673,
//                                                                                                        39.3301640652, -77.9660628933),
//                                                                                    Vector2I(773, 586),
//                                                                                    0);
//  
//  planetRenderer->setElevationDataProvider(elevationDataProvider, true);

  BingMapsLayer* layer = new BingMapsLayer(BingMapType::Aerial(),
                                           "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc",
                                           TimeInterval::fromDays(30));
  model->getLayerSet()->addLayer(layer);

//#warning TODO cache
  model->getPointCloudsRenderer()->addPointCloud(//URL("http://glob3mobile.dyndns.org:8080"),
                                                 //URL("http://aerog3m.cloudapp.net:8082"),
                                                 URL("http://192.168.1.12:8082"),
                                                 //"minnesota_LOD",
                                                 "TomTom_LOD",
                                                 DownloadPriority::LOWER,
                                                 TimeInterval::zero(),
                                                 true,
                                                 PointCloudsRenderer::MIN_AVERAGE3_HEIGHT,
                                                 // PointCloudsRenderer::MIN_MAX_HEIGHT,
                                                 pointSize,
                                                 dynamicPointSize,
                                                 verticalExaggeration,
                                                 deltaHeight,
                                                 new G3MStreamingPointCloud2DemoScene_PointCloudMetadataListener(g3mWidget),
                                                 true);
}
