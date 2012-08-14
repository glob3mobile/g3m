//
//  Layer.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 23/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_Layer_hpp
#define G3MiOSSDK_Layer_hpp

#include <string>
#include "Sector.hpp"
#include "TilePetitions.hpp"
#include "IFactory.hpp"
#include "Context.hpp"

#include "TerrainTouchEventListener.hpp"

class Layer{
protected:
  
  TerrainTouchEventListener* _ttel;

public:
  
  virtual ~Layer(){};
  
  virtual bool fullContains(const Sector& s) const = 0;

  virtual std::vector<Petition*> getTilePetitions(const RenderContext* rc,
                                                  const Tile* tile,
                                                  int width, int height) const = 0;
  
  virtual bool isAvailable(const RenderContext* rc,
                           const Tile* tile) const = 0;
  
  virtual bool isTransparent() const = 0;
  
  virtual URL getFeatureURL(const Geodetic2D& g,
                            const IFactory* factory,
                            const Sector& sector,
                            int width, int height) const = 0;
  
  void addTerrainTouchEventListener(TerrainTouchEventListener* ttel){
    _ttel = ttel;
  }
  
  void onTerrainTouchEventListener(TerrainTouchEvent& tte){
    if (_ttel != NULL){
      _ttel->onTerrainTouchEvent(tte);
    }
  }
  
};

#endif
