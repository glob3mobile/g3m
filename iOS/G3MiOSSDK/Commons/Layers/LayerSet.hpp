//
//  LayerSet.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 23/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_LayerSet_hpp
#define G3MiOSSDK_LayerSet_hpp

#include <vector>
#include "Layer.hpp"

class Petition;

class LayerSet
{
private:
  std::vector<Layer*> _layers;
  
public:
  
  ~LayerSet() {
    for (int i = 0; i < _layers.size(); i++) {
      delete _layers[i];
    }
  }
  
  void addLayer(Layer* layer) {
    _layers.push_back(layer);
  }
  
  std::vector<Petition*> createTilePetitions(const RenderContext* rc,
                                             const Tile* tile,
                                             int width, int height) const;
  
  void onTerrainTouchEvent(const EventContext* ec,
                           const Geodetic3D& g3d,
                           const Tile* tile) const;
  
  int size() const {
    return _layers.size();
  }
  
};

#endif
