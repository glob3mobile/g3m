//
//  MultiLayerTileTexturizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 08/08/12.
//
//

#ifndef G3MiOSSDK_MultiLayerTileTexturizer
#define G3MiOSSDK_MultiLayerTileTexturizer

#include "TileTexturizer.hpp"
#include "TileKey.hpp"
#include "Geodetic3D.hpp"

class IGLTextureId;
class TileTextureBuilder;
class LayerSet;
class IDownloader;
class LeveledTexturedMesh;
class IFloatBuffer;
class TileRasterizer;
class TextureIDReference;
class G3MEventContext;

class MultiLayerTileTexturizer : public TileTexturizer {
private:
  inline LeveledTexturedMesh* getMesh(Tile* tile) const;

public:
  MultiLayerTileTexturizer() ;
  
  virtual ~MultiLayerTileTexturizer();
  
  RenderState getRenderState(LayerSet* layerSet);
  
  void initialize(const G3MContext* context,
                  const TilesRenderParameters* parameters);
  
  Mesh* texturize(const G3MRenderContext* rc,
                  const TileTessellator* tessellator,
                  TileRasterizer* tileRasterizer,
                  const LayerTilesRenderParameters* layerTilesRenderParameters,
                  const LayerSet* layerSet,
                  bool isForcedFullRender,
                  long long texturePriority,
                  Tile* tile,
                  Mesh* tessellatorMesh,
                  Mesh* previousMesh,
                  bool logTilesPetitions);

  void tileToBeDeleted(Tile* tile,
                       Mesh* mesh);
  
  bool tileMeetsRenderCriteria(Tile* tile);
  
  void justCreatedTopTile(const G3MRenderContext* rc,
                          Tile* tile,
                          LayerSet* layerSet);
  
  void ancestorTexturedSolvedChanged(Tile* tile,
                                     Tile* ancestorTile,
                                     bool textureSolved);
  
  const TextureIDReference* getTopLevelTextureIdForTile(Tile* tile);
  
  bool onTerrainTouchEvent(const G3MEventContext* ec,
                           const Geodetic3D& g3d,
                           const Tile* tile,
                           LayerSet* layerSet);
  
  void tileMeshToBeDeleted(Tile* tile,
                           Mesh* mesh);
  
};


#endif
