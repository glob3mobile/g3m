//
//  OSMLayer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/6/13.
//
//

#include "OSMLayer.hpp"

#include "LayerCondition.hpp"
#include "RenderState.hpp"
#include "TimeInterval.hpp"


const std::string OSMLayer::description() const {
  return "[OSMLayer]";
}

bool OSMLayer::rawIsEquals(const Layer* that) const {
  return true;
}

OSMLayer* OSMLayer::copy() const {
  return new OSMLayer(_timeToCache,
                      _readExpired,
                      _initialLevel,
                      _transparency,
                      (_condition == NULL) ? NULL : _condition->copy(),
                      _layerInfo);
}

RenderState OSMLayer::getRenderState() {
  return RenderState::ready();
}
