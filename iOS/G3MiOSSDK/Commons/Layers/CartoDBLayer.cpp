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

RenderState CartoDBLayer::getRenderState() {
  _errors.clear();
  if (_userName.compare("") == 0) {
    _errors.push_back("Missing layer parameter: userName");
  }
  if (_table.compare("") == 0) {
    _errors.push_back("Missing layer parameter: table");
  }
  
  if (_errors.size() > 0) {
    return RenderState::error(_errors);
  }
  return RenderState::ready();
}
