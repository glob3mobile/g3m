//
//  Layer.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 23/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_Layer
#define G3MiOSSDK_Layer

#include <string>
#include <vector>

class LayerCondition;
class LayerTouchEventListener;
class LayerSet;
class LayerTilesRenderParameters;
class G3MRenderContext;
class G3MEventContext;
class Tile;
class URL;
class RenderState;
class Geodetic2D;
class G3MContext;
class Sector;
class LayerTouchEvent;
class Petition;
class TileImageProvider;


class Layer {
protected:
  std::vector<LayerTouchEventListener*> _listeners;
  std::vector<std::string>              _errors;

  LayerSet* _layerSet;

  bool _enable;

  std::string _disclaimerInfo;

  std::vector<std::string> _info;

#ifdef C_CODE
  const LayerTilesRenderParameters* _parameters;
#endif
#ifdef JAVA_CODE
  protected LayerTilesRenderParameters _parameters;
#endif

  const float           _transparency;
  const LayerCondition* _condition;

  void notifyChanges() const;

  std::string _title;

  Layer(const LayerTilesRenderParameters* parameters,
        const float                       transparency,
        const LayerCondition*             condition,
        const std::string&                disclaimerInfo);

  void setParameters(const LayerTilesRenderParameters* parameters);

  virtual std::string getLayerType() const = 0;

  virtual bool rawIsEquals(const Layer* that) const = 0;

public:

  virtual void setEnable(bool enable) {
    if (enable != _enable) {
      _enable = enable;
      notifyChanges();
    }
  }

  virtual bool isEnable() const {
    return _enable;
  }

  virtual ~Layer();

  virtual bool isAvailable(const Tile* tile) const;

  virtual URL getFeatureInfoURL(const Geodetic2D& position,
                                const Sector& sector) const = 0;

  virtual RenderState getRenderState() = 0;

  virtual void initialize(const G3MContext* context) {
  }

  void addLayerTouchEventListener(LayerTouchEventListener* listener) {
    _listeners.push_back(listener);
  }

  bool onLayerTouchEventListener(const G3MEventContext* ec,
                                 const LayerTouchEvent& tte) const;

  void setLayerSet(LayerSet* layerSet);

  void removeLayerSet(LayerSet* layerSet);

  const LayerTilesRenderParameters* getLayerTilesRenderParameters() const {
    return _parameters;
  }

  virtual const std::string description() const = 0;
#ifdef JAVA_CODE
  @Override
  public String toString() {
    return description();
  }
#endif

  virtual bool isEquals(const Layer* that) const;

  virtual Layer* copy() const = 0;

  virtual const Sector getDataSector() const = 0;

  const std::string getTitle() const;

  void setTitle(const std::string& title);

  virtual std::vector<Petition*> createTileMapPetitions(const G3MRenderContext* rc,
                                                        const LayerTilesRenderParameters* layerTilesRenderParameters,
                                                        const Tile* tile) const = 0;

  virtual TileImageProvider* createTileImageProvider(const G3MRenderContext* rc,
                                                     const LayerTilesRenderParameters* layerTilesRenderParameters) const = 0;

  const std::string getInfo() const {
    return _disclaimerInfo;
  }

  void setInfo(const std::string& disclaimerInfo);

#warning TODO BETTER
  std::vector<std::string> getInfos() {
    _info.clear();
    const std::string layerInfo = getInfo();
    _info.push_back(layerInfo);
    return _info;
  }
  
};

#endif
