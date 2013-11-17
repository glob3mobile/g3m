//
//  G3MRasterLayersDemoScene.hpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/16/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#ifndef __G3MApp__G3MRasterLayersDemoScene__
#define __G3MApp__G3MRasterLayersDemoScene__

#include "G3MDemoScene.hpp"

class LayerSet;

class G3MRasterLayersDemoScene : public G3MDemoScene {
private:
  void createLayerSet(LayerSet* layerSet);

public:
  G3MRasterLayersDemoScene(G3MDemoModel* model) :
  G3MDemoScene("Raster Layers", model)
  {
    _options.push_back("MapBox OSM");
    _options.push_back("Open Street Map");
  }

  void activate();

};

#endif
