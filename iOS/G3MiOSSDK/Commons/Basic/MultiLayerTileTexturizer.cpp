//
//  MultiLayerTileTexturizer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 08/08/12.
//
//

#include "MultiLayerTileTexturizer.hpp"
//#include "IDownloader.hpp"
#include "Context.hpp"
#include "LayerSet.hpp"
#include "TilesRenderParameters.hpp"
#include "Tile.hpp"

class Mixer {
private:
  Tile*                  _tile;
  std::vector<Petition*> _petitions;
  const int              _petitionsCount;
  
  std::vector<int>               _status;
  std::vector<const ByteBuffer*> _buffers;
  
  static const int PENDING    = 0;
  static const int DOWNLOADED = 1;
  static const int CANCELED   = 2;
  
public:
  Mixer(Tile* tile,
        std::vector<Petition*>& petitions) :
  _tile(tile),
  _petitions(petitions),
  _petitionsCount(petitions.size())
  {
    
    for (int i = 0; i < _petitionsCount; i++) {
      _status.push_back(0 /*Mixer::PENDING*/);
      _buffers.push_back(NULL);
    }
  }

  void checkCompletion() {
    
    bool completed = true;
    bool anyCanceled = false;
    
    for (int i = 0; i < _petitionsCount; i++) {
      const int status = _status[i];
      if (status == Mixer::PENDING) {
        completed = false;
        break;
      }
      else if (status == Mixer::CANCELED) {
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
  
  void downloaded(int position,
                  const ByteBuffer* buffer) {
    if (_status[position] != Mixer::PENDING) {
      printf("Logic error, expected pending\n");
    }
    _status[position]  = Mixer::DOWNLOADED;
    _buffers[position] = buffer;
    
    checkCompletion();
  }
  
  void canceled(int position) {
    if (_status[position] != Mixer::PENDING) {
      printf("Logic error, expected pending\n");
    }
    _status[position] = Mixer::CANCELED;
    
    checkCompletion();
  }
};


class DownloadListener : public IDownloadListener {
private:
  Mixer*    _mixer;
  const int _position;
  
public:
  DownloadListener(Mixer* mixer,
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
  
  std::vector<Petition*> petitions = _layerSet->createTilePetitions(rc,
                                                                    tile,
                                                                    _parameters->_tileTextureWidth,
                                                                    _parameters->_tileTextureHeight);
  
  Mixer* mixer = new Mixer(tile, petitions);
  
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
  
  
  int ___XX;
  
  return 0;
}

void MultiLayerTileTexturizer::tileToBeDeleted(Tile* tile) {
  
}

bool MultiLayerTileTexturizer::tileMeetsRenderCriteria(Tile* tile) {
  return true;
}
