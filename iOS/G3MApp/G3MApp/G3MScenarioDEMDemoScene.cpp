//
//  G3MScenarioDEMDemoScene.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/20/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#include "G3MScenarioDEMDemoScene.hpp"

#include <G3MiOSSDK/G3MWidget.hpp>
#include <G3MiOSSDK/PlanetRenderer.hpp>
#include <G3MiOSSDK/SingleBilElevationDataProvider.hpp>
#include <G3MiOSSDK/MapBoxLayer.hpp>

#include "G3MDemoModel.hpp"

void G3MScenarioDEMDemoScene::rawActivate(const G3MContext* context) {
  G3MDemoModel* model     = getModel();
  G3MWidget*    g3mWidget = model->getG3MWidget();

  PlanetRenderer* planetRenderer = model->getPlanetRenderer();
  planetRenderer->setVerticalExaggeration(2);

  g3mWidget->setBackgroundColor( Color::fromRGBA255(185, 221, 209, 255).muchDarker() );


  const Sector demSector = Sector::fromDegrees(40.1665739916489, -5.85449532145337,
                                               40.3320215899527, -5.5116079822178570);


  g3mWidget->setShownSector(demSector.shrinkedByPercent(0.2f));

  const double deltaHeight = -700.905;
  ElevationDataProvider* elevationDataProvider = new SingleBilElevationDataProvider(URL("file:///0576.bil"),
                                                                                     demSector,
                                                                                     Vector2I(2516, 1335),
                                                                                     deltaHeight);
  planetRenderer->setElevationDataProvider(elevationDataProvider, true);


  MapBoxLayer* layer = new MapBoxLayer("examples.map-qogxobv1",
                                       TimeInterval::fromDays(30),
                                       true,
                                       11);
  model->getLayerSet()->addLayer(layer);


  const Geodetic3D position = Geodetic3D::fromDegrees(40.13966959177994, -5.89060128999895, 4694.511700438305);
  //const Angle heading = Angle::fromDegrees(51.146970);
  //const Angle pitch = Angle::fromDegrees(69.137225);
  const Angle heading = Angle::fromDegrees(-51.146970);
  const Angle pitch = Angle::fromDegrees(-20.862775);

  g3mWidget->setCameraPosition(position);
  g3mWidget->setCameraHeading(heading);
  g3mWidget->setCameraPitch(pitch);
}
