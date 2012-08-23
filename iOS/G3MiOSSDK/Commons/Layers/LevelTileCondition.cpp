//
//  LevelTileCondition.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 23/08/12.
//
//

#include "LevelTileCondition.hpp"

#include "Tile.hpp"


bool LevelTileCondition::isAvailable(const RenderContext* rc,
                                     const Tile* tile) const {
  const int level = tile->getLevel();
  return ((level >= _minLevel) &&
          (level <= _maxLevel));
}

bool LevelTileCondition::isAvailable(const EventContext* ec,
                                     const Tile* tile) const {
  const int level = tile->getLevel();
  return ((level >= _minLevel) &&
          (level <= _maxLevel));
}
