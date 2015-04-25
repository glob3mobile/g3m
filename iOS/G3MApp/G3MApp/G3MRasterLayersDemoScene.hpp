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

protected:
  void rawActivate(const G3MContext* context);

  void rawSelectOption(const std::string& option,
                       int optionIndex);

public:
  G3MRasterLayersDemoScene(G3MDemoModel* model) :
  G3MDemoScene(model, "Raster Layers", "", 0)
  {
    _options.push_back("Open Street Map");

    _options.push_back("MapQuest OSM");
    _options.push_back("MapQuest Aerial");

    _options.push_back("MapBox OSM");
    _options.push_back("MapBox Terrain");
    _options.push_back("MapBox Aerial");
    _options.push_back("CartoDB Meteorites");
    _options.push_back("Nasa Blue Marble (WMS)");

    // _options.push_back("ESRI ArcGis Online");

    _options.push_back("Bing Aerial");
    _options.push_back("Bing Aerial with Labels");

    _options.push_back("Uruguay (WMS)");
  }
  
};

#endif
