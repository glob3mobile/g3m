//
//  G3MScenarioDEMDemoScene.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/20/13.
//

#include "G3MScenarioDEMDemoScene.hpp"

#include <G3MSharedSDK/G3MWidget.hpp>
#include <G3MSharedSDK/PlanetRenderer.hpp>
#include <G3MSharedSDK/SingleBILElevationDataProvider.hpp>
#include <G3MSharedSDK/BingMapsLayer.hpp>
#include <G3MSharedSDK/LayerSet.hpp>
#include <G3MSharedSDK/Geodetic3D.hpp>
#include <G3MSharedSDK/Color.hpp>

#include "G3MDemoModel.hpp"

void G3MScenarioDEMDemoScene::rawActivate(const G3MContext* context) {
  G3MDemoModel* model     = getModel();
  G3MWidget*    g3mWidget = model->getG3MWidget();

  PlanetRenderer* planetRenderer = model->getPlanetRenderer();
  planetRenderer->setVerticalExaggeration(2);

  g3mWidget->setBackgroundColor( Color::fromRGBA255(185, 221, 209, 255).muchDarker() );


  const Sector demSector = Sector::fromDegrees(40.1665739916489, -5.85449532145337,
                                               40.3320215899527, -5.5116079822178570);

  g3mWidget->setRenderedSector(demSector.shrinkedByPercent(0.2f));

  const double deltaHeight = -700.905;
  ElevationDataProvider* elevationDataProvider = new SingleBILElevationDataProvider(URL("file:///0576.bil"),
                                                                                    demSector,
                                                                                    Vector2I(2516, 1335),
                                                                                    deltaHeight);
  planetRenderer->setElevationDataProvider(elevationDataProvider, true);


  BingMapsLayer* layer = new BingMapsLayer(BingMapType::Aerial(),
                                           "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc",
                                           TimeInterval::fromDays(30));
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
