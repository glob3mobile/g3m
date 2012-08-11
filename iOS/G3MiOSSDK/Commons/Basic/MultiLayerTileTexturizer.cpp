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
  _texCoords(texCoords)
  {
    
  }
  
  virtual ~LTMInitializer() {
    
  }
  
  void calculate() {
    if (_tile == _ancestor) {
      _scale       = MutableVector2D(1, 1);
      _translation = MutableVector2D(0, 0);
    }
    else {
      const Sector tileSector     = _tile->getSector();
      const Sector ancestorSector = _ancestor->getSector();
      
      _scale       = tileSector.getScaleFactor(ancestorSector).asMutableVector2D();
      _translation = tileSector.getTranslationFactor(ancestorSector).asMutableVector2D();
    }
  }
  
  MutableVector2D getScale() const {
    return _scale;
  }
  
  MutableVector2D getTranslation() const {
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
  std::vector<Petition*>    _petitions;
  int                       _petitionsCount;
  int                       _stepsDone;
  
  //  const RenderContext*         _rc;
  const IFactory*  _factory;
  TexturesHandler* _texturesHandler;
  
  const TilesRenderParameters* _parameters;
  
  const Mesh* _tessellatorMesh;
  
  const float* _texCoords;
  
  
  std::vector<PetitionStatus>    _status;
  //  std::vector<ByteBuffer*> _buffers;
  std::vector<long>              _requestsIds;
  
  LeveledTexturedMesh* _mesh;
  
  bool _finalized;
  bool _canceled;
  bool _anyCanceled;
  
public:
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
  _tile(tile),
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
      //      _buffers.push_back(NULL);
    }
    
    for (int i = 0; i < _petitionsCount; i++) {
      const Petition* petition = _petitions[i];
      const long priority = tile->getLevel() * 1000000 + tile->getRow() * 1000 + tile->getColumn();
      const long requestId = downloader->request(URL(petition->getURL()),
                                                 priority,
                                                 new BuilderDownloadStepDownloadListener(this, i),
                                                 true);
      
      _requestsIds.push_back(requestId);
    }
    
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
        //        const ByteBuffer* buffer = _buffers[i];
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
            
            petitionsID += petition->getURL();
            petitionsID += "_";
          }
        }
      }
      
      if (images.size() > 0) {
        GLTextureID glTextureID = _texturesHandler->getGLTextureId(images,
                                                                   rectangles,
                                                                   TextureSpec(petitionsID, textureWidth, textureHeight));
        if (glTextureID.isValid()) {
          getMesh()->setGLTextureIDForLevel(_tile->getLevel() - _parameters->_topLevel,
                                            glTextureID);
        }
      }
      
      for (int i = 0; i < images.size(); i++) {
        _factory->deleteImage(images[i]);
      }
      
      
      for (int i = 0; i < rectangles.size(); i++) {
        delete rectangles[i];
      }
      
      _tile->setTexturizerDirty(true);
    }
    
    deletePetitions();
    
    
    int ___new_texturizer_dgd_at_work;
    
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
  
  void cancel() {
    _canceled = true;
    
    checkHasToLive();
  }
  
  void checkHasToLive() {
    if (_canceled && _finalized) {
      deletePetitions();
      
      delete this;
    }
    
  }
  
  void checkIsPending(int position) const {
    if (_status[position] != STATUS_PENDING) {
      printf("Logic error: Expected STATUS_PENDING at position #%d but found status: %d\n",
             position,
             _status[position]);
    }
  }
  
  void stepDownloaded(int position,
                      ByteBuffer* buffer) {
    checkIsPending(position);
    
    _status[position]  = STATUS_DOWNLOADED;
    //    _buffers[position] = buffer;
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
    
    Tile* current = _tile;
    while (current != NULL) {
      LazyTextureMappingInitializer* initializer = new LTMInitializer(_tile, current, _texCoords);
      LazyTextureMapping* mapping = new LazyTextureMapping(initializer, _texturesHandler, false);
      mappings->push_back(mapping);
      
      current = current->getParent();
    }
    
    if (mappings->size() == 0) {
      printf("*** ERROR ***\n");
    }
    
    return new LeveledTexturedMesh(_tessellatorMesh,
                                   false,
                                   mappings);
  }
  
  LeveledTexturedMesh* getMesh() {
    if (_mesh == NULL) {
      _mesh = createMesh();
    }
    return _mesh;
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
  
  if (previousMesh != NULL) {
    return previousMesh;
  }
  
  const TileKey key = tile->getKey();
  TileTextureBuilder* builder = _builders[key];
  if (builder == NULL) {
    builder = new TileTextureBuilder(this, rc, _layerSet, _parameters, _downloader, tile, tessellatorMesh, getTextureCoordinates(trc));
    _builders[key] = builder;
  }
  
  int ___new_texturizer_dgd_at_work;
  
  tile->setTexturizerDirty(false);
  
  return builder->getMesh();
}

void MultiLayerTileTexturizer::tileToBeDeleted(Tile* tile,
                                               Mesh* mesh) {
  const TileKey key = tile->getKey();
  
  TileTextureBuilder* builder = _builders[key];
  if (builder == NULL) {
    return;
  }
  
  _builders.erase(key);
//  _builders[key] = NULL;
  builder->cancel();
}

bool MultiLayerTileTexturizer::tileMeetsRenderCriteria(Tile* tile) {
  return false;
}

void MultiLayerTileTexturizer::ancestorTexturedSolvedChanged(Tile* tile,
                                                             Tile* ancestorTile,
                                                             bool textureSolved) {
  
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
