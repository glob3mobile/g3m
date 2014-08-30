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

#include "G3MDemoModel.hpp"


void G3MStreamingPointCloudDemoScene::rawActivate(const G3MContext *context) {
  G3MDemoModel* model     = getModel();
  G3MWidget*    g3mWidget = model->getG3MWidget();

  MapBoxLayer* layer = new MapBoxLayer("examples.map-m0t0lrpu",
                                       TimeInterval::fromDays(30),
                                       true,
                                       2);
  model->getLayerSet()->addLayer(layer);

#warning TODO cache
  model->getPointCloudsRenderer()->addPointCloud(URL("http://192.168.1.6:8080"),
                                                 "Loudoun-VA_LOD",
                                                 DownloadPriority::LOWER,
                                                 TimeInterval::zero(),
                                                 false);

  // g3mWidget->setAnimatedCameraPosition( Geodetic3D::fromDegrees(39.102078762909024, -77.66098022460936, 50000) );
  // g3mWidget->setAnimatedCameraPosition( Geodetic3D::fromDegrees(39.208333053497035792, -77.64400127682505115, 6000) );
  g3mWidget->setAnimatedCameraPosition( Geodetic3D::fromDegrees(39.23148585629368057, -77.641587694782629114, 5000) );


// Camera position=(lat=39.23148585629368057d, lon=-77.641587694782629114d, height=6008.4517767920915503) heading=-0.001518 pitch=-90.000000

/*
 latitude  =  39° 15' 53.35" N
 longitude =  77° 33'  7.49" W
 */

}
