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

#include "IDownloadListener.hpp"
#include "MutableVector2D.hpp"


#include <string>
#include <vector>

#include "Tile.hpp"
#include "IFactory.hpp"

class TilePetitions;

struct FinishedTile{
  int   _level;
  int   _row;
  int   _column;
  int   _texID;
};

class SimpleTileTexturizer : public TileTexturizer{
private:
  
  const RenderContext* _rc;
  
  std::vector<FinishedTile> _finishedTiles;
  
  TilePetitions* getTilePetitions(const Tile* tile);
  
  std::vector<MutableVector2D> createTextureCoordinates() const;
  
  
  Mesh* getMesh(Tile* tile, Mesh* mesh);
  
  const int _resolution;
  
public:
  
  SimpleTileTexturizer(int resolution): _resolution(resolution){}
  
  virtual Mesh* texturize(const RenderContext* rc,
                          Tile* tile,
                          Mesh* mesh,
                          Mesh* previousMesh);
  
  void onTilePetitionsFinished(TilePetitions * tp);
  
};

#endif
