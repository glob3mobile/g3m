//
//  LayerSet.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 23/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "LayerSet.hpp"

std::vector<Petition*> LayerSet::createTilePetitions(const Tile& tile, int width, int height) const
{
  std::vector<Petition*> petitions;
  
  const Sector tileSector = tile.getSector();
  
  if (tileSector.getDeltaLatitude().degrees() < 45){
    int a = 0; a++;
  }
  
  for (int i = 0; i < _layers.size(); i++) {
    Layer* layer = _layers[i];
    if (layer->fullContains(tileSector)){
      std::vector<Petition*> pet = layer->getTilePetitions(tile, width, height);
      for (int i = 0; i < pet.size(); i++) {
        petitions.push_back(pet[i]);
      }
    }
  }
  
  return petitions;
}
