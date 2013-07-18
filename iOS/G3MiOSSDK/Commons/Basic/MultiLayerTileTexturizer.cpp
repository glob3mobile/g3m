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
#include "LayerTilesRenderParameters.hpp"
#include "RectangleF.hpp"


enum TileTextureBuilder_PetitionStatus {
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
                  IImage* image,
                  bool expired);

  void onError(const URL& url);
  
  void onCanceledDownload(const URL& url,
                          IImage* image,
                          bool expired) {
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
  
  const TileTessellator* _tessellator;
#ifdef C_CODE
  const Vector2I _resolution;
#endif
#ifdef JAVA_CODE
  private final Vector2I _resolution;
#endif
  
  const bool _mercator;
  
public:
  LTMInitializer(const Vector2I& resolution,
                 const Tile* tile,
                 const Tile* ancestor,
                 const TileTessellator* tessellator,
                 bool mercator) :
  _resolution(resolution),
  _tile(tile),
  _ancestor(ancestor),
  _tessellator(tessellator),
  _scale(1,1),
  _translation(0,0),
  _mercator(mercator)
  {
    
  }
  
  virtual ~LTMInitializer() {
    
  }
  
  void initialize() {
    // The default scale and translation are ok when (tile == _ancestor)
    if (_tile != _ancestor) {
      const Sector tileSector = _tile->getSector();
      
      const Vector2D lowerTextCoordUV = _tessellator->getTextCoord(_ancestor,
                                                                   tileSector._lower,
                                                                   _mercator);
      
      const Vector2D upperTextCoordUV = _tessellator->getTextCoord(_ancestor,
                                                                   tileSector._upper,
                                                                   _mercator);
      
      _scale       = MutableVector2D(upperTextCoordUV._x - lowerTextCoordUV._x,
                                     lowerTextCoordUV._y - upperTextCoordUV._y);
      
      _translation = MutableVector2D(lowerTextCoordUV._x,
                                     upperTextCoordUV._y);
    }
  }
  
  const MutableVector2D getScale() const {
    return _scale;
  }
  
  const MutableVector2D getTranslation() const {
    return _translation;
  }
  
  IFloatBuffer* createTextCoords() const {
    return _tessellator->createTextCoords(_resolution, _tile, _mercator);
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
  const std::vector<RectangleF*> _srcRects;
  const std::vector<RectangleF*> _dstRects;
#endif
#ifdef JAVA_CODE
  private final java.util.ArrayList<RectangleF> _srcRects;
  private final java.util.ArrayList<RectangleF> _dstRects;
#endif
  
  const std::string _textureId;
  
public:
  TextureUploader(TileTextureBuilder* builder,
                  std::vector<RectangleF*> srcRects,
                  std::vector<RectangleF*> dstRects,
                  const std::string& textureId) :
  _builder(builder),
  _srcRects(srcRects),
  _dstRects(dstRects),
  _textureId(textureId)
  {
    
  }
  
  void imageCreated(IImage* image);
};


class TileTextureBuilder : public RCObject {
private:
  MultiLayerTileTexturizer* _texturizer;
  Tile*                     _tile;
  
  std::vector<Petition*> _petitions;
  int                    _petitionsCount;
  int                    _stepsDone;
  
  TexturesHandler* _texturesHandler;
  TextureBuilder*  _textureBuilder;

#ifdef C_CODE
  const Vector2I   _tileTextureResolution;
  const Vector2I   _tileMeshResolution;
#endif
#ifdef JAVA_CODE
  private final Vector2I _tileTextureResolution;
  private final Vector2I _tileMeshResolution;
#endif
  const bool       _mercator;
  
  IDownloader*     _downloader;
  
  const Mesh* _tessellatorMesh;
  
  const TileTessellator* _tessellator;
  
  const int    _firstLevel;
  
  std::vector<TileTextureBuilder_PetitionStatus> _status;
  std::vector<long long>                         _requestsIds;
  
  
  bool _finalized;
  bool _canceled;
  bool _anyCanceled;
  bool _alreadyStarted;
  
  long long _texturePriority;
  
  
  const std::vector<Petition*> cleanUpPetitions(const std::vector<Petition*>& petitions) const {
    const int petitionsSize = petitions.size();
    if (petitionsSize <= 1) {
      return petitions;
    }
    
    std::vector<Petition*> result;
    for (int i = 0; i < petitionsSize; i++) {
      Petition* currentPetition = petitions[i];
      const Sector currentSector = currentPetition->getSector();
      
      bool coveredByFollowingPetition = false;
      for (int j = i+1; j < petitionsSize; j++) {
        Petition* followingPetition = petitions[j];
        
        // only opaque petitions can cover
        if (!followingPetition->isTransparent()) {
          if (followingPetition->getSector().fullContains(currentSector)) {
            coveredByFollowingPetition = true;
            break;
          }
        }
      }
      
      if (coveredByFollowingPetition) {
        delete currentPetition;
      }
      else {
        result.push_back(currentPetition);
      }
    }
    
    return result;
  }
  
  
public:
  LeveledTexturedMesh* _mesh;
  
  TileTextureBuilder(MultiLayerTileTexturizer* texturizer,
                     const G3MRenderContext*   rc,
                     const LayerSet*           layerSet,
                     IDownloader*              downloader,
                     Tile*                     tile,
                     const Mesh*               tessellatorMesh,
                     const TileTessellator*    tessellator,
                     long long                 texturePriority) :
  _texturizer(texturizer),
  _texturesHandler(rc->getTexturesHandler()),
  _textureBuilder(rc->getTextureBuilder()),
  _tileTextureResolution( layerSet->getLayerTilesRenderParameters()->_tileTextureResolution ),
  _tileMeshResolution( layerSet->getLayerTilesRenderParameters()->_tileMeshResolution ),
  _mercator( layerSet->getLayerTilesRenderParameters()->_mercator ),
  _firstLevel( layerSet->getLayerTilesRenderParameters()->_firstLevel ),
  _downloader(downloader),
  _tile(tile),
  _tessellatorMesh(tessellatorMesh),
  _stepsDone(0),
  _anyCanceled(false),
  _mesh(NULL),
  _tessellator(tessellator),
  _finalized(false),
  _canceled(false),
  _alreadyStarted(false),
  _texturePriority(texturePriority)
  {
    _petitions = cleanUpPetitions(layerSet->createTileMapPetitions(rc, tile));
    
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
            
      const long long priority = _texturePriority + _tile->getLevel();
      
      //      printf("%s\n", petition->getURL().getPath().c_str());
      
      const long long requestId = _downloader->requestImage(URL(petition->getURL()),
                                                            priority,
                                                            petition->getTimeToCache(),
                                                            petition->getReadExpired(),
                                                            new BuilderDownloadStepDownloadListener(this, i),
                                                            true);
      if (requestId >= 0) {
        _requestsIds.push_back(requestId);
      }
    }
  }
  
  ~TileTextureBuilder() {
    if (!_finalized && !_canceled) {
      cancel();
    }
    
    deletePetitions();
  }
  
  RectangleF* getInnerRectangle(int wholeSectorWidth, int wholeSectorHeight,
                                const Sector& wholeSector,
                                const Sector& innerSector) const {
    //printf("%s - %s\n", wholeSector.description().c_str(), innerSector.description().c_str());

    const double widthFactor  = innerSector._deltaLongitude.div(wholeSector._deltaLongitude);
    const double heightFactor = innerSector._deltaLatitude.div(wholeSector._deltaLatitude);

    const Vector2D lowerUV = wholeSector.getUVCoordinates(innerSector.getNW());

    return new RectangleF((float) (lowerUV._x   * wholeSectorWidth),
                          (float) (lowerUV._y   * wholeSectorHeight),
                          (float) (widthFactor  * wholeSectorWidth),
                          (float) (heightFactor * wholeSectorHeight));
  }
  
  void composeAndUploadTexture() {
#ifdef JAVA_CODE
    synchronized (this) {
#endif
      
      if (_mesh == NULL) {
        return;
      }

      std::vector<const IImage*>     images;
      std::vector<RectangleF*> sourceRects;
      std::vector<RectangleF*> destRects;
      std::string textureId = _tile->getKey().tinyDescription();
      
      const Sector tileSector = _tile->getSector();
      
      for (int i = 0; i < _petitionsCount; i++) {
        const Petition* petition = _petitions[i];
        IImage* image = petition->getImage();

        if (image != NULL) {
          const Sector imageSector = petition->getSector();
          //Finding intersection image sector - tile sector = srcReq
          const Sector intersectionSector = tileSector.intersection(imageSector);

          RectangleF* sourceRect = NULL;
          if (!intersectionSector.isEqualsTo(imageSector)){
            sourceRect = getInnerRectangle(image->getWidth(), image->getHeight(),
                                           imageSector,
                                           intersectionSector);
          }
          else {
            sourceRect = new RectangleF(0, 0,
                                        image->getWidth(), image->getHeight());
          }

          //Part of the image we are going to draw
          sourceRects.push_back(sourceRect);

          images.push_back(image);

          //Where we are going to draw the image
          destRects.push_back(getInnerRectangle(_tileTextureResolution._x,
                                                _tileTextureResolution._y,
                                                tileSector,
                                                intersectionSector));
          textureId += petition->getURL().getPath();
          textureId += "_";
        }
      }
      
      if (images.size() > 0) {
        _textureBuilder->createTextureFromImages(_tileTextureResolution,
                                                 images,
                                                 sourceRects,
                                                 destRects,
                                                 new TextureUploader(this,
                                                                     sourceRects,
                                                                     destRects,
                                                                     textureId),
                                                 true);
      }
      
#ifdef JAVA_CODE
    }
#endif
  }
  
  void imageCreated(IImage* image,
                    std::vector<RectangleF*> srcRects,
                    std::vector<RectangleF*> dstRects,
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

      for (int i = 0; i < srcRects.size(); i++) {
        delete srcRects[i];
      }
      
      for (int i = 0; i < dstRects.size(); i++) {
        delete dstRects[i];
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
        mapping = new LazyTextureMapping(new LTMInitializer(_tileMeshResolution,
                                                            _tile,
                                                            ancestor,
                                                            _tessellator,
                                                            _mercator),
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
      if (mappings->size() != (_tile->getLevel() - _firstLevel + 1) ) {
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
                         _srcRects,
                         _dstRects,
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
                                                     IImage* image,
                                                     bool expired) {
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
_texturesHandler(NULL)
{
  
}

MultiLayerTileTexturizer::~MultiLayerTileTexturizer() {
  
}

void MultiLayerTileTexturizer::initialize(const G3MContext* context,
                                          const TilesRenderParameters* parameters) {
  //  _layerSet->initialize(ic);
}


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
                                                                        rc->getDownloader(),
                                                                        tile,
                                                                        tessellatorMesh,
                                                                        trc->getTessellator(),
                                                                        trc->getTexturePriority()
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
  
  const int level = tile->getLevel() - ancestorTile->getLevel();
  _texturesHandler->retainGLTextureId(glTextureId);
  if (!tileMesh->setGLTextureIdForLevel(level, glTextureId)) {
    _texturesHandler->releaseGLTextureId(glTextureId);
  }
}

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

bool MultiLayerTileTexturizer::onTerrainTouchEvent(const G3MEventContext* ec,
                                                   const Geodetic3D& position,
                                                   const Tile* tile,
                                                   LayerSet* layerSet) {
  if (layerSet == NULL) {
    return false;
  }
  
  return layerSet->onTerrainTouchEvent(ec, position, tile);
}
