//
//  MultiLayerTileTexturizer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 08/08/12.
//
//

#include "MultiLayerTileTexturizer.hpp"
#include "Context.hpp"
#include "LayerSet.hpp"
#include "TilesRenderParameters.hpp"
#include "Tile.hpp"
#include "LeveledTexturedMesh.hpp"
#include "RectangleI.hpp"
#include "TexturesHandler.hpp"
#include "PlanetRenderer.hpp"
#include "TileTessellator.hpp"
#include "Geodetic3D.hpp"
#include "RCObject.hpp"
#include "ITimer.hpp"
#include "FrameTasksExecutor.hpp"
#include "IImageDownloadListener.hpp"
#include "IDownloader.hpp"
#include "Petition.hpp"
#include "GLConstants.hpp"
#include "IImageListener.hpp"
#include "LayerTilesRenderParameters.hpp"
#include "RectangleF.hpp"
#include "IImageUtils.hpp"
#include "TileRasterizer.hpp"
#include "ITileVisitor.hpp"
#include "TextureIDReference.hpp"
#include "TileTextureBuilder.hpp"


#define TILE_DOWNLOAD_PRIORITY 1000000000


MultiLayerTileTexturizer::MultiLayerTileTexturizer()
{

}

MultiLayerTileTexturizer::~MultiLayerTileTexturizer() {
#ifdef JAVA_CODE
  super.dispose();
#endif

}

void MultiLayerTileTexturizer::initialize(const G3MContext* context,
                                          const TilesRenderParameters* parameters) {
  //  _layerSet->initialize(ic);
}


class TileTextureBuilderStartTask : public FrameTask {
private:
  TileTextureBuilder* _builder;

public:
  TileTextureBuilderStartTask(TileTextureBuilder* builder) :
  _builder(builder)
  {
    _builder->_retain();
  }

  virtual ~TileTextureBuilderStartTask() {
    _builder->_release();
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

  void execute(const G3MRenderContext* rc) {
    _builder->start();
  }

  bool isCanceled(const G3MRenderContext* rc) {
    return _builder->isCanceled();
  }
};


Mesh* MultiLayerTileTexturizer::texturize(const G3MRenderContext* rc,
                                          const TileTessellator* tessellator,
                                          TileRasterizer* tileRasterizer,
                                          const LayerTilesRenderParameters* layerTilesRenderParameters,
                                          const LayerSet* layerSet,
                                          bool isForcedFullRender,
                                          long long texturePriority,
                                          Tile* tile,
                                          Mesh* tessellatorMesh,
                                          Mesh* previousMesh,
                                          bool logTilesPetitions) {
    
  TileTextureBuilderHolder* builderHolder = (TileTextureBuilderHolder*) tile->getTexturizerData();

  if (builderHolder == NULL) {
    builderHolder = new TileTextureBuilderHolder(new TileTextureBuilder(this,
                                                                        tileRasterizer,
                                                                        rc,
                                                                        layerTilesRenderParameters,
                                                                        layerSet->createTileMapPetitions(rc,
                                                                                                         layerTilesRenderParameters,
                                                                                                         tile),
                                                                        rc->getDownloader(),
                                                                        tile,
                                                                        tessellatorMesh,
                                                                        tessellator,
                                                                        texturePriority,
                                                                        logTilesPetitions
                                                                        )
                                                 );
    tile->setTexturizerData(builderHolder);
  }

  TileTextureBuilder* builder = builderHolder->get();
  if (isForcedFullRender) {
    builder->start();
  }
  else {
    rc->getFrameTasksExecutor()->addPreRenderTask(new TileTextureBuilderStartTask(builder));
  }

  tile->setTexturizerDirty(false);
  
  return builder->getMesh();
}

void MultiLayerTileTexturizer::tileToBeDeleted(Tile* tile,
                                               Mesh* mesh) {

  TileTextureBuilderHolder* builderHolder = (TileTextureBuilderHolder*) tile->getTexturizerData();

  if (builderHolder != NULL) {
    TileTextureBuilder* builder = builderHolder->get();
    builder->cancel();
    builder->cleanTile();
    builder->cleanMesh();
  }
//  else {
//    if (mesh != NULL) {
//      ILogger::instance()->logInfo("break (point) on me 4\n");
//    }
//  }
}

void MultiLayerTileTexturizer::tileMeshToBeDeleted(Tile* tile,
                                                   Mesh* mesh) {
  TileTextureBuilderHolder* builderHolder = (TileTextureBuilderHolder*) tile->getTexturizerData();
  if (builderHolder != NULL) {
    TileTextureBuilder* builder = builderHolder->get();
    builder->cancel();
    builder->cleanMesh();
  }
//  else {
//    if (mesh != NULL) {
//      ILogger::instance()->logInfo("break (point) on me 5\n");
//    }
//  }
}

const TextureIDReference* MultiLayerTileTexturizer::getTopLevelTextureIdForTile(Tile* tile) {
  LeveledTexturedMesh* mesh = (LeveledTexturedMesh*) tile->getTexturizedMesh();

  return (mesh == NULL) ? NULL : mesh->getTopLevelTextureId();
}

bool MultiLayerTileTexturizer::tileMeetsRenderCriteria(Tile* tile) {
  return false;
}

LeveledTexturedMesh* MultiLayerTileTexturizer::getMesh(Tile* tile) const {
  TileTextureBuilderHolder* tileBuilderHolder = (TileTextureBuilderHolder*) tile->getTexturizerData();
  return (tileBuilderHolder == NULL) ? NULL : tileBuilderHolder->get()->getMesh();
}

void MultiLayerTileTexturizer::ancestorTexturedSolvedChanged(Tile* tile,
                                                             Tile* ancestorTile,
                                                             bool textureSolved) {
  if (!textureSolved) {
    return;
  }

  if (tile->isTextureSolved()) {
    return;
  }

  LeveledTexturedMesh* ancestorMesh = getMesh(ancestorTile);
  if (ancestorMesh == NULL) {
    return;
  }

  const TextureIDReference* glTextureId = ancestorMesh->getTopLevelTextureId();
  if (glTextureId == NULL) {
    return;
  }

  LeveledTexturedMesh* tileMesh = getMesh(tile);
  if (tileMesh == NULL) {
    return;
  }

//  _texturesHandler->retainGLTextureId(glTextureId);
  const TextureIDReference* glTextureIdRetainedCopy = glTextureId->createCopy();

  const int level = tile->_level - ancestorTile->_level;
  if (!tileMesh->setGLTextureIdForLevel(level, glTextureIdRetainedCopy)) {
//    _texturesHandler->releaseGLTextureId(glTextureId);
    delete glTextureIdRetainedCopy;
  }
}

void MultiLayerTileTexturizer::justCreatedTopTile(const G3MRenderContext* rc,
                                                  Tile* tile,
                                                  LayerSet* layerSet) {
}

RenderState MultiLayerTileTexturizer::getRenderState(LayerSet* layerSet) {
  if (layerSet != NULL) {
    return layerSet->getRenderState();
  }
  return RenderState::ready();
}

bool MultiLayerTileTexturizer::onTerrainTouchEvent(const G3MEventContext* ec,
                                                   const Geodetic3D& position,
                                                   const Tile* tile,
                                                   LayerSet* layerSet) {
  if (layerSet == NULL) {
    return false;
  }
  
  return layerSet->onTerrainTouchEvent(ec, position, tile);
}
