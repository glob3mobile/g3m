//
//  G3MDemoBuilder.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/14/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#include "G3MDemoBuilder.hpp"

#include <G3MiOSSDK/PlanetRendererBuilder.hpp>
#include <G3MiOSSDK/SingleBillElevationDataProvider.hpp>
#include <G3MiOSSDK/LayerSet.hpp>
#include <G3MiOSSDK/MapBoxLayer.hpp>
#include <G3MiOSSDK/MapQuestLayer.hpp>
#include <G3MiOSSDK/WMSLayer.hpp>
#include <G3MiOSSDK/LevelTileCondition.hpp>
#include <G3MiOSSDK/OSMLayer.hpp>
#include <G3MiOSSDK/BingMapsLayer.hpp>
#include <G3MiOSSDK/URLTemplateLayer.hpp>
#include <G3MiOSSDK/IG3MBuilder.hpp>

#include "G3MDemoModel.hpp"


G3MDemoBuilder::G3MDemoBuilder(G3MDemoListener* listener) :
_model( new G3MDemoModel(listener) )
{
}

G3MDemoBuilder::~G3MDemoBuilder() {
}

LayerSet* G3MDemoBuilder::createLayerSet() {
  LayerSet* layerSet = new LayerSet();


  MapBoxLayer* mboxOSMLayer = new MapBoxLayer("examples.map-cnkhv76j",
                                              TimeInterval::fromDays(30),
                                              true,
                                              2);
  mboxOSMLayer->setTitle("Map Box OSM");
  mboxOSMLayer->setEnable(true);
  layerSet->addLayer(mboxOSMLayer);


  MapQuestLayer* mqOSM = MapQuestLayer::newOSM(TimeInterval::fromDays(30));
  mqOSM->setEnable(false);
  mqOSM->setTitle("MapQuest OSM");
  layerSet->addLayer(mqOSM);


  MapQuestLayer* mqlAerial = MapQuestLayer::newOpenAerial(TimeInterval::fromDays(30));
  mqlAerial->setTitle("MapQuest Aerial");
  mqlAerial->setEnable(false);
  layerSet->addLayer(mqlAerial);


  MapBoxLayer* mboxAerialLayer = new MapBoxLayer("examples.map-m0t0lrpu",
                                                 TimeInterval::fromDays(30),
                                                 true,
                                                 2);
  mboxAerialLayer->setTitle("Map Box Aerial");
  mboxAerialLayer->setEnable(false);
  layerSet->addLayer(mboxAerialLayer);


  MapBoxLayer* mboxTerrainLayer = new MapBoxLayer("examples.map-qogxobv1",
                                                  TimeInterval::fromDays(30),
                                                  true,
                                                  2);
  mboxTerrainLayer->setTitle("Map Box Terrain");
  mboxTerrainLayer->setEnable(false);
  layerSet->addLayer(mboxTerrainLayer);


  WMSLayer* blueMarble = new WMSLayer("bmng200405",
                                      URL("http://www.nasa.network.com/wms?"),
                                      WMS_1_1_0,
                                      Sector::fullSphere(),
                                      "image/jpeg",
                                      "EPSG:4326",
                                      "",
                                      false,
                                      new LevelTileCondition(0, 18),
                                      TimeInterval::fromDays(30),
                                      true);
  blueMarble->setTitle("WMS Nasa Blue Marble");
  blueMarble->setEnable(false);
  layerSet->addLayer(blueMarble);


  OSMLayer* osmLayer = new OSMLayer(TimeInterval::fromDays(30));
  osmLayer->setTitle("Open Street Map");
  osmLayer->setEnable(false);
  layerSet->addLayer(osmLayer);


  BingMapsLayer* bingMapsAerialLayer = new BingMapsLayer(BingMapType::Aerial(),
                                                         "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc",
                                                         TimeInterval::fromDays(30));
  bingMapsAerialLayer->setTitle("Bing Aerial");
  bingMapsAerialLayer->setEnable(false);
  layerSet->addLayer(bingMapsAerialLayer);


  BingMapsLayer* bingMapsAerialWithLabels = new BingMapsLayer(BingMapType::AerialWithLabels(),
                                                              "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc",
                                                              TimeInterval::fromDays(30));
  bingMapsAerialWithLabels->setTitle("Bing Aerial With Labels");
  bingMapsAerialWithLabels->setEnable(false);
  layerSet->addLayer(bingMapsAerialWithLabels);

  BingMapsLayer* bingMapsCollinsBart = new BingMapsLayer(BingMapType::CollinsBart(),
                                                         "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc",
                                                         TimeInterval::fromDays(30));
  bingMapsCollinsBart->setTitle("MapQuest OSM");
  bingMapsCollinsBart->setEnable(false);
  layerSet->addLayer(bingMapsCollinsBart);


  std::vector<std::string> subdomains;
  subdomains.push_back("0.");
  subdomains.push_back("1.");
  subdomains.push_back("2.");
  subdomains.push_back("3.");
  MercatorTiledLayer* meteoritesLayer = new MercatorTiledLayer("CartoDB-meteoritessize",
                                                               "http://",
                                                               "tiles.cartocdn.com/osm2/tiles/meteoritessize",
                                                               subdomains,
                                                               "png",
                                                               TimeInterval::fromDays(90),
                                                               false,
                                                               Sector::fullSphere(),
                                                               2,
                                                               17,
                                                               NULL);
  meteoritesLayer->setTitle("CartoDB Meteorites");
  meteoritesLayer->setEnable(false);
  layerSet->addLayer(meteoritesLayer);


  URLTemplateLayer* arcGISOverlayLayerTest = URLTemplateLayer::newMercator("http://www.fairfaxcounty.gov/gis/rest/services/DMV/DMV/MapServer/export?dpi=96&transparent=true&format=png8&bbox={west}%2C{south}%2C{east}%2C{north}&bboxSR=3857&imageSR=3857&size={width}%2C{height}&f=image",
                                                                           Sector::fullSphere(),
                                                                           true,
                                                                           2,
                                                                           18,
                                                                           TimeInterval::fromDays(30),
                                                                           true,
                                                                           new LevelTileCondition(12, 18));
  arcGISOverlayLayerTest->setTitle("ESRI ArcGis Online");
  arcGISOverlayLayerTest->setEnable(false);
  layerSet->addLayer(arcGISOverlayLayerTest);


  return layerSet;
}

