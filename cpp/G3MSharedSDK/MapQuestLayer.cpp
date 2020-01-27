//
//  MapQuestLayer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/8/13.
//
//

#include "MapQuestLayer.hpp"

#include "LayerCondition.hpp"
#include "RenderState.hpp"
#include "TimeInterval.hpp"

const std::string MapQuestLayer::description() const {
  return "[MapQuestLayer]";
}

bool MapQuestLayer::rawIsEquals(const Layer* that) const {
  MapQuestLayer* t = (MapQuestLayer*) that;
  return (_domain == t->_domain);
}

MapQuestLayer* MapQuestLayer::copy() const {
  return new MapQuestLayer(_domain,
                           _subdomains,
                           _initialLevel,
                           _maxLevel,
                           _timeToCache,
                           _readExpired,
                           _transparency,
                           (_condition == NULL) ? NULL : _condition->copy(),
                           _layerInfo);
}

RenderState MapQuestLayer::getRenderState() {
  return RenderState::ready();
}
