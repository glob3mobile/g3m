package org.glob3.mobile.generated; 
//
//  Layer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 23/08/12.
//
//

//
//  Layer.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 23/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//




//class Petition;
//class Tile;
//class LayerCondition;
//class LayerSet;

public abstract class Layer {
  private LayerCondition _condition;
  private java.util.ArrayList<TerrainTouchEventListener> _listeners = new java.util.ArrayList<TerrainTouchEventListener>();

  private LayerSet _layerSet;

  private boolean _enable;

  private final String _name;

  protected final TimeInterval _timeToCache;

  protected final void notifyChanges() {
    if (_layerSet == null) {
  //    ILogger::instance()->logError("Can't notify changes, _layerSet was not set");
    }
    else {
      _layerSet.layerChanged(this);
    }
  }

  public Layer(LayerCondition condition, String name, TimeInterval timeToCache) {
     _condition = condition;
     _name = name;
     _layerSet = null;
     _timeToCache = new TimeInterval(timeToCache);
     _enable = true;

  }

  public void setEnable(boolean enable) {
    if (enable != _enable) {
      _enable = enable;
      notifyChanges();
    }
  }

  public boolean isEnable() {
    return _enable;
  }

  public void dispose() {
  }

  public abstract java.util.ArrayList<Petition> getMapPetitions(G3MRenderContext rc, Tile tile, int width, int height);

  public boolean isAvailable(G3MRenderContext rc, Tile tile) {
    if (!isEnable()) {
      return false;
    }
    if (_condition == null) {
      return true;
    }
    return _condition.isAvailable(rc, tile);
  }

  public boolean isAvailable(G3MEventContext ec, Tile tile) {
    if (!isEnable()) {
      return false;
    }
    if (_condition == null) {
      return true;
    }
    return _condition.isAvailable(ec, tile);
  }

//  virtual bool isTransparent() const = 0;

  public abstract URL getFeatureInfoURL(Geodetic2D g, IFactory factory, Sector sector, int width, int height);

  public boolean isReady() {
    return true;
  }

  public void initialize(G3MContext context) {
  }

  public final void addTerrainTouchEventListener(TerrainTouchEventListener listener) {
    _listeners.add(listener);
  }

  public final void onTerrainTouchEventListener(G3MEventContext ec, TerrainTouchEvent tte) {
    for (int i = 0; i < _listeners.size(); i++) {
      TerrainTouchEventListener listener = _listeners.get(i);
      if (listener != null) {
        listener.onTerrainTouchEvent(ec, tte);
      }
    }
  }

  public final void setLayerSet(LayerSet layerSet) {
    if (_layerSet != null) {
      ILogger.instance().logError("LayerSet already set.");
    }
    _layerSet = layerSet;
  }

  public final String getName() {
    return _name;
  }

}