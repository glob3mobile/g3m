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


class PetitionsMixer {
private:
  Tile*                  _tile;
  std::vector<Petition*> _petitions;
  const int              _petitionsCount;
  
  std::vector<PetitionStatus>    _status;
  std::vector<const ByteBuffer*> _buffers;
  
public:
  PetitionsMixer(Tile* tile,
                 std::vector<Petition*>& petitions) :
  _tile(tile),
  _petitions(petitions),
  _petitionsCount(petitions.size())
  {
    
    for (int i = 0; i < _petitionsCount; i++) {
      _status.push_back(STATUS_PENDING);
      _buffers.push_back(NULL);
    }
  }
  
  void checkCompletion() {
    bool completed   = true;
    bool anyCanceled = false;
    
    for (int i = 0; i < _petitionsCount; i++) {
      const int status = _status[i];
      if (status == STATUS_PENDING) {
        completed = false;
        break;
      }
      else if (status == STATUS_CANCELED) {
        anyCanceled = true;
      }
    }
    
    if (completed) {
      int __diego_at_work;
      if (anyCanceled) {
        printf("Completed with cancelation\n");
      }
      else {
        printf("Completed!!!!\n");
      }
      _tile->setTextureSolved(true);
    }
  }
  
  void checkIsPending(int position) {
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
    
    checkCompletion();
  }
  
  void canceled(int position) {
    checkIsPending(position);

    _status[position] = STATUS_CANCELED;
    
    checkCompletion();
  }
};


class DownloadListener : public IDownloadListener {
private:
  PetitionsMixer* _mixer;
  const int       _position;
  
public:
  DownloadListener(PetitionsMixer* mixer,
                   int position) :
  _mixer(mixer),
  _position(position)
  {
    
  }
  
  void onDownload(const Response& response) {
    _mixer->downloaded(_position, response.getByteBuffer());
  }
  
  void onError(const Response& response) {
    _mixer->canceled(_position);
  }
  
  void onCancel(const URL& url) {
    _mixer->canceled(_position);
  }
};


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
  
  const TileKey key = tile->getKey();
  
  PetitionsMixer* mixer = _mixers[key];
  if (mixer == NULL) {
    std::vector<Petition*> petitions = _layerSet->createTilePetitions(rc,
                                                                      tile,
                                                                      _parameters->_tileTextureWidth,
                                                                      _parameters->_tileTextureHeight);
    
    mixer = new PetitionsMixer(tile, petitions);
    _mixers[key] = mixer;
    
    for (int i = 0; i < petitions.size(); i++) {
      const Petition* petition = petitions[i];
      
      const URL url = URL(petition->getURL());
      const long priority = tile->getLevel();
      const bool deleteListener = true;
      
      _downloader->request(url,
                           priority,
                           new DownloadListener(mixer, i),
                           deleteListener);
    }
  }
  else {
    printf("****** mixer already created\n");
  }
  
//  tile->setTexturizerInProgress(true);
  
  Mesh* result = NULL;
  
//  if (previousMesh == NULL) {
//    const TextureMapping* textureMapping;
//    
//    result = new TexturedMesh(tessellatorMesh, false,
//                              textureMapping, true);
//  }
//  else {
//    result = previousMesh;
//  }
  
  int ___XX;
  
  return result;
}

void MultiLayerTileTexturizer::tileToBeDeleted(Tile* tile,
                                               Mesh* mesh) {
  
}

bool MultiLayerTileTexturizer::tileMeetsRenderCriteria(Tile* tile) {
  return true;
}
