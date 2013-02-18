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
#include "TextureBuilder.hpp"
#include "TileRenderer.hpp"
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

#define TILE_DOWNLOAD_PRIORITY 1000000000

enum PetitionStatus {
  STATUS_PENDING,
  STATUS_DOWNLOADED,
  STATUS_CANCELED
};


class BuilderDownloadStepDownloadListener : public IImageDownloadListener {
private:
  TileTextureBuilder* _builder;
  const int           _position;
public:
  BuilderDownloadStepDownloadListener(TileTextureBuilder* builder,
                                      int position);

  void onDownload(const URL& url,
                  IImage* image);

  void onError(const URL& url);

  void onCanceledDownload(const URL& url,
                          IImage* image) {
  }

  void onCancel(const URL& url);

  virtual ~BuilderDownloadStepDownloadListener();

};


class LTMInitializer : public LazyTextureMappingInitializer {
private:
  const Tile* _tile;
  const Tile* _ancestor;

  MutableVector2D _scale;
  MutableVector2D _translation;

//  IFloatBuffer* _texCoords;
  const TileTessellator* _tessellator;

public:
  LTMInitializer(const Tile* tile,
                 const Tile* ancestor,
                 /*IFloatBuffer* texCoords*/
                 const TileTessellator* tessellator
                 ) :
  _tile(tile),
  _ancestor(ancestor),
//  _texCoords(texCoords),
  _tessellator(tessellator),
  _scale(1,1),
  _translation(0,0)
  {

  }

  virtual ~LTMInitializer() {

  }

  void initialize() {
    // The default scale and translation are ok when (tile == _ancestor)
    if (_tile != _ancestor) {
      const Sector tileSector     = _tile->getSector();
      const Sector ancestorSector = _ancestor->getSector();

      _scale       = tileSector.getScaleFactor(ancestorSector).asMutableVector2D();
      _translation = tileSector.getTranslationFactor(ancestorSector).asMutableVector2D();
    }
  }

  const MutableVector2D getScale() const {
    return _scale;
  }

  const MutableVector2D getTranslation() const {
    return _translation;
  }

  IFloatBuffer* getTexCoords() const {
    //return _texCoords;
    return _tessellator->createUnitTextCoords();
  }

};


class TileTextureBuilderHolder : public ITexturizerData {
private:
  TileTextureBuilder* _builder;

public:
  TileTextureBuilderHolder(TileTextureBuilder* builder) :
  _builder(builder)
  {

  }

  TileTextureBuilder* get() const {
    return _builder;
  }

  virtual ~TileTextureBuilderHolder();

};


class TextureUploader : public IImageListener {
private:
  TileTextureBuilder* _builder;

#ifdef C_CODE
  const std::vector<RectangleI*> _rectangles;
#endif
#ifdef JAVA_CODE
  private final java.util.ArrayList<RectangleI> _rectangles;
#endif

  const std::string _textureId;

public:
  TextureUploader(TileTextureBuilder* builder,
                  std::vector<RectangleI*> rectangles,
                  const std::string& textureId) :
  _builder(builder),
  _rectangles(rectangles),
  _textureId(textureId)
  {

  }

  void imageCreated(IImage* image);
};


class TileTextureBuilder : public RCObject {
private:
  MultiLayerTileTexturizer* _texturizer;
  Tile*                     _tile;

  std::vector<Petition*>    _petitions;
  int                       _petitionsCount;
  int                       _stepsDone;

  const IFactory*  _factory;
  TexturesHandler* _texturesHandler;
  TextureBuilder*  _textureBuilder;
  GL*              _gl;

  const TilesRenderParameters* _parameters;
  IDownloader*                 _downloader;

  const Mesh* _tessellatorMesh;

  const TileTessellator* _tessellator;

  std::vector<PetitionStatus>    _status;
  std::vector<long long>         _requestsIds;


  bool _finalized;
  bool _canceled;
  bool _anyCanceled;
  bool _alreadyStarted;

public:
  LeveledTexturedMesh* _mesh;

