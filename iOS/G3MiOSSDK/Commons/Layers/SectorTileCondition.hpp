//
//  SectorTileCondition.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/4/13.
//
//

#ifndef __G3MiOSSDK__SectorTileCondition__
#define __G3MiOSSDK__SectorTileCondition__

#include "LayerCondition.hpp"
#include "Sector.hpp"

class SectorTileCondition : public LayerCondition {
private:
  const Sector _sector;

public:
  SectorTileCondition(const Sector& sector) :
  _sector(sector)
  {
  }

  bool isAvailable(const G3MRenderContext* rc,
                   const Tile* tile) const;

  bool isAvailable(const G3MEventContext* ec,
                   const Tile* tile) const;

  SectorTileCondition* copy() const;

};

#endif
