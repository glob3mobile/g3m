//
//  SectorTileCondition.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/4/13.
//
//

#include "SectorTileCondition.hpp"

#include "Tile.hpp"

bool SectorTileCondition::isAvailable(const G3MRenderContext* rc,
                                      const Tile* tile) const {
  return _sector.touchesWith(tile->_sector);
}

bool SectorTileCondition::isAvailable(const G3MEventContext* ec,
                                      const Tile* tile) const {
  return _sector.touchesWith(tile->_sector);
}

SectorTileCondition* SectorTileCondition::copy() const {
  return new SectorTileCondition(_sector);
}
