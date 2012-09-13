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

std::vector<Petition*> StaticImageLayer::getMapPetitions(const RenderContext* rc,
                                                         const Tile* tile, int width, int height) const {
  std::vector<Petition*> res;
  
  Sector tileSector = tile->getSector();
  
  if (!_sector.fullContains(tileSector)) {
    return res;
  }
  
  //CREATING ID FOR PETITION
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->add(_layerID);
  isb->add("_");
  isb->add(tileSector.lower().latitude().degrees());
  isb->add("_");
  isb->add(tileSector.lower().longitude().degrees());
  isb->add("_");
  isb->add(tileSector.upper().latitude().degrees());
  isb->add("_");
  isb->add(tileSector.upper().longitude().degrees());
  
  const URL id = URL(isb->getString());
  
  delete isb;
  
  Petition *pet = new Petition(tileSector, id);
  
  if (_storage != NULL) {
    if (_storage->containsImage(id)) {
      const IImage* image = _storage->readImage(id);
      pet->setImage(image);        //FILLING DATA
      res.push_back(pet);
      return res;
    }
  }
  
  const double widthUV = tileSector.getDeltaLongitude().degrees() / _sector.getDeltaLongitude().degrees();
  const double heightUV = tileSector.getDeltaLatitude().degrees() / _sector.getDeltaLatitude().degrees();
  
  const Vector2D p = _sector.getUVCoordinates(tileSector.lower());
  const Vector2D pos(p.x(), p.y() - heightUV);
  
  Rectangle r(pos.x() * _image->getWidth(),
              pos.y() * _image->getHeight(),
              widthUV * _image->getWidth(),
              heightUV * _image->getHeight());
  
  const IImage* subImage = _image->subImage(r);
  
  pet->setImage(subImage);
  
  res.push_back(pet);
  
  if (_storage != NULL) {
    _storage->saveImage(id, subImage);
  }
  
  return res;
}
