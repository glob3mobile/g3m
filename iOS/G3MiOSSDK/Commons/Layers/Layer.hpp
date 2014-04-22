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
class TimeInterval;
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
  LayerCondition*                       _condition;
  std::vector<LayerTouchEventListener*> _listeners;
  std::vector<std::string>              _errors;

  LayerSet* _layerSet;

  bool _enable;

  const std::string _name;

#ifdef C_CODE
  const LayerTilesRenderParameters* _parameters;
#endif
#ifdef JAVA_CODE
  protected LayerTilesRenderParameters _parameters;
#endif

  const long long _timeToCacheMS;
  const bool      _readExpired;

  void notifyChanges() const;

  std::string _title;

  const float _transparency;

  Layer(LayerCondition* condition,
        const std::string& name,
        const TimeInterval& timeToCache,
        bool readExpired,
        const LayerTilesRenderParameters* parameters,
        float transparency);

  void setParameters(const LayerTilesRenderParameters* parameters);

  virtual std::string getLayerType() const = 0;

  virtual bool rawIsEquals(const Layer* that) const = 0;

public:

  const TimeInterval getTimeToCache() const;

  bool getReadExpired() const {
    return _readExpired;
  }

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

  virtual bool isAvailable(const G3MRenderContext* rc,
                           const Tile* tile) const;

  virtual bool isAvailable(const G3MEventContext* ec,
                           const Tile* tile) const;

  //  virtual bool isTransparent() const = 0;

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

  const std::string getName();

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

  bool isEquals(const Layer* that) const;
  
  virtual Layer* copy() const = 0;


  const std::string getTitle() const;

  void setTitle(const std::string& title);

  virtual std::vector<Petition*> createTileMapPetitions(const G3MRenderContext* rc,
                                                        const LayerTilesRenderParameters* layerTilesRenderParameters,
                                                        const Tile* tile) const = 0;

#warning TODO
//  virtual TileImageProvider* createTileImageProvider(const G3MRenderContext* rc,
//                                                     const LayerTilesRenderParameters* layerTilesRenderParameters,
//                                                     const Tile* tile) const = 0;

};

#endif
