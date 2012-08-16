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

enum PetitionStatus {
  STATUS_PENDING,
  STATUS_DOWNLOADED,
  STATUS_CANCELED
};


class BuilderDownloadStepDownloadListener : public IDownloadListener {
private:
  TileTextureBuilder* _builder;
  const int           _position;
  
public:
  BuilderDownloadStepDownloadListener(TileTextureBuilder* builder,
                                      int position) :
  _builder(builder),
  _position(position)
  {
    
  }
  
  void onDownload(const Response* response);
  
  void onError(const Response* response);
  
  void onCanceledDownload(const Response* response) {
  }
  
  void onCancel(const URL* url);
  
};


class LTMInitializer : public LazyTextureMappingInitializer {
private:
  const Tile* _tile;
  const Tile* _ancestor;
  
  MutableVector2D _scale;
  MutableVector2D _translation;
  
  const float* _texCoords;
  
public:
  LTMInitializer(const Tile* tile,
                 const Tile* ancestor,
                 const float* texCoords) :
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
  
  float const* getTexCoords() const {
    return _texCoords;
  }
  
};


class TileTextureBuilder {
private:
  MultiLayerTileTexturizer* _texturizer;
  Tile*                     _tile;
  const TileKey             _tileKey;
  std::vector<Petition*>    _petitions;
  int                       _petitionsCount;
  int                       _stepsDone;
  
  const IFactory*  _factory;
  TexturesHandler* _texturesHandler;
  
  const TilesRenderParameters* _parameters;
  IDownloader*                 _downloader;
  
  const Mesh* _tessellatorMesh;
  
  const float* _texCoords;
  
  
  std::vector<PetitionStatus>    _status;
  std::vector<long>              _requestsIds;
  
  
  bool _finalized;
  bool _canceled;
  bool _anyCanceled;
  
public:
  LeveledTexturedMesh* _mesh;
  
  TileTextureBuilder(MultiLayerTileTexturizer*    texturizer,
                     const RenderContext*         rc,
                     const LayerSet* const        layerSet,
                     const TilesRenderParameters* parameters,
                     IDownloader*                 downloader,
                     Tile* tile,
                     const Mesh* tessellatorMesh,
                     float* texCoords) :
  _texturizer(texturizer),
  _factory(rc->getFactory()),
  _texturesHandler(rc->getTexturesHandler()),
  _parameters(parameters),
  _downloader(downloader),
  _tile(tile),
  _tileKey(tile->getKey()),
  _tessellatorMesh(tessellatorMesh),
  _stepsDone(0),
  _anyCanceled(false),
  _mesh(NULL),
  _texCoords(texCoords),
  _finalized(false),
  _canceled(false)
  {
    _petitions = layerSet->createTilePetitions(rc,
                                               tile,
                                               parameters->_tileTextureWidth,
                                               parameters->_tileTextureHeight);
    
    _petitionsCount = _petitions.size();
    
    for (int i = 0; i < _petitionsCount; i++) {
      _status.push_back(STATUS_PENDING);
    }
    
    //    const Camera* camera = rc->getCurrentCamera();
    //    const Planet* planet = rc->getPlanet();
    //    const Geodetic2D center = tile->getSector().getCenter();
    //
    //    const Vector3D cameraPos = camera->getPosition();
    //    const Vector3D centerVec3 = planet->toVector3D(center);
    //
    //    const double squaredDistance = centerVec3.sub(cameraPos).length();
    
    int __bottleneck_;
    /*
     when all the requests are in the cache, the listener is ejecuted SYNCronously.
     
     this produces a bottleneck combining and uploading the texture
     */
    
    for (int i = 0; i < _petitionsCount; i++) {
      const Petition* petition = _petitions[i];
      
      //      const long priority = tile->getLevel() * 1000000 + tile->getRow() * 1000 + tile->getColumn();
      const long priority = tile->getLevel();
      //      const long priority = (long) (tile->getLevel() * 1000000000000 + -squaredDistance);
      
      int ___remove_print;
      printf("Downloading %s\n", petition->getURL().getPath().c_str());
      
      const long requestId = downloader->request(URL(petition->getURL()),
                                                 priority,
                                                 new BuilderDownloadStepDownloadListener(this, i),
                                                 true);
      
      _requestsIds.push_back(requestId);
    }
    
  }
  
  ~TileTextureBuilder() {
    deletePetitions();
  }
  
  Rectangle* getImageRectangleInTexture(const Sector& wholeSector,
                                        const Sector& imageSector,
                                        int textureWidth,
                                        int textureHeight) const {
    const Vector2D pos = wholeSector.getUVCoordinates(imageSector.lower());
    
    const double width  = imageSector.getDeltaLongitude().div(wholeSector.getDeltaLongitude());
    const double height = imageSector.getDeltaLatitude().div(wholeSector.getDeltaLatitude());
    
    return new Rectangle(pos.x() * textureWidth,
                         (1.0 - pos.y()) * textureHeight,
                         width * textureWidth,
                         height * textureHeight);
  }
  
