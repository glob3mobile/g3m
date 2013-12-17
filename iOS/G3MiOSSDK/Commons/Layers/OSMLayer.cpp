//
//  OSMLayer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/6/13.
//
//

#include "OSMLayer.hpp"

#include "LayerCondition.hpp"


const std::string OSMLayer::description() const {
  return "[OSMLayer]";
}

bool OSMLayer::rawIsEquals(const Layer* that) const {
  return true;
}

OSMLayer* OSMLayer::copy() const {
  return new OSMLayer(TimeInterval::fromMilliseconds(_timeToCacheMS),
                      _readExpired,
                      _initialLevel,
                      (_condition == NULL) ? NULL : _condition->copy());
}

RenderState OSMLayer::getRenderState() {
  return RenderState::ready();
}