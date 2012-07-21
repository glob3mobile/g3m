//
//  TileImagesTileTexturizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_TileImagesTileTexturizer_hpp
#define G3MiOSSDK_TileImagesTileTexturizer_hpp

#include "TileTexturizer.hpp"

#include "IDownloadListener.hpp"
#include "MutableVector2D.hpp"


#include <string>
#include <vector>

#include "Tile.hpp"
#include "IFactory.hpp"
#include "TileRenderer.hpp"
#include "TextureMapping.hpp"


class TilePetitions;
class WMSLayer;

struct RequestedTile{
  int   _level;
  int   _row;
  int   _column;
  int   _texID;  //-1 means no response yet
  std::vector<long> _downloads;
};

class TileImagesTileTexturizer : public TileTexturizer {
private:
  
  const IFactory* _factory;
  TexturesHandler* _texHandler;
  Downloader * _downloader;
  
  std::vector<RequestedTile> _requestedTiles;
  const TileParameters *_parameters;
  
  WMSLayer* _layer;
  
  mutable std::vector<MutableVector2D>* _texCoordsCache;
  
  TilePetitions* getTilePetitions(const Tile* tile);
  
  std::vector<MutableVector2D> getTextureCoordinates(const TileTessellator* tessellator) const;
  
  void translateAndScaleFallBackTex(Tile* tile, Tile* fallbackTile, TextureMapping* tmap) const;
  
  void registerNewRequest(Tile* tile);
  
  RequestedTile* getRequestTile(int level, int row, int column){
    for (int i = 0; i < _requestedTiles.size(); i++) {
      RequestedTile& ft = _requestedTiles[i];
      if ((level  == ft._level) &&
          (row    == ft._row)   &&
          (column == ft._column)){
        return &ft;
      }
    }
    return NULL;
  }
  
  bool isTextureAvailable(Tile* tile)
  {
    RequestedTile* ft = getRequestTile(tile->getLevel(), tile->getRow(), tile->getColumn());
    return !(ft == NULL || ft->_texID < 0);
  }
  
  bool isTextureRequested(Tile* tile)
  {
    RequestedTile* ft = getRequestTile(tile->getLevel(), tile->getRow(), tile->getColumn());
    return ft != NULL;
  }
  
  Mesh* getFallBackTexturedMesh(Tile* tile,
                                const TileTessellator* tessellator,
                                Mesh* tessellatorMesh);
  
  Mesh* getNewTextureMesh(Tile* tile,
                          const TileTessellator* tessellator,
                          Mesh* mesh);
  
  Mesh* getMesh(Tile* tile,
                const TileTessellator* tessellator,
                Mesh* tessellatorMesh,
                Mesh* previousMesh);
  
public:
  
  TileImagesTileTexturizer(const TileParameters* parameters,
                           Downloader* downloader) : 
  _parameters(parameters),
  _layer(NULL),
  _downloader(downloader),
  _texCoordsCache(NULL) {
  }
  
  ~TileImagesTileTexturizer() {
    if (_texCoordsCache != NULL) {
      delete _texCoordsCache;
    }
  }
  
  Mesh* texturize(const RenderContext* rc,
                  Tile* tile,
                  const TileTessellator* tessellator,
                  Mesh* mesh,
                  Mesh* previousMesh);
  
  void onTilePetitionsFinished(TilePetitions * tp);
  
  void tileToBeDeleted(Tile* tile);
  
  bool tileMeetsRenderCriteria(Tile* tile);
  
  void justCreatedTopTile(Tile* tile);
  
  bool isReadyToRender(const RenderContext *rc);


};

#endif
