//
//  MapBoxLayer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/8/13.
//
//

#include "MapBoxLayer.hpp"

#include "LayerCondition.hpp"
#include "RenderState.hpp"
#include "TimeInterval.hpp"

const std::string MapBoxLayer::description() const {
  return "[MapBoxLayer]";
}

bool MapBoxLayer::rawIsEquals(const Layer* that) const {
  MapBoxLayer* t = (MapBoxLayer*) that;
  return (_domain == t->_domain);
}

MapBoxLayer* MapBoxLayer::copy() const {
  return new MapBoxLayer(_mapKey,
                         _timeToCache,
                         _readExpired,
                         _initialLevel,
                         _maxLevel,
                         _transparency,
                         (_condition == NULL) ? NULL : _condition->copy(),
                         _layerInfo);
}

RenderState MapBoxLayer::getRenderState() {
  _errors.clear();
  if (_mapKey.compare("") == 0) {
    _errors.push_back("Missing layer parameter: mapKey");
  }
  
  if (_errors.size() > 0) {
    return RenderState::error(_errors);
  }
  return RenderState::ready();
}
