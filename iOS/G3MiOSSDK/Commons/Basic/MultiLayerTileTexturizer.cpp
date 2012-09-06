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
#include "Rectangle.hpp"
#include "TexturesHandler.hpp"

#include "TileRenderer.hpp"
#include "TileTessellator.hpp"
#include "Geodetic3D.hpp"

#include "RCObject.hpp"
#include "ITimer.hpp"

#include "FrameTasksExecutor.hpp"
#include "IDownloadListener.hpp"
#include "IDownloader.hpp"
#include "Petition.hpp"

enum PetitionStatus {
  STATUS_PENDING,
  STATUS_DOWNLOADED,
  STATUS_CANCELED
};


class BuilderDownloadStepDownloadListener : public IDownloadListener {
private:
  TileTextureBuilder* _builder;
  const int           _position;
  
//  int _onDownload;
//  int _onError;
//  int _onCancel;
  
public:
  BuilderDownloadStepDownloadListener(TileTextureBuilder* builder,
                                      int position);
  
  void onDownload(const Response* response);
  
  void onError(const Response* response);
  
  void onCanceledDownload(const Response* response) {
  }
  
  void onCancel(const URL* url);
  
//  void showInvalidState() const {
//    printf("onDownload=%d, onCancel=%d, onError=%d\n", _onDownload, _onCancel, _onError);
//  }
  
//  void testState() const {
//    if ((_onDownload == 1) && (_onCancel == 0) && (_onError == 0)) {
//      return;
//    }
//    if ((_onDownload == 0) && (_onCancel == 1) && (_onError == 0)) {
//      return;
//    }
//    if ((_onDownload == 0) && (_onCancel == 0) && (_onError == 1)) {
//      return;
//    }
//    showInvalidState();
//  }
  
  virtual ~BuilderDownloadStepDownloadListener();
  
};


class LTMInitializer : public LazyTextureMappingInitializer {
private:
  const Tile* _tile;
  const Tile* _ancestor;
  
  MutableVector2D _scale;
  MutableVector2D _translation;
  
  IFloatBuffer* _texCoords;
  
public:
  LTMInitializer(const Tile* tile,
                 const Tile* ancestor,
                 IFloatBuffer* texCoords) :
  _tile(tile),
  _ancestor(ancestor),
  _texCoords(texCoords),
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
    return _texCoords;
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
  
  bool isTexturizerData() const{
    return true;
  }
};


class TileTextureBuilder : public RCObject {
private:
  MultiLayerTileTexturizer* _texturizer;
  Tile*                     _tile;

//  const TileKey             _tileKey;

  std::vector<Petition*>    _petitions;
  int                       _petitionsCount;
  int                       _stepsDone;
  
  const IFactory*  _factory;
  TexturesHandler* _texturesHandler;
  
  const TilesRenderParameters* _parameters;
  IDownloader*                 _downloader;
  
  const Mesh* _tessellatorMesh;
  
  IFloatBuffer* _texCoords;
  
  std::vector<PetitionStatus>    _status;
  std::vector<long long>              _requestsIds;
  
  
  bool _finalized;
  bool _canceled;
  bool _anyCanceled;
  bool _alreadyStarted;
  
public:
  LeveledTexturedMesh* _mesh;
  
  TileTextureBuilder(MultiLayerTileTexturizer*    texturizer,
                     const RenderContext*         rc,
                     const LayerSet*        layerSet,
                     const TilesRenderParameters* parameters,
                     IDownloader*                 downloader,
                     Tile* tile,
                     const Mesh* tessellatorMesh,
                     IFloatBuffer* texCoords) :
  _texturizer(texturizer),
  _factory(rc->getFactory()),
  _texturesHandler(rc->getTexturesHandler()),
  _parameters(parameters),
  _downloader(downloader),
  _tile(tile),
  //_tileKey(tile->getKey()),
  _tessellatorMesh(tessellatorMesh),
  _stepsDone(0),
  _anyCanceled(false),
  _mesh(NULL),
  _texCoords(texCoords),
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
      
