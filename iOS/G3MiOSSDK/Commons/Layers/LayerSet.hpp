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
#include "TilePetitions.hpp"


class LayerSet
{
private:
  std::vector<Layer*> _layers;
  
public:
  
  ~LayerSet(){
    for (int i = 0; i < _layers.size(); i++) {
      delete _layers[i];
    }
  }
  
  void add(Layer* l){
    _layers.push_back(l);
  }
  
  std::vector<Petition*> createTilePetitions(const IFactory& factory, 
                                             const Tile& tile, int width, int height) const;
  
  
};

#endif
