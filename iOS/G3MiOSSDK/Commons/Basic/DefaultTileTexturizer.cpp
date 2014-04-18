//
//  DefaultTileTexturizer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/16/14.
//
//

#include "DefaultTileTexturizer.hpp"

#include "LayerSet.hpp"
#include "Tile.hpp"
#include "LeveledTexturedMesh.hpp"
#include "TextureIDReference.hpp"
#include "FrameTasksExecutor.hpp"
#include "LayerTilesRenderParameters.hpp"


class DTT_LTMInitializer : public LazyTextureMappingInitializer {
private:
  const Tile* _tile;
  const Tile* _ancestor;

  float _translationU;
  float _translationV;
  float _scaleU;
  float _scaleV;

  const TileTessellator* _tessellator;
#ifdef C_CODE
  const Vector2I _resolution;
#endif
#ifdef JAVA_CODE
  private final Vector2I _resolution;
#endif

public:
  DTT_LTMInitializer(const Vector2I& resolution,
                     const Tile* tile,
                     const Tile* ancestor,
                     const TileTessellator* tessellator) :
  _resolution(resolution),
  _tile(tile),
  _ancestor(ancestor),
  _tessellator(tessellator),
  _translationU(0),
  _translationV(0),
  _scaleU(1),
  _scaleV(1)
  {

  }

  virtual ~DTT_LTMInitializer() {
#ifdef JAVA_CODE
    super.dispose();
#endif

  }

  void initialize() {
    // The default scale and translation are ok when (tile == _ancestor)
    if (_tile != _ancestor) {
      const Sector tileSector = _tile->_sector;

      const Vector2F lowerTextCoordUV = _tessellator->getTextCoord(_ancestor,
                                                                   tileSector._lower);

      const Vector2F upperTextCoordUV = _tessellator->getTextCoord(_ancestor,
                                                                   tileSector._upper);

      _translationU = lowerTextCoordUV._x;
      _translationV = upperTextCoordUV._y;

      _scaleU = upperTextCoordUV._x - lowerTextCoordUV._x;
      _scaleV = lowerTextCoordUV._y - upperTextCoordUV._y;
    }
  }

  const Vector2F getTranslation() const {
    return Vector2F(_translationU, _translationV);
  }

  const Vector2F getScale() const {
    return Vector2F(_scaleU, _scaleV);
  }

  IFloatBuffer* createTextCoords() const {
    return _tessellator->createTextCoords(_resolution, _tile);
  }

};


class DTT_TileTextureBuilder : public RCObject {
private:
  LeveledTexturedMesh* _texturedMesh;

  DefaultTileTexturizer* _texturizer;
  TileRasterizer*        _tileRasterizer;
  Tile*                  _tile;

//  std::vector<Petition*> _petitions;
//  int                    _petitionsCount;
//  int                    _stepsDone;

  TexturesHandler* _texturesHandler;

#ifdef C_CODE
  const Vector2I   _tileTextureResolution;
//  const Vector2I   _tileMeshResolution;
#endif
#ifdef JAVA_CODE
  private final Vector2I _tileTextureResolution;
//  private final Vector2I _tileMeshResolution;
#endif

  IDownloader*     _downloader;

//  const Mesh* _tessellatorMesh;

//  const TileTessellator* _tessellator;

  const bool _logTilesPetitions;

  //  std::vector<TileTextureBuilder_PetitionStatus> _status;
  //  std::vector<long long>                         _requestsIds;


  bool _finalized;
  bool _canceled;
  bool _alreadyStarted;

  long long _texturePriority;



  static const TextureIDReference* getTopLevelTextureIdForTile(Tile* tile) {
    LeveledTexturedMesh* mesh = (LeveledTexturedMesh*) tile->getTexturizedMesh();

    return (mesh == NULL) ? NULL : mesh->getTopLevelTextureId();
  }

