//
//  LayerSet.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 23/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_LayerSet
#define G3MiOSSDK_LayerSet

#include <vector>
#include "Layer.hpp"
#include "Renderer.hpp"

class Petition;
class Vector2I;
class LayerTilesRenderParameters;

class ChangedListener;

class LayerSet {
private:
  std::vector<Layer*> _layers;
  
  ChangedListener* _listener;
  
//  mutable LayerTilesRenderParameters* _layerTilesRenderParameters;
  std::vector<std::string> _errors;
  
  void layersChanged() const;

#ifdef C_CODE
  mutable const G3MContext* _context;
#endif
#ifdef JAVA_CODE
  private G3MContext _context;
#endif

public:
  LayerSet() :
  _listener(NULL),
//  _layerTilesRenderParameters(NULL),
  _context(NULL)
  {
    
  }
  
  ~LayerSet();
  
  void removeAllLayers(const bool deleteLayers);
  
  void addLayer(Layer* layer);
  
  std::vector<Petition*> createTileMapPetitions(const G3MRenderContext* rc,
                                                const LayerTilesRenderParameters* layerTilesRenderParameters,
                                                const Tile* tile) const;
  
  bool onTerrainTouchEvent(const G3MEventContext* ec,
                           const Geodetic3D& g3d,
                           const Tile* tile) const;
  
  RenderState getRenderState();
  
  void initialize(const G3MContext* context)const;
  
  int size() const {
    return _layers.size();
  }
  
  void layerChanged(const Layer* layer) const;
  
  void setChangeListener(ChangedListener* listener) {
    if (_listener != NULL) {
      ILogger::instance()->logError("Listener already set");
    }
    _listener = listener;
  }

  Layer* getLayer(int index) const;

  Layer* getLayerByName(const std::string& name) const;
  Layer* getLayerByTitle(const std::string& title) const;

  LayerTilesRenderParameters* createLayerTilesRenderParameters(std::vector<std::string>& errors) const;

  bool isEquals(const LayerSet* that) const;

  void takeLayersFrom(LayerSet* that);

  void disableAllLayers();
  
};

#endif
