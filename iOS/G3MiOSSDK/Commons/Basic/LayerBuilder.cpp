//
//  LayerBuilder.cpp
//  G3MiOSSDK
//
//  Created by Mari Luz Mateo on 21/12/12.
//
//

#include "LayerBuilder.hpp"
#include "LevelTileCondition.hpp"
#include "LayerSet.hpp"

LayerSet* LayerBuilder::createDefaultSatelliteImagery() {
  LayerSet* layerSet = new LayerSet();
  
  WMSLayer* blueMarble = new WMSLayer("bmng200405",
                                      URL("http://www.nasa.network.com/wms?", false),
                                      WMS_1_1_0,
                                      Sector::fullSphere(),
                                      "image/jpeg",
                                      "EPSG:4326",
                                      "",
                                      false,
                                      new LevelTileCondition(0, 6),
                                      TimeInterval::fromDays(30));
  layerSet->addLayer(blueMarble);
  
  WMSLayer* i3Landsat = new WMSLayer("esat",
                                     URL("http://data.worldwind.arc.nasa.gov/wms?", false),
                                     WMS_1_1_0,
                                     Sector::fullSphere(),
                                     "image/jpeg",
                                     "EPSG:4326",
                                     "",
                                     false,
                                     new LevelTileCondition(7, 10),
                                     TimeInterval::fromDays(30));
  layerSet->addLayer(i3Landsat);
  
  WMSLayer* bing = new WMSLayer("ve",
                                URL("http://worldwind27.arc.nasa.gov/wms/virtualearth?", false),
                                WMS_1_1_0,
                                Sector::fullSphere(),
                                "image/jpeg",
                                "EPSG:4326",
                                "",
                                false,
                                new LevelTileCondition(11, 1000),
                                TimeInterval::fromDays(30));
  layerSet->addLayer(bing);
  
  return layerSet;
}

WMSLayer* LayerBuilder::createBingLayer(bool enabled) {
  WMSLayer* bing = new WMSLayer("ve",
                                URL("http://worldwind27.arc.nasa.gov/wms/virtualearth?", false),
                                WMS_1_1_0,
                                Sector::fullSphere(),
                                "image/jpeg",
                                "EPSG:4326",
                                "",
                                false,
                                NULL,
                                TimeInterval::fromDays(30));
  bing->setEnable(enabled);
  
  return bing;
}

WMSLayer* LayerBuilder::createOSMLayer(bool enabled) {
  WMSLayer* osm = new WMSLayer("osm_auto:all",
                               URL("http://129.206.228.72/cached/osm", false),
                               WMS_1_1_0,
                               // Sector::fromDegrees(-85.05, -180.0, 85.05, 180.0),
                               Sector::fullSphere(),
                               "image/jpeg",
                               "EPSG:4326",
                               "",
                               false,
                               NULL,
                               TimeInterval::fromDays(30));
  osm->setEnable(enabled);
  
  return osm;  
}

WMSLayer* LayerBuilder::createPNOALayer(bool enabled) {
  WMSLayer *pnoa = new WMSLayer("PNOA",
                                URL("http://www.idee.es/wms/PNOA/PNOA", false),
                                WMS_1_1_0,
                                Sector::fromDegrees(21, -18, 45, 6),
                                "image/png",
                                "EPSG:4326",
                                "",
                                true,
                                NULL,
                                TimeInterval::fromDays(30));
  pnoa->setEnable(enabled);
  
  return pnoa;
}

WMSLayer* LayerBuilder::createBlueMarbleLayer(bool enabled) {
  WMSLayer* blueMarble = new WMSLayer("bmng200405",
                                      URL("http://www.nasa.network.com/wms?", false),
                                      WMS_1_1_0,
                                      Sector::fullSphere(),
                                      "image/jpeg",
                                      "EPSG:4326",
                                      "",
                                      false,
                                      new LevelTileCondition(0, 6),
                                      TimeInterval::fromDays(30));
  blueMarble->setEnable(enabled);
  
  return blueMarble;
}

WMSLayer* LayerBuilder::createI3LandSatLayer(bool enabled) {
  WMSLayer* i3Landsat = new WMSLayer("esat",
                                     URL("http://data.worldwind.arc.nasa.gov/wms?", false),
                                     WMS_1_1_0,
                                     Sector::fullSphere(),
                                     "image/jpeg",
                                     "EPSG:4326",
                                     "",
                                     false,
                                     new LevelTileCondition(7, 100),
                                     TimeInterval::fromDays(30));
  i3Landsat->setEnable(enabled);
  
  return i3Landsat;
}

WMSLayer* LayerBuilder::createPoliticalLayer(bool enabled) {
  WMSLayer* political = new WMSLayer("topp:cia",
                                     URL("http://worldwind22.arc.nasa.gov/geoserver/wms?", false),
                                     WMS_1_1_0,
                                     Sector::fullSphere(),
                                     "image/png",
                                     "EPSG:4326",
                                     "countryboundaries",
                                     true,
                                     NULL,
                                     TimeInterval::fromDays(30));
  political->setEnable(enabled);
  
  return political;
}