void G3MDemoBuilder::build() {
  IG3MBuilder*  builder = getG3MBuilder();

  const float verticalExaggeration = 6.0f;
  builder->getPlanetRendererBuilder()->setVerticalExaggeration(verticalExaggeration);

  ElevationDataProvider* elevationDataProvider = new SingleBillElevationDataProvider(URL("file:///full-earth-2048x1024.bil", false),
                                                                                     Sector::fullSphere(),
                                                                                     Vector2I(2048, 1024));
  builder->getPlanetRendererBuilder()->setElevationDataProvider(elevationDataProvider);


  builder->getPlanetRendererBuilder()->setLayerSet(createLayerSet());

  //  ShapesRenderer* shapeRenderer = new ShapesRenderer();
  //  shapeRenderer->setEnable(false);
  //  builder.addRenderer(shapeRenderer);
  //
  //  MarksRenderer* marksRenderer = G3MMarksRenderer::createMarksRenderer(self);
  //  marksRenderer->setEnable(false);
  //  builder.addRenderer(marksRenderer);
  //
  //  MeshRenderer* meshRenderer = G3MMeshRenderer::createMeshRenderer(builder.getPlanet());
  //  meshRenderer->setEnable(false);
  //  builder.addRenderer(meshRenderer);
  //
  //  G3MAppUserData* userData = new G3MAppUserData(layerSet,
  //                                                shapeRenderer,
  //                                                marksRenderer,
  //                                                meshRenderer);
  //  userData->setSatelliteLayerEnabled(true);
  //  userData->setLayerSet([self createLayerSet: userData->getSatelliteLayerEnabled()]);
  //  userData->setMarksRenderer(marksRenderer);
  //  userData->setShapeRenderer(shapeRenderer);
  //  userData->setMeshRenderer(meshRenderer);

  // Setup the builder
  //  builder.getPlanetRendererBuilder()->setShowStatistics(true);
  //  builder.setInitializationTask(new G3MAppInitializationTask(self.g3mWidget), true);
  //  builder.setUserData(userData);

}

G3MDemoModel* G3MDemoBuilder::getModel() {
  return _model;
}
