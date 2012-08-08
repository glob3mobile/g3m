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
  TexturesHandler* _texHandler;
  Downloader * _downloader;
  const RenderContext * _renderContext;
  
  std::vector<TilePetitions*> _tilePetitions;
  std::vector<TilePetitions*> _tilePetitionsTopTile;
  
  const TileParameters *_parameters;
  
  WMSLayer* _layer;
  LayerSet * _layerSet;
  
  mutable std::vector<MutableVector2D>* _texCoordsCache;
  
  TilePetitions* createTilePetitions(const Tile* tile);
  
  std::vector<MutableVector2D> getTextureCoordinates(const TileTessellator* tessellator) const;
  
  void translateAndScaleFallBackTex(Tile* tile, Tile* fallbackTile, TextureMapping* tmap) const;
  
  void registerNewRequest(Tile* tile);
  
  TilePetitions* getRegisteredTilePetitions(Tile* tile) const;
  void removeRegisteredTilePetitions(Tile* tile);
  
  Mesh* getFallBackTexturedMesh(Tile* tile,
                                const TileTessellator* tessellator,
                                Mesh* tessellatorMesh,
                                Mesh* previousMesh);
  
  Mesh* getNewTextureMesh(Tile* tile,
                          const TileTessellator* tessellator,
                          Mesh* mesh,
                          Mesh* previousMesh);
  
public:
  
  TileImagesTileTexturizer(const TileParameters* parameters,
                           Downloader* downloader,
                           LayerSet* layerSet) : 
  _parameters(parameters),
  _layer(NULL),
  _downloader(downloader),
  _texCoordsCache(NULL),
  _layerSet(layerSet){
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
  
  void tileToBeDeleted(Tile* tile);
  
  bool tileMeetsRenderCriteria(Tile* tile);
  
  void justCreatedTopTile(Tile* tile);
  
  bool isReady(const RenderContext *rc);
  
  void initialize(const InitializationContext* ic);


};

#endif
