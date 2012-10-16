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
#include "IFactory.hpp"
#include "Context.hpp"
#include "URL.hpp"
#include "TerrainTouchEventListener.hpp"

class Petition;
class Tile;
class LayerCondition;
class LayerSet;

class Layer {
private:
  LayerCondition*                         _condition;
  std::vector<TerrainTouchEventListener*> _listeners;
  
public:
  
  Layer(LayerCondition* condition) :
  _condition(condition)
  {
    
  }
  
  virtual ~Layer() {
  };
  
  virtual std::vector<Petition*> getMapPetitions(const RenderContext* rc,
                                                 const Tile* tile,
                                                 int width, int height) const = 0;
  
  virtual bool isAvailable(const RenderContext* rc,
                           const Tile* tile) const;
  
  virtual bool isAvailable(const EventContext* ec,
                           const Tile* tile) const;
  
//  virtual bool isTransparent() const = 0;
  
  virtual URL getFeatureInfoURL(const Geodetic2D& g,
                                const IFactory* factory,
                                const Sector& sector,
                                int width, int height) const = 0;
  
  virtual bool isReady() const { 
    return true;
  }
  
  virtual void initialize(const InitializationContext* ic) {}
  
  void addTerrainTouchEventListener(TerrainTouchEventListener* listener) {
    _listeners.push_back(listener);
  }
  
  void onTerrainTouchEventListener(const EventContext* ec,
                                   TerrainTouchEvent& tte) const {
    for (unsigned int i = 0; i < _listeners.size(); i++) {
      TerrainTouchEventListener* listener = _listeners[i];
      if (listener != NULL) {
        listener->onTerrainTouchEvent(ec, tte);
      }
    }
  }
  
};

#endif
