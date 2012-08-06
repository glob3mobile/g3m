//
//  LayerSet.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 23/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "LayerSet.hpp"
#include "Tile.hpp"

std::vector<Petition*> LayerSet::createTilePetitions(const RenderContext* rc, const IFactory& factory, const Tile& tile, int width, int height) const
{
  std::vector<Petition*> petitions;
  
  const Sector tileSector = tile.getSector();
  
  for (int i = 0; i < _layers.size(); i++) {
    Layer* layer = _layers[i];
    if (layer->isAvailable(rc, tile)){
      std::vector<Petition*> pet = layer->getTilePetitions(factory, tile, width, height);
      
      //Storing petitions
      for (int j = 0; j < pet.size(); j++) {
        petitions.push_back(pet[j]);
      }
    }
  }
  
  return petitions;
}
