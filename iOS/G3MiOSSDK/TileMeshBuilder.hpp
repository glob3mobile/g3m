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
#include "ElevationDataProvider.hpp"

class LeveledMesh;

class ElevationDataProviderListener: public IElevationDataListener{
  TileMeshBuilder* _meshBuilder;
  long long _id;
  ElevationDataProvider* _provider;
public:
  ElevationDataProviderListener(TileMeshBuilder* meshBuilder):
  _meshBuilder(meshBuilder),
  _provider(NULL)
  {}
  
  void launch(ElevationDataProvider* provider,
              const Sector& sector,
              const Vector2I& resolution);
  
  void cancel();
  
  void onData(const Sector& sector,
              const Vector2I& resolution,
              ElevationData* elevationData);
  
  void onError(const Sector& sector,
               const Vector2I& resolution);
  
};


class TileMeshBuilder{
  Tile* _tile;
  Tile* _ancestorWithElevationDataSolved;
  ElevationDataProviderListener* _listener;
  ElevationDataProvider* _provider;
  int _resX, _resY;
  /*
  Mesh* createMeshWithoutElevation(const TileTessellator* tesselator,
                                   const Planet* planet,
                                   const Vector2I& resolution,
                                   bool debug) const;
  
  Mesh* createMeshWithElevation(const TileTessellator* tesselator,
                                const Planet* planet,
                                const Vector2I& resolution,
                                float verticalExaggeration,
                                bool debug) const;
  */
  void sendPetition(ElevationDataProvider* provider, const Vector2I& resolution);

  void findAncestorWithElevationDataSolved();
  void fillTileWithAncestorElevationData(ElevationDataProvider* provider);
  
public:
  
  TileMeshBuilder(Tile* tile):
  _tile(tile),
  _ancestorWithElevationDataSolved(NULL),
  _listener(NULL),
  _provider(NULL)
  {
  }
  
  /*
  LeveledMesh* createTileMesh(const TileTessellator* tesselator,
                              ElevationDataProvider* provider,
                              const Planet* planet,
                              const Vector2I& resolution,
                              float verticalExaggeration,
                              bool debug,
                              double defaultHeight = 0);
   */
  
  void fillTileWithElevationData(ElevationDataProvider* provider, const Vector2I& resolution);
  
  void onAncestorSolvedElevationData(Tile* ancestor);
  void onSolvedElevationData(ElevationData* ed);
  void cancelElevationDataRequest();
};


#endif /* defined(__G3MiOSSDK__TileMeshBuilder__) */
