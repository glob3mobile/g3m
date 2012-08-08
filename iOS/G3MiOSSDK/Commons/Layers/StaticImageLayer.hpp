//
//  StaticImageLayer.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 26/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_StaticImageLayer_hpp
#define G3MiOSSDK_StaticImageLayer_hpp

#include "Layer.hpp"
#include "Sector.hpp"
#include "IImage.hpp"

class IStorage;

class StaticImageLayer: public Layer
{
private:
  Sector            _bbox;
  const IImage*     _image;
  const std::string _layerID;
  IStorage* const   _storage;
public:
  
  StaticImageLayer(std::string layerID, IImage* image, const Sector& sector, IStorage* storage): 
  _image(image), _bbox(sector), _layerID(layerID), _storage(storage){}
  
  ~StaticImageLayer(){
    delete _image;
  }
  
  bool fullContains(const Sector& s) const {
    return _bbox.fullContains(s);
  }
  
  std::vector<Petition*> getTilePetitions(const RenderContext* rc,
                                          const Tile* tile,
                                          int width, int height) const;
  
  bool isAvailable(const RenderContext* rc,
                   const Tile* tile)const {
    return true;
  }
  
  bool isTransparent() const{
    return true;
  }
  
};


#endif
