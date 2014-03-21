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
//class Vector2I;
//class LayerTilesRenderParameters;

public abstract class Layer
{
  protected LayerCondition _condition;
  protected java.util.ArrayList<LayerTouchEventListener> _listeners = new java.util.ArrayList<LayerTouchEventListener>();
  protected java.util.ArrayList<String> _errors = new java.util.ArrayList<String>();

  protected LayerSet _layerSet;

  protected boolean _enable;

  protected final String _name;

  protected LayerTilesRenderParameters _parameters;

  protected final long _timeToCacheMS;
  protected final boolean _readExpired;

  protected final void notifyChanges()
  {
    if (_layerSet != null)
    {
      _layerSet.layerChanged(this);
    }
  }

  protected String _title;

  protected final float _transparency;

  protected Layer(LayerCondition condition, String name, TimeInterval timeToCache, boolean readExpired, LayerTilesRenderParameters parameters, float transparency)
  {
     _condition = condition;
     _name = name;
     _layerSet = null;
     _timeToCacheMS = timeToCache._milliseconds;
     _readExpired = readExpired;
     _enable = true;
     _parameters = parameters;
     _title = "";
     _transparency = transparency;

  }

  protected final void setParameters(LayerTilesRenderParameters parameters)
  {
    if (parameters != _parameters)
    {
      _parameters = null;
      _parameters = parameters;
      notifyChanges();
    }
  }

  protected abstract String getLayerType();

  protected abstract boolean rawIsEquals(Layer that);


  public final TimeInterval getTimeToCache()
  {
    return TimeInterval.fromMilliseconds(_timeToCacheMS);
  }

  public final boolean getReadExpired()
  {
    return _readExpired;
  }

  public void setEnable(boolean enable)
  {
    if (enable != _enable)
    {
      _enable = enable;
      notifyChanges();
    }
  }

  public boolean isEnable()
  {
    return _enable;
  }

  public void dispose()
  {
    if (_condition != null)
       _condition.dispose();
    _parameters = null;
  }

  public abstract java.util.ArrayList<Petition> createTileMapPetitions(G3MRenderContext rc, LayerTilesRenderParameters layerTilesRenderParameters, Tile tile);

  public boolean isAvailable(G3MRenderContext rc, Tile tile)
  {
    if (!isEnable())
    {
      return false;
    }
    if (_condition == null)
    {
      return true;
    }
    return _condition.isAvailable(rc, tile);
  }

  public boolean isAvailable(G3MEventContext ec, Tile tile)
  {
    if (!isEnable())
    {
      return false;
    }
    if (_condition == null)
    {
      return true;
    }
    return _condition.isAvailable(ec, tile);
  }

  //  virtual bool isTransparent() const = 0;

  public abstract URL getFeatureInfoURL(Geodetic2D position, Sector sector);

  public abstract RenderState getRenderState();

  public void initialize(G3MContext context)
  {
  }

  public final void addLayerTouchEventListener(LayerTouchEventListener listener)
  {
    _listeners.add(listener);
  }

  public final boolean onLayerTouchEventListener(G3MEventContext ec, LayerTouchEvent tte)
  {
    final int listenersSize = _listeners.size();
    for (int i = 0; i < listenersSize; i++)
    {
      LayerTouchEventListener listener = _listeners.get(i);
      if (listener != null)
      {
        if (listener.onTerrainTouch(ec, tte))
        {
          return true;
        }
      }
    }
    return false;
  }

  public final void setLayerSet(LayerSet layerSet)
  {
    if (_layerSet != null)
    {
      ILogger.instance().logError("LayerSet already set.");
    }
    _layerSet = layerSet;
  }

  public final void removeLayerSet(LayerSet layerSet)
  {
    if (_layerSet != layerSet)
    {
      ILogger.instance().logError("_layerSet doesn't match.");
    }
    _layerSet = null;
  }

  public final String getName()
  {
    return _name;
  }

  public final LayerTilesRenderParameters getLayerTilesRenderParameters()
  {
    return _parameters;
  }

  public abstract String description();
  @Override
  public String toString() {
    return description();
  }

  public final boolean isEquals(Layer that)
  {
    if (this == that)
    {
      return true;
    }
  
    if (that == null)
    {
      return false;
    }
  
    if (!getLayerType().equals(that.getLayerType()))
    {
      return false;
    }
  
    if (_condition != that._condition)
    {
      return false;
    }
  
    final int thisListenersSize = _listeners.size();
    final int thatListenersSize = that._listeners.size();
    if (thisListenersSize != thatListenersSize)
    {
      return false;
    }
  
    for (int i = 0; i < thisListenersSize; i++)
    {
      if (_listeners.get(i) != that._listeners.get(i))
      {
        return false;
      }
    }
  
    if (_enable != that._enable)
    {
      return false;
    }
  
    if (!(_name.equals(that._name)))
    {
      return false;
    }
  
    if (!_parameters.isEquals(that._parameters))
    {
      return false;
    }
  
    if (_timeToCacheMS != that._timeToCacheMS)
    {
      return false;
    }
  
    if (_readExpired != that._readExpired)
    {
      return false;
    }
  
    return rawIsEquals(that);
  }

  public abstract Layer copy();


  public final String getTitle()
  {
    return _title;
  }

  public final void setTitle(String title)
  {
    _title = title;
  }

}