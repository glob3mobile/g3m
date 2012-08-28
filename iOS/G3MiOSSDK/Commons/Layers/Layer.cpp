//
//  Layer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 23/08/12.
//
//

#include "Layer.hpp"
#include "LayerCondition.hpp"


bool Layer::isAvailable(const RenderContext* rc,
                         const Tile* tile) const {
  if (_condition == NULL) {
    return true;
  }
  return _condition->isAvailable(rc, tile);
}

bool Layer::isAvailable(const EventContext* ec,
                         const Tile* tile) const {
  if (_condition == NULL) {
    return true;
  }
  return _condition->isAvailable(ec, tile);
}
