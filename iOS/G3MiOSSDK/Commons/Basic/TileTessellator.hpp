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
class Planet;
class Mesh;
class Tile;
class MutableVector2D;
class IFloatBuffer;
class ElevationData;

#include "Vector2I.hpp"

class TileTessellator {
public:
  virtual ~TileTessellator() { };

  virtual bool isReady(const G3MRenderContext *rc) const = 0;

  virtual Mesh* createTileMesh(const Planet* planet,
                               const Tile* tile,
                               const ElevationData* elevationData,
                               float verticalExaggeration,
                               bool debug) const = 0;

  virtual Vector2I getTileMeshResolution(const Planet* planet,
                                         const Tile* tile,
                                         bool debug) const = 0;

  virtual Mesh* createTileDebugMesh(const Planet* planet,
                                    const Tile* tile) const = 0;

  virtual IFloatBuffer* createUnitTextCoords(const Tile* tile) const = 0;
  
};


#endif