  void finalize() {
    if (_finalized) {
      return;
    }
    
    _finalized = true;
    
    if (!_canceled) {
      std::vector<const IImage*>    images;
      std::vector<const Rectangle*> rectangles;
      std::string petitionsID;
      
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
        const GLTextureID glTextureID = _texturesHandler->getGLTextureId(images,
                                                                         rectangles,
                                                                         TextureSpec(petitionsID,
                                                                                     textureWidth,
                                                                                     textureHeight));
        if (glTextureID.isValid()) {
          getMesh()->setGLTextureIDForLevel(0, glTextureID);
        }
      }
      
      for (int i = 0; i < images.size(); i++) {
        _factory->deleteImage(images[i]);
      }
      
      
      for (int i = 0; i < rectangles.size(); i++) {
        delete rectangles[i];
      }
      
      //      _tile->setTexturizerDirty(true);
      _tile->setTextureSolved(true);
    }
    
    checkHasToLive();
    
    /*
     mixTexture();
     updateTextureMappingOfMesh();
     removeMixer(); // where?? be carefull of SYNC finalization from downloader
     */
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
  
  void checkHasToLive() {
    if (_canceled || _finalized) {
      deletePetitions();
      
      _texturizer->deleteBuilder(_tileKey, this);
    }
  }
  
  void cancel() {
    _canceled = true;
    
    if (!_finalized) {
      int __testing_requests_cancelation;
      for (int i = 0; i < _requestsIds.size(); i++) {
        const long requestId = _requestsIds[i];
        _downloader->cancelRequest(requestId);
      }
    }
    _requestsIds.clear();
    
    checkHasToLive();
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
    checkIsPending(position);
    
    _status[position]  = STATUS_DOWNLOADED;
    _petitions[position]->setByteBuffer(buffer->copy());
    
    stepDone();
  }
  
  void stepCanceled(int position) {
    checkIsPending(position);
    
    _anyCanceled = true;
    
    _status[position] = STATUS_CANCELED;
    
    stepDone();
  }
  
