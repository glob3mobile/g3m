//
//  CartoDBLayer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 5/27/13.
//
//

#include "CartoDBLayer.hpp"

#include "LayerCondition.hpp"


const std::string CartoDBLayer::description() const {
  return "[CartoDBLayer]";
}

bool CartoDBLayer::rawIsEquals(const Layer* that) const {
  CartoDBLayer* t = (CartoDBLayer*) that;
  return (_domain == t->_domain);
}

CartoDBLayer* CartoDBLayer::copy() const {
  return new CartoDBLayer(_userName,
                          _table,
                          TimeInterval::fromMilliseconds(_timeToCacheMS),
                          _readExpired,
                          (_condition == NULL) ? NULL : _condition->copy());
}
