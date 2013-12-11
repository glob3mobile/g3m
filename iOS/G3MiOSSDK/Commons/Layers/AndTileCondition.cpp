//
//  AndTileCondition.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/5/13.
//
//

#include "AndTileCondition.hpp"

AndTileCondition::~AndTileCondition() {
  int size = _children.size();
  for (int i = 0; i < size; i++) {
    LayerCondition* child = _children[i];
    delete child;
  }
}

bool AndTileCondition::isAvailable(const G3MRenderContext* rc,
                                   const Tile* tile) const {
  int size = _children.size();
  for (int i = 0; i < size; i++) {
    LayerCondition* child = _children[i];
    if (!child->isAvailable(rc, tile)) {
      return false;
    }
  }
  return true;
}

bool AndTileCondition::isAvailable(const G3MEventContext* ec,
                                   const Tile* tile) const {
  int size = _children.size();
  for (int i = 0; i < size; i++) {
    LayerCondition* child = _children[i];
    if (!child->isAvailable(ec, tile)) {
      return false;
    }
  }
  return true;
}

LayerCondition* AndTileCondition::copy() const {
  return new AndTileCondition(_children);
}