  LeveledTexturedMesh* createMesh() const {
    int ___new_texturizer_dgd_at_work;
    
    std::vector<LazyTextureMapping*>* mappings = new std::vector<LazyTextureMapping*>();
    
    Tile* ancestor = _tile;
    bool fallbackSolved = false;
    while (ancestor != NULL) {
      int __TODO_look_for_ancestor_texture;
      
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
          const GLTextureID glTextureId = _texturizer->getTopLevelGLTextureIDForTile(ancestor);
          if (glTextureId.isValid()) {
            _texturesHandler->retainGLTextureId(glTextureId);
            mapping->setGLTextureID(glTextureId);
            fallbackSolved = true;
          }
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
  
  const GLTextureID getTopLevelGLTextureID() const {
    return (_mesh == NULL) ? GLTextureID::invalid() : _mesh->getTopLevelGLTextureID();
  }
  
  bool hasMesh() const {
    return _mesh != NULL;
  }
  
  LeveledTexturedMesh* getMesh() {
    if (_mesh == NULL) {
      _mesh = createMesh();
    }
    return _mesh;
  }
  
  void cleanMesh() {
    if (_mesh != NULL) {
      _mesh = NULL;
    }
  }
};

void BuilderDownloadStepDownloadListener::onDownload(const Response* response) {
  _builder->stepDownloaded(_position, response->getByteBuffer());
}

void BuilderDownloadStepDownloadListener::onError(const Response* response) {
  _builder->stepCanceled(_position);
}

void BuilderDownloadStepDownloadListener::onCancel(const URL* url) {
  _builder->stepCanceled(_position);
}

void MultiLayerTileTexturizer::initialize(const InitializationContext* ic,
                                          const TilesRenderParameters* parameters) {
  _downloader = ic->getDownloader();
  _parameters = parameters;
}

Mesh* MultiLayerTileTexturizer::texturize(const RenderContext* rc,
                                          const TileRenderContext* trc,
                                          Tile* tile,
                                          Mesh* tessellatorMesh,
                                          Mesh* previousMesh) {
  
  _texturesHandler = rc->getTexturesHandler();
  
  if (previousMesh != NULL) {
    return previousMesh;
  }
  
  const TileKey key = tile->getKey();
  TileTextureBuilder* builder = _builders[key];
  if (builder == NULL) {
    builder = new TileTextureBuilder(this, rc, _layerSet, _parameters, _downloader, tile, tessellatorMesh, getTextureCoordinates(trc));
    _builders[key] = builder;
  }
//  else {
//    printf("please break (point) me\n");
//  }
  
  //  if (builder != NULL) {
  //    builder->cancel();
  ////    builder->_mesh = NULL;
  ////    delete builder;
  //  }
  //  builder = new TileTextureBuilder(this, rc, _layerSet, _parameters, _downloader, tile, tessellatorMesh, getTextureCoordinates(trc));
  //  _builders[key] = builder;
  
  int ___new_texturizer_dgd_at_work;
  
  tile->setTexturizerDirty(false);
  
  return builder->getMesh();
}

void MultiLayerTileTexturizer::tileToBeDeleted(Tile* tile,
                                               Mesh* mesh) {
  const TileKey key = tile->getKey();
  
  TileTextureBuilder* builder = _builders[key];
  if (builder != NULL) {
    builder->cancel();
    //    _builders.erase(key);
    
    int __TODO_delete_builder;
    //    builder->deleteMesh();
    //    delete builder;
  }
}

void MultiLayerTileTexturizer::tileMeshToBeDeleted(Tile* tile,
                                                   Mesh* mesh) {
  const TileKey key = tile->getKey();
  
  TileTextureBuilder* builder = _builders[key];
  if (builder != NULL) {
    builder->cleanMesh();
  }
  else {
    printf("break (point) on me\n");
  }
}

void MultiLayerTileTexturizer::deleteBuilder(TileKey key,
                                             TileTextureBuilder* builder) {
  TileTextureBuilder* currentBuilder = _builders[key];
  if (currentBuilder == NULL) {
    //printf("break (point) me\n");
  }
  else {
    if (builder == currentBuilder) {
      _builders.erase(key);
      
      int __TODO_delete_builder;
      //      delete builder;
    }
    else {
      printf("break (point) me\n");
    }
  }
  
}

const GLTextureID MultiLayerTileTexturizer::getTopLevelGLTextureIDForTile(Tile* tile) {
  const TileKey key = tile->getKey();
  
  const TileTextureBuilder* builder = _builders[key];
  
  return (builder == NULL) ? GLTextureID::invalid() : builder->getTopLevelGLTextureID();
}

bool MultiLayerTileTexturizer::tileMeetsRenderCriteria(Tile* tile) {
  return false;
}

void MultiLayerTileTexturizer::ancestorTexturedSolvedChanged(Tile* tile,
                                                             Tile* ancestorTile,
                                                             bool textureSolved) {
  int __TODO;
  
  if (textureSolved == false) {
    return;
  }
  
  if (tile->isTextureSolved()) {
    return;
  }
  
  TileTextureBuilder* ancestorBuilder = _builders[ancestorTile->getKey()];
  if (ancestorBuilder == NULL) {
    return;
  }
  
  if (!ancestorBuilder->hasMesh()) {
    return;
  }
  
  LeveledTexturedMesh* ancestorMesh = ancestorBuilder->getMesh();
  
  TileTextureBuilder* tileBuilder = _builders[tile->getKey()];
  if (tileBuilder == NULL) {
    return;
  }
  
  const GLTextureID glTextureId = ancestorMesh->getTopLevelGLTextureID();
  if (glTextureId.isValid()) {
    _texturesHandler->retainGLTextureId(glTextureId);
    
    const int level = tile->getLevel() - ancestorTile->getLevel() - _parameters->_topLevel;
    
    tileBuilder->getMesh()->setGLTextureIDForLevel(level, glTextureId);
  }
}

float* MultiLayerTileTexturizer::getTextureCoordinates(const TileRenderContext* trc) const {
  if (_texCoordsCache == NULL) {
    std::vector<MutableVector2D>* texCoordsV = trc->getTessellator()->createUnitTextCoords();
    
    const int texCoordsSize = texCoordsV->size();
    float* texCoordsA = new float[2 * texCoordsSize];
    int p = 0;
    for (int i = 0; i < texCoordsSize; i++) {
      texCoordsA[p++] = (float) texCoordsV->at(i).x();
      texCoordsA[p++] = (float) texCoordsV->at(i).y();
    }
    
    delete texCoordsV;
    
    _texCoordsCache = texCoordsA;
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
  //  printf("JustCreatedTopTile=%s\n", tile->getKey().description().c_str());
  
  std::vector<Petition*> petitions = _layerSet->createTilePetitions(rc,
                                                                    tile,
                                                                    _parameters->_tileTextureWidth,
                                                                    _parameters->_tileTextureHeight);
  
  
  _pendingTopTileRequests += petitions.size();
  
  const long priority = 1000000000;
  for (int i = 0; i < petitions.size(); i++) {
    const Petition* petition = petitions[i];
    _downloader->request(URL(petition->getURL()),
                         priority,
                         new TopTileDownloadListener(this),
                         true);
    
    delete petition;
  }
}

bool MultiLayerTileTexturizer::isReady(const RenderContext *rc) {
  const bool isReady = _pendingTopTileRequests <= 0;
  //  printf("MultiLayerTileTexturizer::isReady(%d)\n", isReady);
  return isReady;
}

void MultiLayerTileTexturizer::onTerrainTouchEvent(const Geodetic3D& g3d, const Tile* tile){
  _layerSet->onTerrainTouchEvent(g3d, tile);
}

