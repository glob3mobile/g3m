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
#include "TileRenderer.hpp"


class TilePetitions;
class WMSLayer;

struct RequestedTile{
  int   _level;
  int   _row;
  int   _column;
  int   _texID;  //-1 means no response yet
};

class SimpleTileTexturizer : public TileTexturizer {
private:
  
  const IFactory* _factory;
  TexturesHandler* _texHandler;
  
  std::vector<RequestedTile> _requestedTiles;
  const TileParameters *_parameters;
  
  WMSLayer* _layer;
  
  TilePetitions* getTilePetitions(const Tile* tile);
  
  std::vector<MutableVector2D> createTextureCoordinates() const;
  
  void registerNewRequest(int level, int row, int column){
    //Tile finished
    RequestedTile ft;
    ft._column = column;
    ft._level = level;
    ft._row = row;
    ft._texID = -1;
    _requestedTiles.push_back(ft);
  }
  
  RequestedTile* getRequestTex(int level, int row, int column){
    for (int i = 0; i < _requestedTiles.size(); i++) {
      RequestedTile& ft = _requestedTiles[i];
      if (level == ft._level && row == ft._row && column == ft._column){
        return &ft;
      }
    }
    return NULL;
  }
  
  
  
  Mesh* getMesh(Tile* tile, Mesh* mesh);
  
public:
  
  SimpleTileTexturizer(const TileParameters *par):_parameters(par), _layer(NULL){}
  
  Mesh* texturize(const RenderContext* rc,
                          Tile* tile,
                          Mesh* mesh,
                          Mesh* previousMesh);
  
  void onTilePetitionsFinished(TilePetitions * tp);
  
  void tileToBeDeleted(Tile* tile);
  
};

#endif
