//
//  StaticImageLayer.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 26/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "StaticImageLayer.hpp"

#include "IStringBuilder.hpp"

#include "IStorage.hpp"

#include "Tile.hpp"
#include "Petition.hpp"


std::vector<Petition*> StaticImageLayer::getTilePetitions(const RenderContext* rc,
                                                          const Tile* tile, int width, int height) const
{
  std::vector<Petition*> res;

  Sector tileSector = tile->getSector();
  
  if (!_bbox.fullContains(tileSector)) {
    return res;
  }
  
  //CREATING ID FOR PETITION
  
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->add(_layerID)->add("_")->add(tileSector.lower().latitude().degrees());
  isb->add("_")->add(tileSector.lower().longitude().degrees());
  isb->add("_")->add(tileSector.upper().latitude().degrees());
  isb->add("_")->add(tileSector.upper().longitude().degrees());
  
  
  const URL id = URL(isb->getString());
  
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