  TileTextureBuilder(MultiLayerTileTexturizer*    texturizer,
                     const G3MRenderContext*      rc,
                     const LayerSet*              layerSet,
                     const TilesRenderParameters* parameters,
                     IDownloader*                 downloader,
                     Tile*                        tile,
                     const Mesh*                  tessellatorMesh,
                     const TileTessellator*       tessellator) :
  _texturizer(texturizer),
  _factory(rc->getFactory()),
  _texturesHandler(rc->getTexturesHandler()),
  _textureBuilder(rc->getTextureBuilder()),
  _gl(rc->getGL()),
  _parameters(parameters),
  _downloader(downloader),
  _tile(tile),
  _tessellatorMesh(tessellatorMesh),
  _stepsDone(0),
  _anyCanceled(false),
  _mesh(NULL),
  _tessellator(tessellator),
  _finalized(false),
  _canceled(false),
  _alreadyStarted(false)
  {
    _petitions = layerSet->createTileMapPetitions(rc,
                                                  tile,
                                                  parameters->_tileTextureWidth,
                                                  parameters->_tileTextureHeight);

    _petitionsCount = _petitions.size();

    for (int i = 0; i < _petitionsCount; i++) {
      _status.push_back(STATUS_PENDING);
    }

    _mesh = createMesh();
  }

  void start() {
    if (_canceled) {
      return;
    }
    if (_alreadyStarted) {
      return;
    }
    _alreadyStarted = true;

    if (_tile == NULL) {
      return;
    }

    for (int i = 0; i < _petitionsCount; i++) {
      const Petition* petition = _petitions[i];

      //      const long long priority =  (_parameters->_incrementalTileQuality
      //                                   ? 1000 - _tile->getLevel()
      //                                   : _tile->getLevel());
      const long long priority = TILE_DOWNLOAD_PRIORITY + _tile->getLevel();

//      printf("%s\n", petition->getURL().getPath().c_str());

      const long long requestId = _downloader->requestImage(URL(petition->getURL()),
                                                            priority,
                                                            petition->getTimeToCache(),
                                                            new BuilderDownloadStepDownloadListener(this, i),
                                                            true);

      _requestsIds.push_back(requestId);
    }
  }

  ~TileTextureBuilder() {
    if (!_finalized && !_canceled) {
      cancel();
    }

    deletePetitions();
  }

  RectangleI* getImageRectangleInTexture(const Sector& wholeSector,
                                         const Sector& imageSector,
                                         int textureWidth,
                                         int textureHeight) const {
    const Vector2D lowerFactor = wholeSector.getUVCoordinates(imageSector.lower());

    const double widthFactor  = imageSector.getDeltaLongitude().div(wholeSector.getDeltaLongitude());
    const double heightFactor = imageSector.getDeltaLatitude().div(wholeSector.getDeltaLatitude());

    return new RectangleI((int) IMathUtils::instance()->round( lowerFactor._x         * textureWidth ),
                          (int) IMathUtils::instance()->round( (1.0 - lowerFactor._y) * textureHeight ),
                          (int) IMathUtils::instance()->round( widthFactor            * textureWidth ),
                          (int) IMathUtils::instance()->round( heightFactor           * textureHeight ));
  }

  void composeAndUploadTexture() {
#ifdef JAVA_CODE
    synchronized (this) {
#endif

      if (_mesh == NULL) {
        return;
      }

      std::vector<IImage*>     images;
      std::vector<RectangleI*> rectangles;
      std::string textureId = _tile->getKey().tinyDescription();

      const int textureWidth  = _parameters->_tileTextureWidth;
      const int textureHeight = _parameters->_tileTextureHeight;

      const Sector tileSector = _tile->getSector();

      for (int i = 0; i < _petitionsCount; i++) {
        const Petition* petition = _petitions[i];
        IImage* image = petition->getImage();

        if (image != NULL) {
          images.push_back(image);

          rectangles.push_back(getImageRectangleInTexture(tileSector,
                                                          petition->getSector(),
                                                          textureWidth,
                                                          textureHeight));

          textureId += petition->getURL().getPath();
          textureId += "_";
        }
      }

      if (images.size() > 0) {
        _textureBuilder->createTextureFromImages(_gl,
                                                 _factory,
                                                 images,
                                                 rectangles,
                                                 textureWidth,
                                                 textureHeight,
                                                 new TextureUploader(this, rectangles, textureId),
                                                 true);
      }

#ifdef JAVA_CODE
    }
#endif
  }

