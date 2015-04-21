//
//  LayerTouchEventListener.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 14/08/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_TerrainTouchEventListener
#define G3MiOSSDK_TerrainTouchEventListener

#include "Geodetic3D.hpp"
#include "Sector.hpp"
class Layer;

class LayerTouchEvent {
private:
  const Geodetic3D _position;
  const Sector     _sector;
  const Layer*     _layer;

public:
  LayerTouchEvent(const Geodetic3D& position,
                  const Sector& sector,
                  const Layer* layer):
  _position(position),
  _sector(sector),
  _layer(layer)
  {

  }

  const Geodetic3D getPosition() const {
    return _position;
  }

  const Sector getSector() const {
    return _sector;
  }

  const Layer* getLayer() const {
    return _layer;
  }

};


class LayerTouchEventListener {
public:

  /**
   Process terrain touch event, return true if the event was processed.
   */
  virtual bool onTerrainTouch(const G3MEventContext* context,
                              const LayerTouchEvent& ev) = 0;

#ifdef C_CODE
  virtual ~LayerTouchEventListener() { }
#endif
#ifdef JAVA_CODE
  void dispose();
#endif
  
};

#endif
