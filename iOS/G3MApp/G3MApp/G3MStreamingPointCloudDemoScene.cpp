//
//  G3MStreamingPointCloudDemoScene.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 8/19/14.
//  Copyright (c) 2014 Igo Software SL. All rights reserved.
//

#include "G3MStreamingPointCloudDemoScene.hpp"
#include <G3MiOSSDK/MapBoxLayer.hpp>
#include <G3MiOSSDK/LayerSet.hpp>
#include <G3MiOSSDK/G3MWidget.hpp>
#include <G3MiOSSDK/PointCloudsRenderer.hpp>
#include <G3MiOSSDK/URL.hpp>
#include <G3MiOSSDK/DownloadPriority.hpp>

#include <G3MiOSSDK/SingleBilElevationDataProvider.hpp>
#include <G3MiOSSDK/PlanetRenderer.hpp>

#include "G3MDemoModel.hpp"


class G3MStreamingPointCloudDemoScene_PointCloudMetadataListener : public PointCloudsRenderer::PointCloudMetadataListener {
private:
  G3MWidget* _g3mWidget;
public:
  G3MStreamingPointCloudDemoScene_PointCloudMetadataListener(G3MWidget* g3mWidget) :
  _g3mWidget(g3mWidget)
  {
  }

  void onMetadata(long long pointsCount,
                  const Sector& sector,
                  double minHeight,
                  double maxHeight) {
//    _g3mWidget->setAnimatedCameraPosition(Geodetic3D(sector._center, 26000),
//                                          Angle::zero(),
//                                          Angle::fromDegrees(-25));
////    _g3mWidget->setAnimatedCameraPosition( Geodetic3D(sector._center, 5000) );

//    _g3mWidget->setAnimatedCameraPosition( Geodetic3D::fromDegrees(39.068479748852752209, -77.602316923351310152, 100000)  );
//    _g3mWidget->setAnimatedCameraPosition( Geodetic3D::fromDegrees(39.068479748852752209, -77.602316923351310152, 83000)  );
    _g3mWidget->setAnimatedCameraPosition( Geodetic3D::fromDegrees(39.068479748852752209, -77.602316923351310152, 70000) );


//    _g3mWidget->setAnimatedCameraPosition( Geodetic3D::fromDegrees(39.053582130600460687, -77.512115783629141674, 530),
//                                          Angle::fromDegrees(23),
//                                          Angle::fromDegrees(-28));

  }

};


void G3MStreamingPointCloudDemoScene::rawActivate(const G3MContext *context) {
  G3MDemoModel* model     = getModel();
  G3MWidget*    g3mWidget = model->getG3MWidget();

  const float pointSize = 2;
  const float verticalExaggeration = 1;

  PlanetRenderer* planetRenderer = model->getPlanetRenderer();
  planetRenderer->setVerticalExaggeration(verticalExaggeration);

//  ElevationDataProvider* elevationDataProvider = new SingleBilElevationDataProvider(URL("file:///full-earth-2048x1024.bil"),
//                                                                                    Sector::fullSphere(),
//                                                                                    Vector2I(2048, 1024));
//  planetRenderer->setElevationDataProvider(elevationDataProvider, true);

  MapBoxLayer* layer = new MapBoxLayer("examples.map-m0t0lrpu",
                                       TimeInterval::fromDays(30),
                                       true,
                                       2);
  model->getLayerSet()->addLayer(layer);

#warning TODO cache
  model->getPointCloudsRenderer()->addPointCloud(URL("http://glob3mobile.dyndns.org:8080"),
                                                 //"Loudoun-VA_simplified2_LOD",
                                                 "Loudoun-VA_fragment_LOD",
                                                 DownloadPriority::LOWER,
                                                 TimeInterval::zero(),
                                                 false,
                                                 pointSize,
                                                 verticalExaggeration,
                                                 new G3MStreamingPointCloudDemoScene_PointCloudMetadataListener(g3mWidget),
                                                 true);

//  g3mWidget->setAnimatedCameraPosition( Geodetic3D::fromDegrees(39.23148585629368057, -77.641587694782629114, 5000) );

}
