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
class TilesRenderParameters;
class TileRenderContext;
class Geodetic3D;

#include "TerrainTouchEventListener.hpp"

class TileTexturizer {
public:
  virtual ~TileTexturizer() {
  }
  
  virtual bool isReady(const RenderContext *rc) = 0;
  
  virtual void initialize(const InitializationContext* ic,
                          const TilesRenderParameters* parameters) = 0;
  
  virtual Mesh* texturize(const RenderContext* rc,
                          const TileRenderContext* trc,
                          Tile* tile,
                          Mesh* tessellatorMesh,
                          Mesh* previousMesh) = 0;
  
  virtual void tileToBeDeleted(Tile* tile,
                               Mesh* mesh) = 0;
  
  virtual void tileMeshToBeDeleted(Tile* tile,
                                   Mesh* mesh) = 0;
  
  virtual bool tileMeetsRenderCriteria(Tile* tile) = 0;
  
  virtual void justCreatedTopTile(const RenderContext* rc,
                                  Tile* tile) = 0;
  
  virtual void ancestorTexturedSolvedChanged(Tile* tile,
                                             Tile* ancestorTile,
                                             bool textureSolved) = 0;
  
  virtual void onTerrainTouchEvent(const EventContext* ec,
                                   const Geodetic3D& position,
                                   const Tile* tile) = 0;
  
};

#endif
