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
#include "Petition.hpp"


std::vector<Petition*> StaticImageLayer::getMapPetitions(const RenderContext* rc,
                                                         const Tile* tile, int width, int height) const
{
  std::vector<Petition*> res;
  
  Sector tileSector = tile->getSector();
  
  if (!_sector.fullContains(tileSector)) {
    return res;
  }
  
  //CREATING ID FOR PETITION
  const URL url = URL(rc->getFactory()->stringFormat("%s_%f_%f_%f_%f", _layerID.c_str(),
                                                    tileSector.lower().latitude().degrees(),
                                                    tileSector.lower().longitude().degrees(),
                                                    tileSector.upper().latitude().degrees(),
                                                    tileSector.upper().longitude().degrees() ));
  
  Petition *pet = new Petition(tileSector, url);
  
  if (_storage != NULL) {
    if (_storage->contains(url)) {
      const ByteBuffer* buffer = _storage->read(url);
      pet->setByteBuffer(buffer);        //FILLING DATA
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
  
  const ByteBuffer* buffer = subImage->getEncodedImage(); //Image Encoding PNG
  pet->setByteBuffer(buffer);        //FILLING DATA
  delete subImage;
  
  res.push_back(pet);
  
  if (_storage != NULL) {
    _storage->save(url, *buffer);
  }
  
  return res;
}