  void imageCreated(IImage* image,
                    std::vector<RectangleI*> rectangles,
                    const std::string& textureId) {
#ifdef JAVA_CODE
    synchronized (this) {
#endif

      if (_mesh == NULL) {
        IFactory::instance()->deleteImage(image);
        return;
      }

      const bool isMipmap = false;

      const IGLTextureId* glTextureId = _texturesHandler->getGLTextureId(image,
                                                                         GLFormat::rgba(),
                                                                         textureId,
                                                                         isMipmap);

      if (glTextureId != NULL) {
        if (!_mesh->setGLTextureIdForLevel(0, glTextureId)) {
          _texturesHandler->releaseGLTextureId(glTextureId);
        }
      }

      IFactory::instance()->deleteImage(image);

      for (int i = 0; i < rectangles.size(); i++) {
        delete rectangles[i];
      }

#ifdef JAVA_CODE
    }
#endif
  }

  void done() {
    if (!_finalized) {
      _finalized = true;

      if (!_canceled && (_tile != NULL) && (_mesh != NULL)) {
        composeAndUploadTexture();
      }

      if (_tile != NULL) {
        _tile->setTextureSolved(true);
      }
    }
  }

  void deletePetitions() {
    for (int i = 0; i < _petitionsCount; i++) {
      Petition* petition = _petitions[i];
      delete petition;
    }
    _petitions.clear();
    _petitionsCount = 0;
  }

  void stepDone() {
    _stepsDone++;

    if (_stepsDone == _petitionsCount) {
      if (_anyCanceled) {
        ILogger::instance()->logInfo("Completed with cancelation\n");
      }

      done();
    }
  }

  void cancel() {
    if (_canceled) {
      return;
    }

    _canceled = true;

    if (!_finalized) {
      for (int i = 0; i < _requestsIds.size(); i++) {
        const long long requestId = _requestsIds[i];
        _downloader->cancelRequest(requestId);
      }
    }
    _requestsIds.clear();
  }

  bool isCanceled() const {
    return _canceled;
  }

//  void checkIsPending(int position) const {
//    if (_status[position] != STATUS_PENDING) {
//      ILogger::instance()->logError("Logic error: Expected STATUS_PENDING at position #%d but found status: %d\n",
//                                    position,
//                                    _status[position]);
//    }
//  }

  void stepDownloaded(int position,
                      IImage* image) {
    if (_canceled) {
      IFactory::instance()->deleteImage(image);
      return;
    }
    //checkIsPending(position);

    _status[position]  = STATUS_DOWNLOADED;
    _petitions[position]->setImage( image );

    stepDone();
  }

  void stepCanceled(int position) {
    if (_canceled) {
      return;
    }
    //checkIsPending(position);

    _anyCanceled = true;

    _status[position] = STATUS_CANCELED;

    stepDone();
  }

