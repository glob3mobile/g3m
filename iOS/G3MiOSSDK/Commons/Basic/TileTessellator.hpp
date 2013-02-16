//
//  TileTessellator.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 27/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_TileTessellator_hpp
#define G3MiOSSDK_TileTessellator_hpp

class G3MRenderContext;
class Mesh;
class Tile;
class MutableVector2D;
class IFloatBuffer;

class TileTessellator {
public:
  virtual ~TileTessellator() { };
  
  virtual bool isReady(const G3MRenderContext *rc) const = 0;
  
  virtual Mesh* createMesh(const G3MRenderContext* rc,
                           const Tile* tile,
                           bool debug) const = 0;
  
  virtual Mesh* createDebugMesh(const G3MRenderContext* rc,
                                const Tile* tile) const = 0;

  virtual IFloatBuffer* createUnitTextCoords() const = 0;

};


#endif
