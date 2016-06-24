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

#include <G3MiOSSDK/SingleBilElevationDataProvider.hpp>
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
    _g3mWidget->setAnimatedCameraPosition( Geodetic3D::fromDegrees(39.068479748852752209, -77.602316923351310152, 70000) );
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

//  ElevationDataProvider* elevationDataProvider = new SingleBilElevationDataProvider(URL("file:///full-earth-2048x1024.bil"),
//                                                                                    Sector::fullSphere(),
//                                                                                    Vector2I(2048, 1024));
//  planetRenderer->setElevationDataProvider(elevationDataProvider, true);

  BingMapsLayer* layer = new BingMapsLayer(BingMapType::Aerial(),
                                           "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc",
                                           TimeInterval::fromDays(30));
  model->getLayerSet()->addLayer(layer);

//#warning TODO cache
  model->getPointCloudsRenderer()->addPointCloud(URL("http://glob3mobile.dyndns.org:8080"),
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
