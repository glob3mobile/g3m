//
//  SimpleTileTexturizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_SimpleTileTexturizer_hpp
#define G3MiOSSDK_SimpleTileTexturizer_hpp

#include "TileTexturizer.hpp"


class SimpleTileTexturizer : public TileTexturizer {
public:
  virtual Mesh* texturize(const RenderContext* rc,
                          const Tile* tile,
                          Mesh* mesh) const;
};

#endif
