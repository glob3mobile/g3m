//
//  MultiLayerTileTexturizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 08/08/12.
//
//

#ifndef G3MiOSSDK_MultiLayerTileTexturizer_hpp
#define G3MiOSSDK_MultiLayerTileTexturizer_hpp

#include "TileTexturizer.hpp"
class LayerSet;
class IDownloader;

#include <map>
#include "TileKey.hpp"
class TileTextureBuilder;


class MultiLayerTileTexturizer : public TileTexturizer {
private:
  const LayerSet* const        _layerSet;
  
  IDownloader*                 _downloader;
  const TilesRenderParameters* _parameters;
  
  std::map<TileKey, TileTextureBuilder*> _builders;
  
public:
  MultiLayerTileTexturizer(LayerSet* layerSet) :
  _layerSet(layerSet),
  _downloader(0),
  _parameters(0)
  {
    
  }
  
  virtual ~MultiLayerTileTexturizer() {
  }
  
  bool isReady(const RenderContext *rc);
  
  void initialize(const InitializationContext* ic,
                  const TilesRenderParameters* parameters);
  
  Mesh* texturize(const RenderContext* rc,
                  const TileRenderContext* trc,
                  Tile* tile,
                  Mesh* tessellatorMesh,
                  Mesh* previousMesh);
  
  void tileToBeDeleted(Tile* tile,
                       Mesh* mesh);
  
  bool tileMeetsRenderCriteria(Tile* tile);
  
  void justCreatedTopTile(const RenderContext* rc,
                          Tile* tile);
  
};


#endif
