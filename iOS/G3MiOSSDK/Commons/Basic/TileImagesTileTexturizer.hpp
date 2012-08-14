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
#include "LayerSet.hpp"


class TilePetitions;
class WMSLayer;

class TileImagesTileTexturizer : public TileTexturizer {
private:
  
  const IFactory* _factory;
  TexturesHandler* _texturesHandler;
  IDownloader * _downloader;
  
  std::vector<TilePetitions*> _tilePetitions;
  std::vector<TilePetitions*> _tilePetitionsTopTile;
  
  const TilesRenderParameters *_parameters;
  
  WMSLayer* _layer;
  LayerSet * _layerSet;
  
  mutable float* _texCoordsCache;
  
  TilePetitions* createTilePetitions(const RenderContext* rc,
                                     const Tile* tile);
  
  float* getTextureCoordinates(const TileRenderContext* trc) const;
  
  void translateAndScaleFallBackTex(Tile* tile, Tile* fallbackTile, SimpleTextureMapping* tmap) const;
  
  void registerNewRequest(const RenderContext* rc,
                          Tile* tile);
  
  TilePetitions* getRegisteredTilePetitions(Tile* tile) const;
  void removeRegisteredTilePetitions(Tile* tile);
  
  Mesh* getFallBackTexturedMesh(Tile* tile,
                                const TileRenderContext* trc,
                                Mesh* tessellatorMesh,
                                Mesh* previousMesh);
  
  Mesh* getNewTextureMesh(Tile* tile,
                          const TileRenderContext* trc,
                          Mesh* mesh,
                          Mesh* previousMesh);
  
public:
  
  TileImagesTileTexturizer(const TilesRenderParameters* parameters,
                           IDownloader* downloader,
                           LayerSet* layerSet) : 
  _parameters(parameters),
  _layer(NULL),
  _downloader(downloader),
  _texCoordsCache(NULL),
  _layerSet(layerSet){
  }
  
  ~TileImagesTileTexturizer() {
    if (_texCoordsCache != NULL) {
      delete [] _texCoordsCache;
    }
  }
  
  Mesh* texturize(const RenderContext* rc,
                  const TileRenderContext* trc,
                  Tile* tile,
                  Mesh* mesh,
                  Mesh* previousMesh);
  
  void tileToBeDeleted(Tile* tile,
                       Mesh* mesh);
  
  bool tileMeetsRenderCriteria(Tile* tile);
  
  void justCreatedTopTile(const RenderContext* rc,
                          Tile* tile);
  
  bool isReady(const RenderContext *rc);
  
  void initialize(const InitializationContext* ic,
                  const TilesRenderParameters* parameters);
  
  void ancestorTexturedSolvedChanged(Tile* tile,
                                     Tile* ancestorTile,
                                     bool textureSolved);
  
  void onTerrainTouchEvent(const Geodetic3D& g3d, const Tile* tile){}
  
};

#endif
