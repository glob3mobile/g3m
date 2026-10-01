//
//  LayerBuilder.cpp
//  G3M
//
//  Created by Mari Luz Mateo on 21/12/12.
//
//

#include "LayerBuilder.hpp"

#include "LayerSet.hpp"
#include "URLTemplateLayer.hpp"
#include "Info.hpp"


Layer* LayerBuilder::createOSMLayer() {
  const std::string  urlTemplate   = "https://tile.openstreetmap.org/{level}/{x}/{y}.png";
  const Sector       dataSector    = Sector::FULL_SPHERE;
  const bool         isTransparent = false;
  const int          firstLevel    = 2;
  const int          maxLevel      = 18;
  const TimeInterval timeToCache   = TimeInterval::fromDays(30);
  const bool         readExpired   = true;
  const float        transparency  = 1;
  const LayerCondition* condition  = NULL;

  std::vector<const Info*>* layerInfo = new std::vector<const Info*>();
  layerInfo->push_back(new Info("Map data by OpenStreetMap contributors (ODbL)"));

  return URLTemplateLayer::newMercator(urlTemplate,
                                       dataSector,
                                       isTransparent,
                                       firstLevel,
                                       maxLevel,
                                       timeToCache,
                                       readExpired,
                                       transparency,
                                       condition,
                                       layerInfo);
}


Layer* LayerBuilder::createSentinel2CloudlessLayer() {
  const std::string  urlTemplate   = "https://tiles.maps.eox.at/wmts/1.0.0/s2cloudless-2020_3857/default/g/{level}/{y}/{x}.jpg";
  const Sector       dataSector    = Sector::FULL_SPHERE;
  const bool         isTransparent = false;
  const int          firstLevel    = 2;
  const int          maxLevel      = 17; // deepest level served by EOX
  const TimeInterval timeToCache   = TimeInterval::fromDays(30);
  const bool         readExpired   = true;
  const float        transparency  = 1;
  const LayerCondition* condition  = NULL;

  std::vector<const Info*>* layerInfo = new std::vector<const Info*>();
  layerInfo->push_back(new Info("Sentinel-2 cloudless by EOX IT Services GmbH (Contains modified Copernicus Sentinel data 2020)"));

  return URLTemplateLayer::newMercator(urlTemplate,
                                       dataSector,
                                       isTransparent,
                                       firstLevel,
                                       maxLevel,
                                       timeToCache,
                                       readExpired,
                                       transparency,
                                       condition,
                                       layerInfo);
}


Layer* LayerBuilder::createBlueMarbleLayer() {
  const std::string  urlTemplate   = "https://tiles.maps.eox.at/wmts/1.0.0/bluemarble_3857/default/g/{level}/{y}/{x}.jpg";
  const Sector       dataSector    = Sector::FULL_SPHERE;
  const bool         isTransparent = false;
  const int          firstLevel    = 2;
  const int          maxLevel      = 8; // deepest level served by EOX
  const TimeInterval timeToCache   = TimeInterval::fromDays(30);
  const bool         readExpired   = true;
  const float        transparency  = 1;
  const LayerCondition* condition  = NULL;

  std::vector<const Info*>* layerInfo = new std::vector<const Info*>();
  layerInfo->push_back(new Info("Blue Marble Next Generation by NASA, rendered by EOX IT Services GmbH"));

  return URLTemplateLayer::newMercator(urlTemplate,
                                       dataSector,
                                       isTransparent,
                                       firstLevel,
                                       maxLevel,
                                       timeToCache,
                                       readExpired,
                                       transparency,
                                       condition,
                                       layerInfo);
}


LayerSet* LayerBuilder::createDefault() {
  LayerSet* layerSet = new LayerSet();

  layerSet->addLayer( createOSMLayer() );

  return layerSet;
}
