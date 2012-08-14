//
//  TerrainTouchEventListener.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 14/08/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_TerrainTouchEventListener_hpp
#define G3MiOSSDK_TerrainTouchEventListener_hpp

#include "Geodetic2D.hpp"
#include "Sector.hpp"
class Layer;

class TerrainTouchEvent{
public:
  const Geodetic2D _g2d;
  const Sector     _sector;
  const Layer *    _layer;
  
  TerrainTouchEvent(const Geodetic2D& g2d, const Sector& s, const Layer* layer):
  _g2d(g2d), _sector(s), _layer(layer){
    
  }
  
};

class TerrainTouchEventListener{
public:
  virtual void onTerrainTouchEvent(const TerrainTouchEvent& event) = 0;
  
  virtual ~TerrainTouchEventListener(){}
  
};

#endif
