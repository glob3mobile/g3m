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
#include <string>
#include "Layer.hpp"
#include "Renderer.hpp"
#include "ChangedInfoListener.hpp"

class Layer;
class ChangedListener;
class G3MContext;
class TileImageProvider;
class G3MRenderContext;
class LayerTilesRenderParameters;
class Tile;
class G3MEventContext;
class Geodetic3D;
class RenderState;


class LayerSet : public ChangedInfoListener {
private:
  std::vector<Layer*> _layers;

  ChangedListener* _listener;

  ChangedInfoListener* _changedInfoListener;

  //  mutable LayerTilesRenderParameters* _layerTilesRenderParameters;
  std::vector<std::string> _errors;

  std::vector<const Info*> _infos;

  void layersChanged() const;

#ifdef C_CODE
  mutable const G3MContext* _context;
#endif
#ifdef JAVA_CODE
  private G3MContext _context;
#endif

  mutable TileImageProvider* _tileImageProvider;

  TileImageProvider* createTileImageProvider(const G3MRenderContext* rc,
                                             const LayerTilesRenderParameters* layerTilesRenderParameters) const;

  bool checkLayersDataSector(const bool forceFirstLevelTilesRenderOnStart,
                             std::vector<std::string>& errors) const;

  bool checkLayersRenderState(std::vector<std::string>& errors,
                              std::vector<Layer*>& enableLayers) const;

  LayerTilesRenderParameters* checkAndComposeLayerTilesRenderParameters (const bool forceFirstLevelTilesRenderOnStart,
                                                                        const std::vector<Layer*>& enableLayers,
                                                                        std::vector<std::string>& errors) const;

public:
  LayerSet() :
  _listener(NULL),
  _context(NULL),
  _tileImageProvider(NULL),
  //  _layerTilesRenderParameters(NULL),
  _changedInfoListener(NULL)
  {
  }

  ~LayerSet();

  void removeAllLayers(const bool deleteLayers);

  void addLayer(Layer* layer);

  bool onTerrainTouchEvent(const G3MEventContext* ec,
                           const Geodetic3D& g3d,
                           const Tile* tile) const;

  RenderState getRenderState();

  void initialize(const G3MContext* context) const;

  size_t size() const {
    return _layers.size();
  }

  void layerChanged(const Layer* layer) const;

  void setChangeListener(ChangedListener* listener);
  
  void setTileImageProvider(TileImageProvider* tileImageProvider);


  Layer* getLayer(size_t index) const;

  Layer* getLayerByTitle(const std::string& title) const;

  LayerTilesRenderParameters* createLayerTilesRenderParameters(const bool forceFirstLevelTilesRenderOnStart,
                                                               std::vector<std::string>& errors) const;

  bool isEquals(const LayerSet* that) const;

  void takeLayersFrom(LayerSet* that);

  void disableAllLayers();

  TileImageProvider* getTileImageProvider(const G3MRenderContext* rc,
                                          const LayerTilesRenderParameters* layerTilesRenderParameters) const;


  void setChangedInfoListener(ChangedInfoListener* changedInfoListener);

  const std::vector<const Info*> getInfo();

  void changedInfo(const std::vector<const Info*>& info);
  
};

#endif
