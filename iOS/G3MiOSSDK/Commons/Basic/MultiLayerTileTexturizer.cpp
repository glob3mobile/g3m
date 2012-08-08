//
//  MultiLayerTileTexturizer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 08/08/12.
//
//

#include "MultiLayerTileTexturizer.hpp"
//#include "IDownloader.hpp"
#include "Context.hpp"
#include "LayerSet.hpp"
#include "TilesRenderParameters.hpp"

void MultiLayerTileTexturizer::initialize(const InitializationContext* ic,
                                          const TilesRenderParameters* parameters) {
  _downloader = ic->getDownloader();
  _parameters = parameters;
}

void MultiLayerTileTexturizer::justCreatedTopTile(const RenderContext* rc,
                                                  Tile* tile) {
  
}

bool MultiLayerTileTexturizer::isReady(const RenderContext *rc) {
  return true;
}

Mesh* MultiLayerTileTexturizer::texturize(const RenderContext* rc,
                                          const TileRenderContext* trc,
                                          Tile* tile,
                                            Mesh* tessellatorMesh,
                                          Mesh* previousMesh) {
  
  //  TexturedMesh* result = getFinalTexturedMesh(tile, tessellatorMesh);
  
  std::vector<Petition*> petitions = _layerSet->createTilePetitions(rc,
                                                                    tile,
                                                                    _parameters->_tileTextureWidth,
                                                                    _parameters->_tileTextureHeight);
  
  int ___XX;
  
  return 0;
}

void MultiLayerTileTexturizer::tileToBeDeleted(Tile* tile) {
  
}

bool MultiLayerTileTexturizer::tileMeetsRenderCriteria(Tile* tile) {
  return true;
}
