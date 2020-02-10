//
//  TileTessellator.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 27/06/12.
//

#include "TileTessellator.hpp"

#include "Geodetic2D.hpp"
#include "Vector2F.hpp"

const Vector2F TileTessellator::getTextCoord(const Tile* tile,
                                             const Geodetic2D& position) const {
  return getTextCoord(tile,
                      position._latitude,
                      position._longitude);
}