  static LeveledTexturedMesh* createMesh(Tile* tile,
                                         const Mesh* tessellatorMesh,
                                         const Vector2I tileMeshResolution,
                                         const TileTessellator* tessellator) {
    std::vector<LazyTextureMapping*>* mappings = new std::vector<LazyTextureMapping*>();

    Tile* ancestor = tile;
    bool fallbackSolved = false;
    while (ancestor != NULL && !fallbackSolved) {
      const bool ownedTexCoords = true;
      const bool transparent    = false;
      LazyTextureMapping* mapping = new LazyTextureMapping(new DTT_LTMInitializer(tileMeshResolution,
                                                                                  tile,
                                                                                  ancestor,
                                                                                  tessellator),
                                                           ownedTexCoords,
                                                           transparent);

      if (ancestor != tile) {
        const TextureIDReference* glTextureId = getTopLevelTextureIdForTile(ancestor);
        if (glTextureId != NULL) {
          TextureIDReference* glTextureIdRetainedCopy = glTextureId->createCopy();

          mapping->setGLTextureId(glTextureIdRetainedCopy);
          fallbackSolved = true;
        }
      }

      mappings->push_back(mapping);

      ancestor = ancestor->getParent();
    }

    return new LeveledTexturedMesh(tessellatorMesh,
                                   false,
                                   mappings);
  }

public:

  DTT_TileTextureBuilder(DefaultTileTexturizer*            texturizer,
                         TileRasterizer*                   tileRasterizer,
                         const G3MRenderContext*           rc,
                         const LayerTilesRenderParameters* layerTilesRenderParameters,
//                         const std::vector<Petition*>&     petitions,
                         IDownloader*                      downloader,
                         Tile*                             tile,
                         const Mesh*                       tessellatorMesh,
                         const TileTessellator*            tessellator,
                         long long                         texturePriority,
                         bool                              logTilesPetitions) :
  _texturizer(texturizer),
  _tileRasterizer(tileRasterizer),
  _texturesHandler(rc->getTexturesHandler()),
  _tileTextureResolution( layerTilesRenderParameters->_tileTextureResolution ),
//  _tileMeshResolution( layerTilesRenderParameters->_tileMeshResolution ),
  _downloader(downloader),
  _tile(tile),
//  _tessellatorMesh(tessellatorMesh),
//  _stepsDone(0),
  _texturedMesh( NULL ),
//  _tessellator(tessellator),
  _finalized(false),
  _canceled(false),
  _alreadyStarted(false),
  _texturePriority(texturePriority),
  _logTilesPetitions(logTilesPetitions)
  {
    _texturedMesh = createMesh(tile,
                               tessellatorMesh,
                               layerTilesRenderParameters->_tileMeshResolution,
                               tessellator);
  }

  LeveledTexturedMesh* getTexturedMesh() {
    return _texturedMesh;
  }

  void cleanTexturedMesh() {
    _texturedMesh = NULL;
  }

  void cleanTile() {
    _tile = NULL;
  }

  void start() {
#warning Diego at work!
    if (_tile != NULL) {
      _tile->setTextureSolved(true);
    }
  }

  void cancel() {
#warning Diego at work!
    _canceled = true;
//    if (!_canceled) {
//      _canceled = true;
//
//      if (!_finalized) {
//        for (int i = 0; i < _requestsIds.size(); i++) {
//          const long long requestId = _requestsIds[i];
//          _downloader->cancelRequest(requestId);
//        }
//      }
//      _requestsIds.clear();
//    }
  }

  bool isCanceled() const {
    return _canceled;
  }

};


class DTT_TileTextureBuilderHolder : public ITexturizerData {
private:
  DTT_TileTextureBuilder* _builder;

public:
  DTT_TileTextureBuilderHolder(DTT_TileTextureBuilder* builder) :
  _builder(builder)
  {
  }

  DTT_TileTextureBuilder* get() const {
    return _builder;
  }

  ~DTT_TileTextureBuilderHolder() {
    if (_builder != NULL) {
      _builder->_release();
    }
  }

#ifndef C_CODE
  void unusedMethod() { }
#endif
};


class DTT_TileTextureBuilderStartTask : public FrameTask {
private:
  DTT_TileTextureBuilder* _builder;

public:
  DTT_TileTextureBuilderStartTask(DTT_TileTextureBuilder* builder) :
  _builder(builder)
  {
    _builder->_retain();
  }

