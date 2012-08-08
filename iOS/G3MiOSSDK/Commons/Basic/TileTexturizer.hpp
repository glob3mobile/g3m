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
class TileTessellator;
class InitializationContext;

class TileTexturizer {
public:
  virtual ~TileTexturizer() {
  }
  
  virtual bool isReady(const RenderContext *rc) = 0;

  virtual void initialize(const InitializationContext* ic) = 0;

  virtual Mesh* texturize(const RenderContext* rc,
                          Tile* tile,
                          const TileTessellator* tessellator,
                          Mesh* tessellatorMesh,
                          Mesh* previousMesh) = 0;
  
  virtual void tileToBeDeleted(Tile* tile) = 0;
  
  virtual bool tileMeetsRenderCriteria(Tile* tile) = 0;
  
  virtual void justCreatedTopTile(Tile* tile) = 0;
  

};

#endif
