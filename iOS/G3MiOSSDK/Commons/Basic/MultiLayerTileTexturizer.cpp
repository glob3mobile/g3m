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
  
  void onDownload(const Response& response);
  
  void onError(const Response& response);
  
  void onCancel(const URL& url);
  
};



class TileTextureBuilder {
private:
  MultiLayerTileTexturizer* _texturizer;
  Tile*                     _tile;
  std::vector<Petition*>    _petitions;
  int                       _petitionsCount;
  int                       _stepsDone;
  
  const RenderContext*         _rc;
  //  const IFactory*              _factory;
  const TilesRenderParameters* _parameters;
  
  const Mesh* _tessellatorMesh;
  
  std::vector<PetitionStatus>    _status;
  std::vector<const ByteBuffer*> _buffers;
  std::vector<long>              _requestsIds;
  
  LeveledTexturedMesh* _mesh;
  
  bool _anyCanceled;
  
public:
  TileTextureBuilder(MultiLayerTileTexturizer*    texturizer,
                     const RenderContext*         rc,
                     const LayerSet* const        layerSet,
                     const TilesRenderParameters* parameters,
                     IDownloader*                 downloader,
                     Tile* tile,
                     const Mesh* tessellatorMesh) :
  _texturizer(texturizer),
  _rc(rc),
  _parameters(parameters),
  _tile(tile),
  _tessellatorMesh(tessellatorMesh),
  _stepsDone(0),
  _anyCanceled(false),
  _mesh(NULL)
  {
    _petitions = layerSet->createTilePetitions(rc,
                                               tile,
                                               parameters->_tileTextureWidth,
                                               parameters->_tileTextureHeight);
    
    _petitionsCount = _petitions.size();
    
    for (int i = 0; i < _petitionsCount; i++) {
      _status.push_back(STATUS_PENDING);
      _buffers.push_back(NULL);
      
      
      const Petition* petition = _petitions[i];
      const long priority = tile->getLevel() * 1000000 + tile->getRow() * 1000 + tile->getColumn();
      const long requestId = downloader->request(URL(petition->getURL()),
                                                 priority,
                                                 new BuilderDownloadStepDownloadListener(this, i),
                                                 true);
      
      _requestsIds.push_back(requestId);
    }
    
  }
  
