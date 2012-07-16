//
//  TileTessellator.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 27/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_TileTessellator_hpp
#define G3MiOSSDK_TileTessellator_hpp

class RenderContext;
class Mesh;
class Tile;

class TileTessellator {
public:
  virtual ~TileTessellator() { };
  
  virtual Mesh* createMesh(const RenderContext* rc, const Tile* tile) const = 0;
  
};


#endif
