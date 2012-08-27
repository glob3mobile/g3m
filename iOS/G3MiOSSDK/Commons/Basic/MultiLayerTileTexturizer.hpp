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
#include "TileKey.hpp"
#include "GLTextureId.hpp"
#include "Geodetic3D.hpp"

class TileTextureBuilder;
class LayerSet;
class IDownloader;

class MultiLayerTileTexturizer : public TileTexturizer {
private:
#ifdef C_CODE
  const LayerSet* const        _layerSet;
  const TilesRenderParameters* _parameters;
#else
  const LayerSet*              _layerSet;
  TilesRenderParameters* _parameters;
#endif

  mutable float* _texCoordsCache;
  
#ifdef C_CODE
  float* getTextureCoordinates(const TileRenderContext* trc) const;
#else
  float[] getTextureCoordinates(const TileRenderContext* trc) const;
#endif
  
  long _pendingTopTileRequests;
  
  TexturesHandler* _texturesHandler;
  
public:
  MultiLayerTileTexturizer(LayerSet* layerSet) :
  _layerSet(layerSet),
  _parameters(NULL),
  _texCoordsCache(NULL),
  _pendingTopTileRequests(0),
  _texturesHandler(NULL)
  {
    
  }
  
  void countTopTileRequest() {
    _pendingTopTileRequests--;
  }
  
  virtual ~MultiLayerTileTexturizer() {
    if (_texCoordsCache != NULL) {
      delete [] _texCoordsCache;
      _texCoordsCache = NULL;
    }
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
  
  void ancestorTexturedSolvedChanged(Tile* tile,
                                     Tile* ancestorTile,
                                     bool textureSolved);
  
  const GLTextureId getTopLevelGLTextureIdForTile(Tile* tile);
  
  void onTerrainTouchEvent(const EventContext* ec,
                           const Geodetic3D& g3d,
                           const Tile* tile);
  
  void tileMeshToBeDeleted(Tile* tile,
                           Mesh* mesh);
  
};


#endif
