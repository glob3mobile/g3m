//
//  StaticImageLayer.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 26/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "StaticImageLayer.hpp"

#include <sstream>

#include "IStorage.hpp"

#include "Tile.hpp"

std::vector<Petition*> StaticImageLayer::getTilePetitions(const RenderContext* rc,
                                                          const Tile* tile, int width, int height) const
{
  std::vector<Petition*> res;

  Sector tileSector = tile->getSector();
  
  if (!_bbox.fullContains(tileSector)) {
    return res;
  }
  
  //CREATING ID FOR PETITION
  std::string id = rc->getFactory()->stringFormat("%s_%f_%f_%f_%f", _layerID.c_str(),
                                                  tileSector.lower().latitude().degrees(),
                                                  tileSector.lower().longitude().degrees(),
                                                  tileSector.upper().latitude().degrees(),
                                                  tileSector.upper().longitude().degrees() );
  
  Petition *pet = new Petition(tileSector, id, true);

  if (_storage != NULL)
  {
    if (_storage->contains(id)){
      const ByteBuffer* bb = _storage->read(id);
      pet->setByteBuffer(bb);        //FILLING DATA
      res.push_back(pet);
      return res;
    }
  }
  
  double widthUV = tileSector.getDeltaLongitude().degrees() / _bbox.getDeltaLongitude().degrees();
  double heightUV = tileSector.getDeltaLatitude().degrees() / _bbox.getDeltaLatitude().degrees();
  
  Vector2D p = _bbox.getUVCoordinates(tileSector.lower().latitude(), tileSector.lower().longitude());
  Vector2D pos(p.x(), p.y() - heightUV);
  
  Rectangle r(pos.x() * _image->getWidth(), pos.y() * _image->getHeight(), widthUV * _image->getWidth(), heightUV * _image->getHeight());
  
  IImage* subImage = _image->subImage(r);
  
  ByteBuffer* bb = subImage->getEncodedImage(); //Image Encoding PNG
  pet->setByteBuffer(bb);        //FILLING DATA
  delete subImage;

  res.push_back(pet);
  
  if (_storage != NULL){
    _storage->save(id, *bb);
  }
  
  return res;
}