      //const long priority = _tile->getLevel() * 1000000 + _tile->getRow() * 1000 + _tile->getColumn();
      const long long priority = _tile->getLevel();
      
      const long long requestId = _downloader->request(URL(petition->getURL()),
                                                  priority,
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
  
  Rectangle* getImageRectangleInTexture(const Sector& wholeSector,
                                        const Sector& imageSector,
                                        int textureWidth,
                                        int textureHeight) const {
    const Vector2D lowerFactor = wholeSector.getUVCoordinates(imageSector.lower());
    
    const double widthFactor  = imageSector.getDeltaLongitude().div(wholeSector.getDeltaLongitude());
    const double heightFactor = imageSector.getDeltaLatitude().div(wholeSector.getDeltaLatitude());
    
    return new Rectangle(lowerFactor.x()         * textureWidth,
                         (1.0 - lowerFactor.y()) * textureHeight,
                         widthFactor  * textureWidth,
                         heightFactor * textureHeight);
  }
  
  void finalize() {
    if (_finalized) {
      return;
    }
    
    _finalized = true;
    
    if (!_canceled && (_tile != NULL) && (_mesh != NULL)) {
      std::vector<const IImage*>    images;
      std::vector<const Rectangle*> rectangles;
      std::string petitionsID = _tile->getKey().tinyDescription();
      
      const int textureWidth  = _parameters->_tileTextureWidth;
      const int textureHeight = _parameters->_tileTextureHeight;
      
      const Sector tileSector = _tile->getSector();
      
      for (int i = 0; i < _petitionsCount; i++) {
        Petition* petition       = _petitions[i];
        const ByteBuffer* buffer = petition->getByteBuffer();
        
        if (buffer != NULL) {
          const IImage* image = _factory->createImageFromData(buffer);
          if (image != NULL) {
            images.push_back(image);
            
            const Sector petitionSector = petition->getSector();
            
            Rectangle* rectangle = getImageRectangleInTexture(tileSector,
                                                              petitionSector,
                                                              textureWidth,
                                                              textureHeight);
            rectangles.push_back(rectangle);
            
            petitionsID += petition->getURL().getPath();
            petitionsID += "_";
          }
        }
      }
      
      if (images.size() > 0) {
//        int __TESTING_mipmapping;
        const bool isMipmap = false;
        const GLTextureId glTextureId = _texturesHandler->getGLTextureId(images,
                                                                         rectangles,
                                                                         TextureSpec(petitionsID,
                                                                                     textureWidth,
                                                                                     textureHeight,
                                                                                     isMipmap));
        if (glTextureId.isValid()) {
          if (!_mesh->setGLTextureIdForLevel(0, glTextureId)) {
            _texturesHandler->releaseGLTextureId(glTextureId);
          }
        }
        
      }
      
      for (int i = 0; i < images.size(); i++) {
        _factory->deleteImage(images[i]);
      }
      
#ifdef C_CODE
      for (int i = 0; i < rectangles.size(); i++) {
        delete rectangles[i];
      }
#endif
    }
    
    _tile->setTextureSolved(true);
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
        printf("Completed with cancelation\n");
      }
      
      finalize();
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
  
  void checkIsPending(int position) const {
    if (_status[position] != STATUS_PENDING) {
      printf("Logic error: Expected STATUS_PENDING at position #%d but found status: %d\n",
             position,
             _status[position]);
    }
  }
  
  void stepDownloaded(int position,
                      const ByteBuffer* buffer) {
    if (_canceled) {
      return;
    }
    checkIsPending(position);
    
    _status[position]  = STATUS_DOWNLOADED;
    _petitions[position]->setByteBuffer(buffer->copy());
    
    stepDone();
  }
  
  void stepCanceled(int position) {
    if (_canceled) {
      return;
    }
    checkIsPending(position);
    
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
        mapping = new LazyTextureMapping(new LTMInitializer(_tile,
                                                            ancestor,
                                                            _texCoords),
                                         _texturesHandler,
                                         false);
      }
      
      if (ancestor != _tile) {
        if (!fallbackSolved) {
          const GLTextureId glTextureId = _texturizer->getTopLevelGLTextureIdForTile(ancestor);
          if (glTextureId.isValid()) {
            _texturesHandler->retainGLTextureId(glTextureId);
            mapping->setGLTextureId(glTextureId);
            fallbackSolved = true;
          }
        }
      }
      else {
        if ( mapping->getGLTextureId().isValid() ) {
          printf("break (point) on me 3\n");
        }
      }
      
      mappings->push_back(mapping);
      ancestor = ancestor->getParent();
    }
    
