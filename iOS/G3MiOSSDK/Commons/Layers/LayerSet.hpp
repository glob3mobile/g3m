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


class LayerSetChangedListener {
public:
#ifdef C_CODE
  virtual ~LayerSetChangedListener() {

  }
#endif

  virtual void changed(const LayerSet* layerSet) = 0;
};


class LayerSet {
private:
  std::vector<Layer*> _layers;

  LayerSetChangedListener* _listener;
  
public:
  LayerSet() :
  _listener(NULL)
  {

  }

  ~LayerSet() {
    for (unsigned int i = 0; i < _layers.size(); i++) {
      delete _layers[i];
    }
  }
  
  void addLayer(Layer* layer);
  
  std::vector<Petition*> createTileMapPetitions(const G3MRenderContext* rc,
                                                const Tile* tile,
                                                int width, int height) const;
  
  void onTerrainTouchEvent(const G3MEventContext* ec,
                           const Geodetic3D& g3d,
                           const Tile* tile) const;
  
  bool isReady() const;
  
  void initialize(const G3MContext* context)const;
  
  int size() const {
    return _layers.size();
  }

  void layerChanged(const Layer* layer) const;

  void setChangeListener(LayerSetChangedListener* listener) {
    if (_listener != NULL) {
      ILogger::instance()->logError("Listener already set");
    }
    _listener = listener;
  }

  Layer* get(int index);
  
  Layer* getLayer(const std::string& name);
};

#endif
