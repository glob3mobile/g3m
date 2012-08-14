//
//  LayerSet.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 23/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "LayerSet.hpp"
#include "Tile.hpp"

std::vector<Petition*> LayerSet::createTilePetitions(const RenderContext* rc,
                                                     const Tile* tile,
                                                     int width, int height) const
{
  std::vector<Petition*> petitions;
  
  for (int i = 0; i < _layers.size(); i++) {
    Layer* layer = _layers[i];
    if (layer->isAvailable(rc, tile)){
      std::vector<Petition*> pet = layer->getTilePetitions(rc, tile, width, height);
      
      //Storing petitions
      for (int j = 0; j < pet.size(); j++) {
        petitions.push_back(pet[j]);
      }
    }
  }
  
  return petitions;
}

std::vector<URL*> LayerSet::getFeatureURL(const Geodetic2D& g,
                               const IFactory* factory,
                               const Tile* tile,
                               int width, int height) const
{
  std::vector<URL*> petitions;
  for (int i = 0; i < _layers.size(); i++) {
    Layer* layer = _layers[i];
    URL url = layer->getFeatureURL(g, factory, tile, width, height);
    if (!url.isNull()){
      petitions.push_back(new URL(url));
    }
  }
  return petitions;
}