    if (mappings->size() != _tile->getLevel() + 1) {
      printf("pleae break (point) me\n");
    }
    
    return new LeveledTexturedMesh(_tessellatorMesh,
                                   false,
                                   mappings);
  }
  
  LeveledTexturedMesh* getMesh() {
    return _mesh;
  }
  
  void cleanMesh() {
    if (_mesh != NULL) {
      _mesh = NULL;
    }
  }
  
  void cleanTile() {
    if (_tile != NULL) {
      _tile = NULL;
    }
  }
  
};



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


void BuilderDownloadStepDownloadListener::onDownload(const Response* response) {
//  _onDownload++;
  _builder->stepDownloaded(_position, response->getByteBuffer());
}

void BuilderDownloadStepDownloadListener::onError(const Response* response) {
//  _onError++;
  _builder->stepCanceled(_position);
}

void BuilderDownloadStepDownloadListener::onCancel(const URL* url) {
//  _onCancel++;
  _builder->stepCanceled(_position);
}


MultiLayerTileTexturizer::~MultiLayerTileTexturizer() {
  if (_texCoordsCache != NULL) {
    delete _texCoordsCache;
    _texCoordsCache = NULL;
  }
}


void MultiLayerTileTexturizer::initialize(const InitializationContext* ic,
                                          const TilesRenderParameters* parameters) {
  _parameters = parameters;
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
  
  void execute(const RenderContext* rc) {
    _builder->start();
  }
  
  bool isCanceled(const RenderContext *rc){
    return false;
  }
};

Mesh* MultiLayerTileTexturizer::texturize(const RenderContext* rc,
                                          const TileRenderContext* trc,
                                          Tile* tile,
                                          Mesh* tessellatorMesh,
                                          Mesh* previousMesh) {
  _texturesHandler = rc->getTexturesHandler();
  
  
  TileTextureBuilderHolder* builderHolder = (TileTextureBuilderHolder*) tile->getTexturizerData();
  
  if (builderHolder == NULL) {
    builderHolder = new TileTextureBuilderHolder(new TileTextureBuilder(this,
                                                                        rc,
                                                                        _layerSet,
                                                                        _parameters,
                                                                        rc->getDownloader(),
                                                                        tile,
                                                                        tessellatorMesh,
                                                                        getTextureCoordinates(trc)));
    tile->setTexturizerData(builderHolder);
  }
  
  if (trc->isForcedFullRender()) {
    builderHolder->get()->start();
  }
  else {
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
      
      void execute(const RenderContext* rc) {
        _builder->start();
      }
      
      bool isCanceled(const RenderContext *rc) {
        return _builder->isCanceled();
      }
    };
    rc->getFrameTasksExecutor()->addPreRenderTask(new BuilderStartTask(builderHolder->get()));
  }
  
  tile->setTexturizerDirty(false);
  return builderHolder->get()->getMesh();
}

