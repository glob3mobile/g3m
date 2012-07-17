//
//  TileTexturizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_TileTexturizer_hpp
#define G3MiOSSDK_TileTexturizer_hpp

class Mesh;
class RenderContext;
class Tile;


class TileTexturizer {
  
public:
  virtual Mesh* texturize(const RenderContext* rc,
                          Tile* tile,
                          Mesh* mesh) = 0;
  
  virtual ~TileTexturizer() {
    
  }
};

#endif
