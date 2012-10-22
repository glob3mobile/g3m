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
#include "Geodetic3D.hpp"

class IGLTextureId;
class TileTextureBuilder;
class LayerSet;
class IDownloader;
class LeveledTexturedMesh;
class IFloatBuffer;


class MultiLayerTileTexturizer : public TileTexturizer {
private:
//  LayerSet* _layerSet;
  
#ifdef C_CODE
  const TilesRenderParameters* _parameters;
#else
  TilesRenderParameters* _parameters;
#endif

  mutable IFloatBuffer* _texCoordsCache;
  
  IFloatBuffer* getTextureCoordinates(const TileRenderContext* trc) const;
  
  long _pendingTopTileRequests;
  
  TexturesHandler* _texturesHandler;
  
  inline LeveledTexturedMesh* getMesh(Tile* tile) const;

public:
  MultiLayerTileTexturizer() ;
  
  void countTopTileRequest() {
    _pendingTopTileRequests--;
  }
  
  virtual ~MultiLayerTileTexturizer();
  
  bool isReady(const RenderContext *rc,
               LayerSet* layerSet);
  
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
                          Tile* tile,
                          LayerSet* layerSet);
  
  void ancestorTexturedSolvedChanged(Tile* tile,
                                     Tile* ancestorTile,
                                     bool textureSolved);
  
  const IGLTextureId* getTopLevelGLTextureIdForTile(Tile* tile);
  
  void onTerrainTouchEvent(const EventContext* ec,
                           const Geodetic3D& g3d,
                           const Tile* tile,
                           LayerSet* layerSet);
  
  void tileMeshToBeDeleted(Tile* tile,
                           Mesh* mesh);
  
};


#endif
