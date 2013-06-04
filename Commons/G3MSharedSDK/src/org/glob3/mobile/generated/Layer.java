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
  private LayerCondition _condition;
  private java.util.ArrayList<TerrainTouchEventListener> _listeners = new java.util.ArrayList<TerrainTouchEventListener>();

  private LayerSet _layerSet;

  private boolean _enable;

  private final String _name;

  protected LayerTilesRenderParameters _parameters;

  protected final long _timeToCacheMS;
  protected final boolean _readExpired;

  protected final void notifyChanges()
  {
    if (_layerSet == null)
    {
  //    ILogger::instance()->logError("Can't notify changes, _layerSet was not set");
    }
    else
    {
      _layerSet.layerChanged(this);
    }
  }

  protected Layer(LayerCondition condition, String name, TimeInterval timeToCache, boolean readExpired, LayerTilesRenderParameters parameters)
  {
     _condition = condition;
     _name = name;
     _layerSet = null;
     _timeToCacheMS = timeToCache.milliseconds();
     _readExpired = readExpired;
     _enable = true;
     _parameters = parameters;

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

  public abstract java.util.ArrayList<Petition> createTileMapPetitions(G3MRenderContext rc, Tile tile);

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

  public boolean isReady()
  {
    return true;
  }

  public void initialize(G3MContext context)
  {
  }

  public final void addTerrainTouchEventListener(TerrainTouchEventListener listener)
  {
    _listeners.add(listener);
  }

  public final boolean onTerrainTouchEventListener(G3MEventContext ec, TerrainTouchEvent tte)
  {
    final int listenersSize = _listeners.size();
    for (int i = 0; i < listenersSize; i++)
    {
      TerrainTouchEventListener listener = _listeners.get(i);
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

}