//
//  G3MStreamingPointCloud2DemoScene.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 8/19/14.
//  Copyright (c) 2014 Igo Software SL. All rights reserved.
//

#include "G3MStreamingPointCloud2DemoScene.hpp"
#include <G3MiOSSDK/MapBoxLayer.hpp>
#include <G3MiOSSDK/LayerSet.hpp>
#include <G3MiOSSDK/G3MWidget.hpp>
#include <G3MiOSSDK/PointCloudsRenderer.hpp>
#include <G3MiOSSDK/URL.hpp>
#include <G3MiOSSDK/DownloadPriority.hpp>

#include <G3MiOSSDK/SingleBilElevationDataProvider.hpp>
#include <G3MiOSSDK/PlanetRenderer.hpp>

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
                                          Geodetic3D::fromDegrees(44.950996472837502438,
                                                                  -93.098090960367656521,
                                                                  301.5960958814906121),
                                          Angle::zero(),
                                          Angle::fromDegrees(-26));
  }

};


void G3MStreamingPointCloud2DemoScene::rawActivate(const G3MContext *context) {
  G3MDemoModel* model     = getModel();
  G3MWidget*    g3mWidget = model->getG3MWidget();

  const float pointSize = 2;
  const float verticalExaggeration = 1;
  const double deltaHeight = -202;

  PlanetRenderer* planetRenderer = model->getPlanetRenderer();
  planetRenderer->setVerticalExaggeration(verticalExaggeration);

//  ElevationDataProvider* elevationDataProvider = new SingleBilElevationDataProvider(URL("file:///full-earth-2048x1024.bil"),
//                                                                                    Sector::fullSphere(),
//                                                                                    Vector2I(2048, 1024));
//  planetRenderer->setElevationDataProvider(elevationDataProvider, true);

//  ElevationDataProvider* elevationDataProvider = new SingleBilElevationDataProvider(URL("file:///loudoun_4326_300.bil", false),
//                                                                                    Sector::fromDegrees(38.8423858972, -77.3224512673,
//                                                                                                        39.3301640652, -77.9660628933),
//                                                                                    Vector2I(773, 586),
//                                                                                    0);
//  
//  planetRenderer->setElevationDataProvider(elevationDataProvider, true);

  MapBoxLayer* layer = new MapBoxLayer("examples.map-m0t0lrpu",
                                       TimeInterval::fromDays(30),
                                       true,
                                       2);
  model->getLayerSet()->addLayer(layer);

//#warning TODO cache
  model->getPointCloudsRenderer()->addPointCloud(URL("http://glob3mobile.dyndns.org:8080"),
                                                 "minnesota_LOD",
                                                 DownloadPriority::LOWER,
                                                 TimeInterval::zero(),
                                                 true,
                                                 PointCloudsRenderer::MIN_MAX_HEIGHT,
                                                 pointSize,
                                                 verticalExaggeration,
                                                 deltaHeight,
                                                 new G3MStreamingPointCloud2DemoScene_PointCloudMetadataListener(g3mWidget),
                                                 true);
}
