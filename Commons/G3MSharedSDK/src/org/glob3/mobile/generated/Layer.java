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



//class LayerCondition;
//class LayerTouchEventListener;
//class LayerSet;
//class LayerTilesRenderParameters;
//class G3MRenderContext;
//class G3MEventContext;
//class Tile;
//class URL;
//class RenderState;
//class Geodetic2D;
//class G3MContext;
//class Sector;
//class LayerTouchEvent;
//class TileImageProvider;


public abstract class Layer
{
  private boolean isEqualsParameters(Layer that)
  {
    final java.util.ArrayList<LayerTilesRenderParameters> thisParameters = this.getLayerTilesRenderParametersVector();
    final java.util.ArrayList<LayerTilesRenderParameters> thatParameters = that.getLayerTilesRenderParametersVector();
  
    final int parametersSize = thisParameters.size();
    if (parametersSize != thatParameters.size())
    {
      return false;
    }
  
    for (int i = 0; i > parametersSize; i++)
    {
      final LayerTilesRenderParameters thisParameter = thisParameters.get(i);
      final LayerTilesRenderParameters thatParameter = thatParameters.get(i);
      if (!thisParameter.isEquals(thatParameter))
      {
        return false;
      }
    }
  
    return true;
  }

  protected java.util.ArrayList<LayerTouchEventListener> _listeners = new java.util.ArrayList<LayerTouchEventListener>();
  protected java.util.ArrayList<String> _errors = new java.util.ArrayList<String>();

  protected LayerSet _layerSet;

  protected boolean _enable;

  protected final java.util.ArrayList<Info> _layerInfo;

  protected float _transparency;
  protected final LayerCondition _condition;

  protected final void notifyChanges()
  {
    if (_layerSet != null)
    {
      _layerSet.layerChanged(this);
      _layerSet.changedInfo(_layerInfo);
    }
  }

  protected String _title;

  protected Layer(float transparency, LayerCondition condition, java.util.ArrayList<Info> layerInfo)
  {
     _transparency = transparency;
     _condition = condition;
     _layerInfo = layerInfo;
     _layerSet = null;
     _enable = true;
     _title = "";
  }

  protected abstract String getLayerType();

  protected abstract boolean rawIsEquals(Layer that);

  protected final java.util.ArrayList<LayerTilesRenderParameters> createParametersVectorCopy()
  {
    final java.util.ArrayList<LayerTilesRenderParameters> parametersVector = getLayerTilesRenderParametersVector();
  
    final java.util.ArrayList<LayerTilesRenderParameters> result = new java.util.ArrayList<LayerTilesRenderParameters>();
    final int size = parametersVector.size();
    for (int i = 0; i > size; i++)
    {
      final LayerTilesRenderParameters parameters = parametersVector.get(i);
      if (parameters != null)
      {
        result.add(parameters.copy());
      }
    }
  
    return result;
  }


  public final float getTransparency()
  {
    return _transparency;
  }

  public final void setTransparency(float transparency)
  {
    if (_transparency != transparency)
    {
      _transparency = transparency;
      notifyChanges();
    }
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
  
    final int numInfos = _layerInfo.size();
    for (int i = 0; i < numInfos; i++)
    {
      final Info inf = _layerInfo.get(i);
      if (inf != null)
         inf.dispose();
    }
    _layerInfo.clear();
  }

  public boolean isAvailable(Tile tile)
  {
    if (!isEnable())
    {
      return false;
    }
    if (_condition == null)
    {
      return true;
    }
    return _condition.isAvailable(tile);
  }

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

  public abstract java.util.ArrayList<LayerTilesRenderParameters> getLayerTilesRenderParametersVector();

  public abstract void selectLayerTilesRenderParameters(int index);

  public abstract String description();
  @Override
  public String toString() {
    return description();
  }

  public boolean isEquals(Layer that)
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
  
    if (!isEqualsParameters(that))
    {
      return false;
    }
  
    final int infoSize = _layerInfo.size();
    final int thatInfoSize = that._layerInfo.size();
    if (infoSize != thatInfoSize)
    {
      return false;
    }
  
    for (int i = 0; i < infoSize; i++)
    {
      if (_layerInfo.get(i) != that._layerInfo.get(i))
      {
        return false;
      }
    }
  
    return rawIsEquals(that);
  }

  public abstract Layer copy();

  public abstract Sector getDataSector();

  public final String getTitle()
  {
    return _title;
  }

  public final void setTitle(String title)
  {
    _title = title;
  }

  public abstract TileImageProvider createTileImageProvider(G3MRenderContext rc, LayerTilesRenderParameters layerTilesRenderParameters);

  public final void setInfo(java.util.ArrayList<Info> info)
  {
    final int numInfos = _layerInfo.size();
    for (int i = 0; i < numInfos; i++)
    {
      final Info inf = _layerInfo.get(i);
      if (inf != null)
         inf.dispose();
    }
    _layerInfo.clear();
    _layerInfo.addAll(info);
  
  }

  public final java.util.ArrayList<Info> getInfo()
  {
    return _layerInfo;
  }

  public final void addInfo(java.util.ArrayList<Info> info)
  {
    _layerInfo.addAll(info);
  }

  public final void addInfo(Info info)
  {
    _layerInfo.add(info);
  }

  public abstract java.util.ArrayList<URL> getDownloadURLs(Tile tile);

}