  LeveledTexturedMesh* createMesh() const {
    std::vector<LazyTextureMapping*>* mappings = new std::vector<LazyTextureMapping*>();

    Tile* ancestor = _tile;
    bool fallbackSolved = false;
    while (ancestor != NULL) {
      LazyTextureMapping* mapping;
      if (fallbackSolved) {
        mapping = NULL;
      }
      else {
        const bool ownedTexCoords = true;
        const bool transparent    = false;
        mapping = new LazyTextureMapping(new LTMInitializer(_tile,
                                                            ancestor,
                                                            _tessellator),
                                         _texturesHandler,
                                         ownedTexCoords,
                                         transparent);
      }

      if (ancestor != _tile) {
        if (!fallbackSolved) {
          const IGLTextureId* glTextureId= _texturizer->getTopLevelGLTextureIdForTile(ancestor);
          if (glTextureId != NULL) {
            _texturesHandler->retainGLTextureId(glTextureId);
            mapping->setGLTextureId(glTextureId);
            fallbackSolved = true;
          }
        }
      }
      else {
        if (mapping != NULL) {
          if ( mapping->getGLTextureId() != NULL ) {
            ILogger::instance()->logInfo("break (point) on me 3\n");
          }
        }
      }

      mappings->push_back(mapping);
      ancestor = ancestor->getParent();
    }

    if ((mappings != NULL) && (_tile != NULL)) {
      if (mappings->size() != _tile->getLevel() + 1) {
        ILogger::instance()->logInfo("pleae break (point) me\n");
      }
    }

    return new LeveledTexturedMesh(_tessellatorMesh,
                                   false,
                                   mappings);
  }

  LeveledTexturedMesh* getMesh() {
    return _mesh;
  }

  void cleanMesh() {
#ifdef JAVA_CODE
    synchronized (this) {
#endif

      if (_mesh != NULL) {
        _mesh = NULL;
      }

#ifdef JAVA_CODE
    }
#endif
  }

  void cleanTile() {
    if (_tile != NULL) {
      _tile = NULL;
    }
  }

};

void TextureUploader::imageCreated(IImage* image) {
  _builder->imageCreated(image,
                         _rectangles,
                         _textureId);
}

BuilderDownloadStepDownloadListener::BuilderDownloadStepDownloadListener(TileTextureBuilder* builder,
                                                                         int position) :
_builder(builder),
_position(position)
//_onDownload(0),
//_onError(0),
//_onCancel(0)
{
  _builder->_retain();
}

BuilderDownloadStepDownloadListener::~BuilderDownloadStepDownloadListener() {
  //  testState();

  if (_builder != NULL) {
    _builder->_release();
  }
}


TileTextureBuilderHolder::~TileTextureBuilderHolder() {
  if (_builder != NULL) {
    _builder->_release();
  }
}


void BuilderDownloadStepDownloadListener::onDownload(const URL& url,
                                                     IImage* image) {
  //  _onDownload++;
  _builder->stepDownloaded(_position, image);
}

void BuilderDownloadStepDownloadListener::onError(const URL& url) {
  //  _onError++;
  _builder->stepCanceled(_position);
}

void BuilderDownloadStepDownloadListener::onCancel(const URL& url) {
  //  _onCancel++;
  _builder->stepCanceled(_position);
}

MultiLayerTileTexturizer::MultiLayerTileTexturizer() :
_parameters(NULL),
//_texCoordsCache(NULL),
//_pendingTopTileRequests(0),
_texturesHandler(NULL)
{

}

MultiLayerTileTexturizer::~MultiLayerTileTexturizer() {
//  delete _texCoordsCache;
//  _texCoordsCache = NULL;
}

void MultiLayerTileTexturizer::initialize(const G3MContext* context,
                                          const TilesRenderParameters* parameters) {
  _parameters = parameters;
  //  _layerSet->initialize(ic);
}

//class BuilderStartTask : public FrameTask {
//private:
//  TileTextureBuilder* _builder;
//
//public:
//  BuilderStartTask(TileTextureBuilder* builder) :
//  _builder(builder)
//  {
//    _builder->_retain();
//  }
//
//  virtual ~BuilderStartTask() {
//    _builder->_release();
//  }
//
//  void execute(const G3MRenderContext* rc) {
//    _builder->start();
//  }
//
//  bool isCanceled(const G3MRenderContext *rc){
//    return false;
//  }
//};

class BuilderStartTask : public FrameTask {
private:
  TileTextureBuilder* _builder;

public:
  BuilderStartTask(TileTextureBuilder* builder) :
  _builder(builder)
  {
    _builder->_retain();
  }

  virtual ~BuilderStartTask() {
    _builder->_release();
  }