void MultiLayerTileTexturizer::tileToBeDeleted(Tile* tile,
                                               Mesh* mesh) {
  
  TileTextureBuilderHolder* builderHolder = (TileTextureBuilderHolder*) tile->getTexturizerData();
  
  if (builderHolder != NULL) {
    builderHolder->get()->cancel();
    builderHolder->get()->cleanTile();
    builderHolder->get()->cleanMesh();
  }
  else {
    if (mesh != NULL) {
      printf("break (point) on me 4\n");
    }
  }
}

void MultiLayerTileTexturizer::tileMeshToBeDeleted(Tile* tile,
                                                   Mesh* mesh) {
  TileTextureBuilderHolder* builderHolder = (TileTextureBuilderHolder*) tile->getTexturizerData();
  if (builderHolder != NULL) {
    builderHolder->get()->cancel();
    builderHolder->get()->cleanMesh();
  }
  else {
    if (mesh != NULL) {
      printf("break (point) on me 5\n");
    }
  }
}

const GLTextureId MultiLayerTileTexturizer::getTopLevelGLTextureIdForTile(Tile* tile) {
  LeveledTexturedMesh* mesh = (LeveledTexturedMesh*) tile->getTexturizedMesh();
  
  return (mesh == NULL) ? GLTextureId::invalid() : mesh->getTopLevelGLTextureId();
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
  
  const GLTextureId glTextureId = ancestorMesh->getTopLevelGLTextureId();
  if (!glTextureId.isValid()) {
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

IFloatBuffer* MultiLayerTileTexturizer::getTextureCoordinates(const TileRenderContext* trc) const {
  if (_texCoordsCache == NULL) {
//    std::vector<MutableVector2D>* texCoordsV = trc->getTessellator()->createUnitTextCoords();
//    
//    const int texCoordsSize = texCoordsV->size();
//    float* texCoordsA = new float[2 * texCoordsSize];
//    int p = 0;
//    for (int i = 0; i < texCoordsSize; i++) {
//      texCoordsA[p++] = (float) texCoordsV->at(i).x();
//      texCoordsA[p++] = (float) texCoordsV->at(i).y();
//    }
//    
//    delete texCoordsV;
    
    _texCoordsCache = trc->getTessellator()->createUnitTextCoords();
  }
  return _texCoordsCache;
}

class TopTileDownloadListener : public IDownloadListener {
private:
  MultiLayerTileTexturizer* _texturizer;
  
public:
  TopTileDownloadListener(MultiLayerTileTexturizer* texturizer) :
  _texturizer(texturizer)
  {
  }
  
  void onDownload(const Response* response) {
    _texturizer->countTopTileRequest();
  }
  
  void onError(const Response* response) {
    _texturizer->countTopTileRequest();
  }
  
  void onCanceledDownload(const Response* response) {
  }
  
  void onCancel(const URL* url) {
    _texturizer->countTopTileRequest();
  }
  
};

void MultiLayerTileTexturizer::justCreatedTopTile(const RenderContext* rc,
                                                  Tile* tile) {
  std::vector<Petition*> petitions = _layerSet->createTileMapPetitions(rc,
                                                                       tile,
                                                                       _parameters->_tileTextureWidth,
                                                                       _parameters->_tileTextureHeight);
  
  _pendingTopTileRequests += petitions.size();
  
  const long priority = 1000000000;  // very big priority for toplevel tiles
  for (int i = 0; i < petitions.size(); i++) {
    const Petition* petition = petitions[i];
    rc->getDownloader()->request(URL(petition->getURL()),
                         priority,
                         new TopTileDownloadListener(this),
                         true);
    
    delete petition;
  }
}

bool MultiLayerTileTexturizer::isReady(const RenderContext *rc) {
  return (_pendingTopTileRequests <= 0);
}

void MultiLayerTileTexturizer::onTerrainTouchEvent(const EventContext* ec,
                                                   const Geodetic3D& position,
                                                   const Tile* tile){
  _layerSet->onTerrainTouchEvent(ec, position, tile);
}