  virtual ~DTT_TileTextureBuilderStartTask() {
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

LeveledTexturedMesh* DefaultTileTexturizer::getMesh(Tile* tile) const {
  DTT_TileTextureBuilderHolder* tileBuilderHolder = (DTT_TileTextureBuilderHolder*) tile->getTexturizerData();
  return (tileBuilderHolder == NULL) ? NULL : tileBuilderHolder->get()->getTexturedMesh();
}


RenderState DefaultTileTexturizer::getRenderState(LayerSet* layerSet) {
  if (layerSet != NULL) {
    return layerSet->getRenderState();
  }
  return RenderState::ready();
}

void DefaultTileTexturizer::initialize(const G3MContext* context,
                                       const TilesRenderParameters* parameters) {
  // do nothing
}

Mesh* DefaultTileTexturizer::texturize(const G3MRenderContext* rc,
                                       const TileTessellator* tessellator,
                                       TileRasterizer* tileRasterizer,
                                       const LayerTilesRenderParameters* layerTilesRenderParameters,
                                       const LayerSet* layerSet,
                                       bool forceFullRender,
                                       long long texturePriority,
                                       Tile* tile,
                                       Mesh* tessellatorMesh,
                                       Mesh* previousMesh,
                                       bool logTilesPetitions) {
  DTT_TileTextureBuilderHolder* builderHolder = (DTT_TileTextureBuilderHolder*) tile->getTexturizerData();

  if (builderHolder == NULL) {
    builderHolder = new DTT_TileTextureBuilderHolder(new DTT_TileTextureBuilder(this,
                                                                                tileRasterizer,
                                                                                rc,
                                                                                layerTilesRenderParameters,
//                                                                                layerSet->createTileMapPetitions(rc,
//                                                                                                                 layerTilesRenderParameters,
//                                                                                                                 tile),
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

  DTT_TileTextureBuilder* builder = builderHolder->get();
  Mesh* texturizedMesh = builder->getTexturedMesh();
  if (forceFullRender) {
    builder->start();
  }
  else {
    rc->getFrameTasksExecutor()->addPreRenderTask(new DTT_TileTextureBuilderStartTask(builder));
  }

  tile->setTexturizerDirty(false);
  return texturizedMesh;
}

void DefaultTileTexturizer::tileToBeDeleted(Tile* tile,
                                            Mesh* mesh) {
  DTT_TileTextureBuilderHolder* builderHolder = (DTT_TileTextureBuilderHolder*) tile->getTexturizerData();
  if (builderHolder != NULL) {
    DTT_TileTextureBuilder* builder = builderHolder->get();
    builder->cancel();
    builder->cleanTile();
    builder->cleanTexturedMesh();
  }
}

void DefaultTileTexturizer::tileMeshToBeDeleted(Tile* tile,
                                                Mesh* mesh) {
  DTT_TileTextureBuilderHolder* builderHolder = (DTT_TileTextureBuilderHolder*) tile->getTexturizerData();
  if (builderHolder != NULL) {
    DTT_TileTextureBuilder* builder = builderHolder->get();
    builder->cancel();
    builder->cleanTexturedMesh();
  }
}

bool DefaultTileTexturizer::tileMeetsRenderCriteria(Tile* tile) {
  return false;
}

void DefaultTileTexturizer::justCreatedTopTile(const G3MRenderContext* rc,
                                               Tile* tile,
                                               LayerSet* layerSet) {
  // do nothing
}

void DefaultTileTexturizer::ancestorTexturedSolvedChanged(Tile* tile,
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

  const TextureIDReference* glTextureIdRetainedCopy = glTextureId->createCopy();

  const int level = tile->_level - ancestorTile->_level;
  if (!tileMesh->setGLTextureIdForLevel(level, glTextureIdRetainedCopy)) {
    delete glTextureIdRetainedCopy;
  }
}

bool DefaultTileTexturizer::onTerrainTouchEvent(const G3MEventContext* ec,
                                                const Geodetic3D& position,
                                                const Tile* tile,
                                                LayerSet* layerSet) {
  if (layerSet == NULL) {
    return false;
  }
  
  return layerSet->onTerrainTouchEvent(ec, position, tile);
}
