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
#include "TexturedMesh.hpp"


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
  Tile*                  _tile;
  std::vector<Petition*> _petitions;
  int                    _petitionsCount;
  int                    _stepsDone;
  
  std::vector<PetitionStatus>    _status;
  std::vector<const ByteBuffer*> _buffers;
  std::vector<long>              _requestsIds;

  Mesh* _mesh;
  
  bool _anyCanceled;
  
public:
  TileTextureBuilder(const RenderContext*         rc,
                     const LayerSet* const        layerSet,
                     const TilesRenderParameters* parameters,
                     IDownloader*                 downloader,
                     Tile* tile) :
  _tile(tile),
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
  
  void stepDone() {
//    bool completed   = true;
//    bool anyCanceled = false;
//    
//    for (int i = 0; i < _petitionsCount; i++) {
//      const int status = _status[i];
//      if (status == STATUS_PENDING) {
//        completed = false;
//        break;
//      }
//      else if (status == STATUS_CANCELED) {
//        anyCanceled = true;
//      }
//    }
//    
//    if (completed) {
//      int __diego_at_work;
//      if (anyCanceled) {
//        printf("Completed with cancelation\n");
//      }
//      else {
//        printf("Completed!!!!\n");
//      }
//      _tile->setTextureSolved(true);
//    }
    
    _stepsDone++;
    
    if (_stepsDone == _petitionsCount) {
//      bool canceled = false;
//      for (int i = 0; i < _petitionsCount; i++) {
//        const int status = _status[i];
//        if (status == STATUS_CANCELED) {
//          canceled = true;
//          break;
//        }
//      }
      
      if (_anyCanceled) {
        printf("Completed with cancelation\n");
      }
      else {
//        printf("Completed!!!!\n");
      }
      
      int ___new_texturizer_dgd_at_work;
      
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
  
  Mesh* createMesh() const {
    int ___new_texturizer_dgd_at_work;

//    LeveledTexturedMesh* mesh = new LeveledTexturedMesh();
//    
//    return mesh;
    return NULL;
  }
  
  Mesh* getMesh() {
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
    builder = new TileTextureBuilder(rc, _layerSet, _parameters, _downloader, tile);
    _builders[key] = builder;
  }
  
  int ___XX;
  
  return builder->getMesh();
}

void MultiLayerTileTexturizer::tileToBeDeleted(Tile* tile,
                                               Mesh* mesh) {
  
}

bool MultiLayerTileTexturizer::tileMeetsRenderCriteria(Tile* tile) {
  return true;
}
