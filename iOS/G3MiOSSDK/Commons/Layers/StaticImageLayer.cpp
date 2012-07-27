//
//  StaticImageLayer.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 26/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "StaticImageLayer.hpp"

std::vector<Petition*> StaticImageLayer::getTilePetitions(const Tile& tile, int width, int height) const
{
  std::vector<Petition*> res;
  
  if (!_bbox.fullContains(tile.getSector())) {
    return res;
  }
  
  
  
  Sector imageSector = tile.getSector();
  char id[200];
  sprintf(id, "%s_%f_%f_%f_%f", _layerID.c_str(), 
          imageSector.lower().latitude().degrees(),
          imageSector.lower().longitude().degrees(),
          imageSector.upper().latitude().degrees(),
          imageSector.upper().longitude().degrees());
  
  Petition *pet = new Petition(tile.getSector(), id);

  if (_storage != NULL)
  {
    if (_storage->contains(id)){
      ByteBuffer* bb = _storage->getByteBuffer(id);
      pet->setByteBuffer(bb);        //FILLING DATA
      res.push_back(pet);
      return res;
    }
  }
  
  

  
  double widthUV = imageSector.getDeltaLongitude().degrees() / _bbox.getDeltaLongitude().degrees();
  double heightUV = imageSector.getDeltaLatitude().degrees() / _bbox.getDeltaLatitude().degrees();
  
  Vector2D p = _bbox.getUVCoordinates(imageSector.lower().latitude(), imageSector.lower().longitude());
  Vector2D pos(p.x(), p.y() - heightUV);
  
  Rectangle r(pos.x() * _image->getWidth(), pos.y() * _image->getHeight(), widthUV * _image->getWidth(), heightUV * _image->getHeight());
  
  Rectangle r2(0, 0, 0.5 * _image->getWidth(), 0.5 * _image->getHeight());
  
  IImage* subImage = _image->subImage(r);
  
  ByteBuffer* bb = subImage->getEncodedImage(); //Image Encoding PNG
  pet->setByteBuffer(bb);        //FILLING DATA

  res.push_back(pet);
  
  if (_storage != NULL){
    _storage->save(id, *bb);
  }
  
  return res;
}
