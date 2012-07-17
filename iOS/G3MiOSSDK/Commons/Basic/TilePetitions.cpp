//
//  TilePetitions.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 17/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "TilePetitions.hpp"

void TilePetitions::onDownload(const Response &response)
{
  _nDownloads++;
  
  std::string url = response.getURL().getPath();
  
  for (int j = 0; j < _petitions.size(); j++) {
    if (_petitions[j].getURL() == url)
    {
      //STORING PIXEL DATA FOR RECEIVED URL
      ByteBuffer *bb = new ByteBuffer(*response.getByteBuffer());
      _petitions[j].setByteBuffer(bb);
    }
  }
  
  if (allFinished())
  {
    _texturizer->onTilePetitionsFinished(this);
  }
  
  if (_nDownloads + _nErrors == _petitions.size()){
    delete this;
  }
}


void TilePetitions::onError(const Response& e)
{
  _nErrors++;
  if (_nDownloads + _nErrors == _petitions.size()){
    delete this;
  }
}

bool TilePetitions::allFinished() const{ 
  for (int i = 0; i < _petitions.size(); i++) {
    if (!_petitions[i].isArrived()){
      return false;
    }
  }
  return true;
}