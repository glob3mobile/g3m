//
//  TileTessellator.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 27/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#include "TileTessellator.hpp"

#include "Geodetic2D.hpp"

const Vector2F TileTessellator::getTextCoord(const Tile* tile,
                                             const Geodetic2D& position,
                                             bool mercator) const {
  return getTextCoord(tile,
                      position._latitude,
                      position._longitude,
                      mercator);
}
