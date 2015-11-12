//
//  G3MWMS_DEMDemoScene.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/20/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#include "G3MWMS_DEMDemoScene.hpp"

#include <G3MiOSSDK/G3MWidget.hpp>
#include <G3MiOSSDK/PlanetRenderer.hpp>
#include <G3MiOSSDK/SingleBilElevationDataProvider.hpp>
#include <G3MiOSSDK/MapBoxLayer.hpp>
#include <G3MiOSSDK/NASAElevationDataProvider.hpp>

#include "G3MDemoModel.hpp"

void G3MWMS_DEMDemoScene::rawActivate(const G3MContext* context) {
  G3MDemoModel* model     = getModel();
  G3MWidget*    g3mWidget = model->getG3MWidget();

  PlanetRenderer* planetRenderer = model->getPlanetRenderer();
  planetRenderer->setVerticalExaggeration(2);

  g3mWidget->setBackgroundColor( Color::fromRGBA255(185, 221, 209, 255).muchDarker() );

  ElevationDataProvider* edp = new NASAElevationDataProvider();
  planetRenderer->setElevationDataProvider(edp, true);

  MapBoxLayer* layer = new MapBoxLayer("examples.map-qogxobv1",
                                       TimeInterval::fromDays(30),
                                       true,
                                       2);
  model->getLayerSet()->addLayer(layer);


  const Geodetic3D position = Geodetic3D::fromDegrees(28.6004501909256774183631932829,
                                                      -17.9961281315742489539388770936,
                                                      4499.92370976353322475915774703);

  const Angle heading = Angle::fromDegrees(-43.474467);
  const Angle pitch = Angle::fromDegrees(-9.857420);

  g3mWidget->setCameraPosition(position);
  g3mWidget->setCameraHeading(heading);
  g3mWidget->setCameraPitch(pitch);
}
