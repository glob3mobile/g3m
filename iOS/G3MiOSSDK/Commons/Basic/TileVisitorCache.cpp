//
//  TileVisitorCache.cpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 04/12/13.
//
//

#include "TileVisitorCache.hpp"
#include "IDownloader.hpp"
#include "Petition.hpp"
#include "IImageDownloadListenerTileCache.hpp"


void TileVisitorCache::visitTile(std::vector<Layer*>& layers,
                                 const Tile* tile) {
  for (int i = 0; i < layers.size(); i++) {
    _numVisits++;
    const Layer* layer = layers[i];
    std::vector<Petition*> petitions = layer->createTileMapPetitions(NULL, tile);
    for (int j = 0; j < petitions.size(); j++) {
      _numPetitions++;

      const Petition* petition = petitions[i];
      
      IImageDownloadListenerTileCache* listener = new IImageDownloadListenerTileCache(_numVisits, tile, layer->getTitle());
      //IImageDownloadListenerTileCache(_numVisits, tile, layer->getTitle());
      IDownloader* downloader = _context->getDownloader();
      
      long long requestId = downloader->requestImage(URL(petition->getURL()), 1, petition->getTimeToCache(), true, listener, true);
      if(requestId == -1){
        _context->getLogger()->logInfo(
                                     "This request has been cached (z: %d, x: %d, y: %d)",tile->_level,tile->_column,tile->_row);
      }else{
        _context->getLogger()->logInfo(
                                       "Petition %d has been request (z: %d, x: %d, y: %d)",requestId, tile->_level,tile->_column,tile->_row);
      }
    }
  }
}
