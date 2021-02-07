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


Layer* LayerBuilder::createOSMLayer() {
  const std::string  urlTemplate   = "https://[abc].tile.openstreetmap.org/{level}/{x}/{y}.png";
  const Sector       dataSector    = Sector::FULL_SPHERE;
  const bool         isTransparent = false;
  const int          firstLevel    = 2;
  const int          maxLevel      = 18;
  const TimeInterval timeToCache   = TimeInterval::fromDays(30);
  const bool         readExpired   = true;

  return URLTemplateLayer::newMercator(urlTemplate,
                                       dataSector,
                                       isTransparent,
                                       firstLevel,
                                       maxLevel,
                                       timeToCache,
                                       readExpired);
}


LayerSet* LayerBuilder::createDefault() {
  LayerSet* layerSet = new LayerSet();

  layerSet->addLayer( createOSMLayer() );

  return layerSet;
}