  void execute(const G3MRenderContext* rc) {
    _builder->start();
  }

  bool isCanceled(const G3MRenderContext *rc) {
    return _builder->isCanceled();
  }
};

Mesh* MultiLayerTileTexturizer::texturize(const G3MRenderContext* rc,
                                          const TileRenderContext* trc,
                                          Tile* tile,
                                          Mesh* tessellatorMesh,
                                          Mesh* previousMesh) {
  _texturesHandler = rc->getTexturesHandler();


  TileTextureBuilderHolder* builderHolder = (TileTextureBuilderHolder*) tile->getTexturizerData();

  if (builderHolder == NULL) {
    builderHolder = new TileTextureBuilderHolder(new TileTextureBuilder(this,
                                                                        rc,
                                                                        trc->getLayerSet(),
                                                                        _parameters,
                                                                        rc->getDownloader(),
                                                                        tile,
                                                                        tessellatorMesh,
                                                                        trc->getTessellator()
                                                                        )
                                                 );
    tile->setTexturizerData(builderHolder);
  }

  TileTextureBuilder* builder = builderHolder->get();
  if (trc->isForcedFullRender()) {
    builder->start();
  }
  else {
    rc->getFrameTasksExecutor()->addPreRenderTask(new BuilderStartTask(builder));
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
  else {
    if (mesh != NULL) {
      ILogger::instance()->logInfo("break (point) on me 4\n");
    }
  }
}

void MultiLayerTileTexturizer::tileMeshToBeDeleted(Tile* tile,
                                                   Mesh* mesh) {
  TileTextureBuilderHolder* builderHolder = (TileTextureBuilderHolder*) tile->getTexturizerData();
  if (builderHolder != NULL) {
    TileTextureBuilder* builder = builderHolder->get();
    builder->cancel();
    builder->cleanMesh();
  }
  else {
    if (mesh != NULL) {
      ILogger::instance()->logInfo("break (point) on me 5\n");
    }
  }
}

const IGLTextureId* MultiLayerTileTexturizer::getTopLevelGLTextureIdForTile(Tile* tile) {
  LeveledTexturedMesh* mesh = (LeveledTexturedMesh*) tile->getTexturizedMesh();

  return (mesh == NULL) ? NULL : mesh->getTopLevelGLTextureId();
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

  const IGLTextureId* glTextureId = ancestorMesh->getTopLevelGLTextureId();
  if (glTextureId == NULL) {
    return;
  }

  LeveledTexturedMesh* tileMesh = getMesh(tile);
  if (tileMesh == NULL) {
    return;
  }

  const int level = tile->getLevel() - ancestorTile->getLevel() - _parameters->_topLevel;
  _texturesHandler->retainGLTextureId(glTextureId);
  if (!tileMesh->setGLTextureIdForLevel(level, glTextureId)) {
    _texturesHandler->releaseGLTextureId(glTextureId);
  }
}

//IFloatBuffer* MultiLayerTileTexturizer::getTextureCoordinates(const TileRenderContext* trc) const {
////  if (_texCoordsCache == NULL) {
////    _texCoordsCache = trc->getTessellator()->createUnitTextCoords();
////  }
////  return _texCoordsCache;
//  int _____XXXXXXX;
//  return trc->getTessellator()->createUnitTextCoords();
//}

void MultiLayerTileTexturizer::justCreatedTopTile(const G3MRenderContext* rc,
                                                  Tile* tile,
                                                  LayerSet* layerSet) {
}

bool MultiLayerTileTexturizer::isReady(const G3MRenderContext *rc,
                                       LayerSet* layerSet) {
  if (layerSet != NULL) {
    return layerSet->isReady();
  }
  return true;
}

void MultiLayerTileTexturizer::onTerrainTouchEvent(const G3MEventContext* ec,
                                                   const Geodetic3D& position,
                                                   const Tile* tile,
                                                   LayerSet* layerSet){
  if (layerSet != NULL) {
    layerSet->onTerrainTouchEvent(ec, position, tile);
  }
}