  /*
   void TilePetitions::createTexture(TexturesHandler* texturesHandler, const IFactory* factory, int width, int height)
   {
   if (allFinished())
   {
   //Creating images (opaque one must be the first)
   std::vector<const IImage*> images;
   std::vector<const Rectangle*> rectangles;
   for (int i = 0; i < getNumPetitions(); i++) {
   const ByteBuffer* bb = getPetition(i)->getByteBuffer();
   IImage *im = factory->createImageFromData(*bb);
   
   if (im != NULL) {
   Sector imSector = getPetition(i)->getSector();
   images.push_back(im);
   
   Rectangle* rec = getImageRectangleInTexture(_tileSector, imSector, width, height);
   rectangles.push_back(rec);
   }
   }
   
   //Creating the texture
   const std::string& petitionsID = getPetitionsID();
   //_texID = texturesHandler->getTextureId(images, url, width, height);
   _texID = texturesHandler->getGLTextureId(images,
   rectangles,
   TextureSpec(petitionsID, width, height));
   
   //RELEASING MEMORY
   for (int i = 0; i < _petitions.size(); i++) {
   _petitions[i]->releaseData();
   }
   for (int i = 0; i < images.size(); i++) {
   factory->deleteImage(images[i]);
   }
   for (int i = 0; i < rectangles.size(); i++) {
   delete rectangles[i];
   }
   }
   }
   */
  
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
  
  
  void stepDone() {
    _stepsDone++;
    
    if (_stepsDone == _petitionsCount) {
      if (_anyCanceled) {
        printf("Completed with cancelation\n");
      }
      else {
        //        printf("Completed!!!!\n");
        
        const IFactory* factory = _rc->getFactory();
        
        std::vector<const IImage*>    images;
        std::vector<const Rectangle*> rectangles;
        
        const int textureWidth  = _parameters->_tileTextureWidth;
        const int textureHeight = _parameters->_tileTextureHeight;
        
        const Sector tileSector = _tile->getSector();
        std::string petitionsID;

        for (int i = 0; i < _petitionsCount; i++) {
          const ByteBuffer* buffer   = _buffers[i];
          Petition*         petition = _petitions[i];
          
          IImage *im = factory->createImageFromData(*buffer);
          if (im != NULL) {
            images.push_back(im);
            
            const Sector imageSector = petition->getSector();
            
            Rectangle* rec = getImageRectangleInTexture(tileSector,
                                                        imageSector,
                                                        textureWidth,
                                                        textureHeight);
            rectangles.push_back(rec);
            

            petitionsID += petition->getURL();
            petitionsID += "_";
          }
          
          delete buffer;
          delete petition;
        }
        
        GLTextureID glTextureID = _rc->getTexturesHandler()->getGLTextureId(images,
                                                                            rectangles,
                                                                            TextureSpec(petitionsID, textureWidth, textureHeight));
        
        for (int i = 0; i < images.size(); i++) {
          factory->deleteImage(images[i]);
        }
        for (int i = 0; i < rectangles.size(); i++) {
          delete rectangles[i];
        }
        
        getMesh()->setGLTextureIDForLevel(_tile->getLevel() - _parameters->_topLevel,
                                          glTextureID);
      }
      
      int ___new_texturizer_dgd_at_work;
      
      _tile->setTexturizerDirty(true);
      /*
       mixTexture();
       updateTextureMappingOfMesh();
       removeMixer(); // where?? be carefull of SYNC finalization from downloader
       */
    }
  }
  
  void checkIsPending(int position) const {
    if (_status[position] != STATUS_PENDING) {
      printf("Logic error: Expected STATUS_PENDING at position #%d but found status: %d\n",
             position,
             _status[position]);
    }
  }
  
  void downloaded(int position,
                  const ByteBuffer* buffer) {
    checkIsPending(position);
    
    _status[position]  = STATUS_DOWNLOADED;
    _buffers[position] = buffer;
    
    stepDone();
  }
  
  void canceled(int position) {
    checkIsPending(position);
    
    _anyCanceled = true;
    
    _status[position] = STATUS_CANCELED;
    
    stepDone();
  }
  
  LeveledTexturedMesh* createMesh() const {
    int ___new_texturizer_dgd_at_work;
    
    LeveledTexturedMesh* mesh = new LeveledTexturedMesh(_tessellatorMesh, false);
    return mesh;
  }
  
  LeveledTexturedMesh* getMesh() {
    if (_mesh == NULL) {
      _mesh = createMesh();
    }
    return _mesh;
  }
};

void BuilderDownloadStepDownloadListener::onDownload(const Response& response) {
  _builder->downloaded(_position, response.getByteBuffer());
}

void BuilderDownloadStepDownloadListener::onError(const Response& response) {
  _builder->canceled(_position);
}

void BuilderDownloadStepDownloadListener::onCancel(const URL& url) {
  _builder->canceled(_position);
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
    builder = new TileTextureBuilder(this, rc, _layerSet, _parameters, _downloader, tile, tessellatorMesh);
    _builders[key] = builder;
  }
  
  int ___new_texturizer_dgd_at_work;
  
  tile->setTexturizerDirty(false);
  
  return builder->getMesh();
}

void MultiLayerTileTexturizer::tileToBeDeleted(Tile* tile,
                                               Mesh* mesh) {
  
}

bool MultiLayerTileTexturizer::tileMeetsRenderCriteria(Tile* tile) {
  return true;
}

void MultiLayerTileTexturizer::ancestorTexturedSolvedChanged(Tile* tile,
                                                             Tile* ancestorTile,
                                                             bool textureSolved) {
  
}
