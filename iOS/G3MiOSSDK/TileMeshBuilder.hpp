//
//  TileMeshBuilder.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 16/04/13.
//
//

#ifndef __G3MiOSSDK__TileMeshBuilder__
#define __G3MiOSSDK__TileMeshBuilder__

#include <iostream>

#include "Tile.hpp"
#include "TileTessellator.hpp"

class LeveledMesh;

class TileMeshBuilder{
  Tile* _tile;
  TileTessellator* _tesselator;
  ElevationDataProvider* _provider;
  Tile* _ancestorWithElevationDataSolved;
  
  Mesh* createMeshWithoutElevation(const Planet* planet,
                                   const Vector2I& resolution,
                                   bool debug) const;
  
  Mesh* createMeshWithElevation(const Planet* planet,
                                const Vector2I& resolution,
                                float verticalExaggeration,
                                bool debug) const;
  
  //TODO: DOWNLOAD LISTENER
  
  
public:
  
  TileMeshBuilder(Tile* tile,
                  TileTessellator* tesselator,
                  ElevationDataProvider* edp):
  _tile(tile),
  _tesselator(tesselator),
  _provider(edp),
  _ancestorWithElevationDataSolved(NULL){}
  
  void findAncestorWithElevationDataSolved();
  
  LeveledMesh* createTileMesh(const Planet* planet,
                              const Vector2I& resolution,
                              float verticalExaggeration,
                              bool debug,
                              double defaultHeight = 0);
  
};


#endif /* defined(__G3MiOSSDK__TileMeshBuilder__) */
