//
//  TilePetitions.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 17/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "TilePetitions.hpp"

#include "TileImagesTileTexturizer.hpp"

void TilePetitions::onDownload(const Response &response)
{
  _downloadsCounter++;
  
  std::string url = response.getURL().getPath();
  
  for (int j = 0; j < _petitions.size(); j++) {
    if (_petitions[j]->getURL() == url)
    {
      //STORING PIXEL DATA FOR RECEIVED URL
      ByteBuffer *bb = new ByteBuffer(*response.getByteBuffer());
      _petitions[j]->setByteBuffer(bb);
    }
  }
  
  if (_texturizer != NULL && allFinished()) {
    _texturizer->onTilePetitionsFinished(this);
  }
  
  tryToDeleteMyself();
}


void TilePetitions::onError(const Response& e) {
  _errorsCounter++;
  tryToDeleteMyself();
}

bool TilePetitions::allFinished() const {
  for (int i = 0; i < _petitions.size(); i++) {
    if (!_petitions[i]->isArrived()){
      return false;
    }
  }
  return true;
}

void TilePetitions::onCancel(const std::string& url){
  _errorsCounter++;
  tryToDeleteMyself();
}

std::string TilePetitions::getPetitionsID() const
{
  std::string id;
  for (int j = 0; j < _petitions.size(); j++) {
    if (j > 0){
      id += "__";
    }
    id += _petitions[j]->getURL();
  }
  return id;
}
