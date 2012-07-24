//
//  LayerSet.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 23/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "LayerSet.hpp"

TilePetitions* LayerSet::createTilePetitions(const Tile& tile, int width, int height) const
{
  TilePetitions *tt = new TilePetitions(tile.getLevel(), tile.getRow(), tile.getColumn(), NULL);
  
  for (int i = 0; i < _layers.size(); i++) {
    Layer* layer = _layers[i];
    
    if (layer->fullContains(tile.getSector())){
      std::vector<Petition> pet = layer->getTilePetitions(tile, width, height);
      
      tt->add(pet);
    }
  }
  return tt;
}
