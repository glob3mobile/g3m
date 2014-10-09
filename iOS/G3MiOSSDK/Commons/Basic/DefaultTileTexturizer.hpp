//
//  DefaultTileTexturizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/16/14.
//
//

#ifndef __G3MiOSSDK__DefaultTileTexturizer__
#define __G3MiOSSDK__DefaultTileTexturizer__

#include "TileTexturizer.hpp"
class LeveledTexturedMesh;
class TextureIDReference;


class DefaultTileTexturizer : public TileTexturizer {
private:
  inline LeveledTexturedMesh* getMesh(Tile* tile) const;

public:
  virtual ~DefaultTileTexturizer() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

  RenderState getRenderState(LayerSet* layerSet);

  void initialize(const G3MContext* context,
                  const TilesRenderParameters* parameters);

  Mesh* texturize(const G3MRenderContext* rc,
                  const TileTessellator* tessellator,
//                  TileRasterizer* tileRasterizer,
                  const LayerTilesRenderParameters* layerTilesRenderParameters,
                  const LayerSet* layerSet,
                  bool forceFullRender,
                  long long tileDownloadPriority,
                  Tile* tile,
                  Mesh* tessellatorMesh,
                  Mesh* previousMesh,
                  bool logTilesPetitions);

  void tileToBeDeleted(Tile* tile,
                       Mesh* mesh);

  void tileMeshToBeDeleted(Tile* tile,
                           Mesh* mesh);

  bool tileMeetsRenderCriteria(Tile* tile);

  void justCreatedTopTile(const G3MRenderContext* rc,
                          Tile* tile,
                          LayerSet* layerSet);

  void ancestorTexturedSolvedChanged(Tile* tile,
                                     Tile* ancestorTile,
                                     bool textureSolved);

  bool onTerrainTouchEvent(const G3MEventContext* ec,
                           const Geodetic3D& position,
                           const Tile* tile,
                           LayerSet* layerSet);
  
};

#endif
