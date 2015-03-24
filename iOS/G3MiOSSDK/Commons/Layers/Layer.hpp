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
class TileImageProvider;

#include "Info.hpp"

class Layer {
private:
  bool isEqualsParameters(const Layer* that) const;

protected:
  std::vector<LayerTouchEventListener*> _listeners;
  std::vector<std::string>              _errors;

  LayerSet* _layerSet;

  bool _enable;

  std::vector<const Info*>* _layerInfo;

  float                 _transparency;
  const LayerCondition* _condition;

  void notifyChanges() const;

  std::string _title;

  Layer(float           transparency,
        const LayerCondition* condition,
        std::vector<const Info*>* layerInfo);

  virtual std::string getLayerType() const = 0;

  virtual bool rawIsEquals(const Layer* that) const = 0;

  const std::vector<const LayerTilesRenderParameters*> createParametersVectorCopy() const;

public:

  const float getTransparency() const {
    return _transparency;
  }
  
  void setTransparency(float transparency);
  
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

  virtual const std::vector<const LayerTilesRenderParameters*> getLayerTilesRenderParametersVector() const = 0;

  virtual void selectLayerTilesRenderParameters(int index) = 0;

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
  
  virtual TileImageProvider* createTileImageProvider(const G3MRenderContext* rc,
                                                     const LayerTilesRenderParameters* layerTilesRenderParameters) const = 0;

  void setInfo(const std::vector<const Info*>& info) const;
  
  const std::vector<const Info*>& getInfo() const;
  
  void addInfo(const std::vector<const Info*>& info);
  
  void addInfo(const Info* info);

  virtual const std::vector<URL*> getDownloadURLs(const Tile* tile) const = 0;

};

#endif
