//
//  LevelTileCondition.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 23/08/12.
//
//

#include "LevelTileCondition.hpp"

#include "Tile.hpp"


bool LevelTileCondition::isAvailable(const G3MRenderContext* rc,
                                     const Tile* tile) const {
  const int level = tile->_level;
  return ((level >= _minLevel) &&
          (level <= _maxLevel));
}

bool LevelTileCondition::isAvailable(const G3MEventContext* ec,
                                     const Tile* tile) const {
  const int level = tile->_level;
  return ((level >= _minLevel) &&
          (level <= _maxLevel));
}

LayerCondition* LevelTileCondition::copy() const {
  return new LevelTileCondition(_minLevel,
                                _maxLevel);
}
