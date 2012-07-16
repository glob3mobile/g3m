//
//  SimpleTileTexturizer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#include "SimpleTileTexturizer.hpp"

#include "Context.hpp"


TilePetitions SimpleTileTexturizer::getTilePetitions(const Tile* tile) const
{
  
  std::string url = "http://www.arkive.org/images/browse/world-map.jpg"; //FIXED

  //SAVING PETITION
  TilePetitions tt(tile);
  tt.add(url, tile->getSector());
  ((std::vector<TilePetitions>)_tilePetitions).push_back(tt);
  
  return tt;
}



Mesh* SimpleTileTexturizer::texturize(const RenderContext* rc,
                                      const Tile* tile,
                                      Mesh* mesh) const {
  //THROWING AND CREATING THE PETITIONS
  int priority = 10;
  TilePetitions tp = getTilePetitions(tile);
  Downloader* d = rc->getDownloader();
  for (int i = 0; i < tp.getPetitions().size(); i++) {
    const std::string& url = tp.getPetitions()[i].getURL();
    d->request(url, priority, (IDownloadListener*) this);
  }
  
  
  //CHECKING IF THE TILE IS COMPLETED
  for (int i = 0; i < _tilePetitions.size(); i++) { //EACH TILE
    if (_tilePetitions[i].getTile()== tile){
      
      if (_tilePetitions[i].allFinished())
      {
        //TAKING JUST FIRST!!!
        const ByteBuffer& bb = _tilePetitions[i].getPetitions()[0].getByteBuffer();
        IImage *im = rc->getFactory()->createImageFromData(bb);
        
        const std::string& url = _tilePetitions[i].getPetitions()[0].getURL();
        int __JM_at_work;        
        int texID = rc->getTexturesHandler()->getTextureId(rc, im, url, 256, 256);
        
        
      } else{
        break;
      }
      
    }
  }
  
  
  return mesh;
}

void SimpleTileTexturizer::onDownload(const Response &response){
  
  std::string url = response.getURL().getPath();
  for (int i = 0; i < _tilePetitions.size(); i++) { //EACH TILE
    
    for (int j = 0; j < _tilePetitions[i].getPetitions().size(); j++) {
      if (_tilePetitions[i].getPetitions()[j].getURL() == url)
      {
        //STORING PIXEL DATA FOR RECEIVED URL
        _tilePetitions[i].getPetitions()[j].setByteBuffer(response.getByteBuffer());
      }
    }
  }
}


void SimpleTileTexturizer::onError(const Response& e)
{
  
}
