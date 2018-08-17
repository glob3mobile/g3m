package org.glob3.mobile.generated;import java.util.*;

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
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class LayerCondition;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class LayerTouchEventListener;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class LayerSet;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class LayerTilesRenderParameters;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class G3MRenderContext;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class G3MEventContext;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Tile;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class URL;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class RenderState;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Geodetic2D;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class G3MContext;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Sector;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class LayerTouchEvent;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TileImageProvider;


public abstract class Layer
{
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isEqualsParameters(const Layer* that) const
  private boolean isEqualsParameters(Layer that)
  {
	final java.util.ArrayList<const LayerTilesRenderParameters> thisParameters = this.getLayerTilesRenderParametersVector();
	final java.util.ArrayList<const LayerTilesRenderParameters> thatParameters = that.getLayerTilesRenderParametersVector();
  
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void notifyChanges() const
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual String getLayerType() const = 0;
  protected abstract String getLayerType();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean rawIsEquals(const Layer* that) const = 0;
  protected abstract boolean rawIsEquals(Layer that);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const java.util.ArrayList<const LayerTilesRenderParameters*> createParametersVectorCopy() const
  protected final java.util.ArrayList<LayerTilesRenderParameters> createParametersVectorCopy()
  {
	final java.util.ArrayList<const LayerTilesRenderParameters> parametersVector = getLayerTilesRenderParametersVector();
  
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


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const float getTransparency() const
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean isEnable() const
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
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	_layerInfo = null;
//#endif
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean isAvailable(const Tile* tile) const
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual URL getFeatureInfoURL(const Geodetic2D& position, const Sector& sector) const = 0;
  public abstract URL getFeatureInfoURL(Geodetic2D position, Sector sector);

  public abstract RenderState getRenderState();

  public void initialize(G3MContext context)
  {
  }

  public final void addLayerTouchEventListener(LayerTouchEventListener listener)
  {
	_listeners.add(listener);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean onLayerTouchEventListener(const G3MEventContext* ec, const LayerTouchEvent& tte) const
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual const java.util.ArrayList<const LayerTilesRenderParameters*> getLayerTilesRenderParametersVector() const = 0;
  public abstract java.util.ArrayList<LayerTilesRenderParameters> getLayerTilesRenderParametersVector();

  public abstract void selectLayerTilesRenderParameters(int index);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual const String description() const = 0;
  public abstract String description();
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public final Override public String toString()
  {
	return description();
  }
//#endif

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean isEquals(const Layer* that) const
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual Layer* copy() const = 0;
  public abstract Layer copy();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual const Sector getDataSector() const = 0;
  public abstract Sector getDataSector();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String getTitle() const
  public final String getTitle()
  {
	return _title;
  }

  public final void setTitle(String title)
  {
	_title = title;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual TileImageProvider* createTileImageProvider(const G3MRenderContext* rc, const LayerTilesRenderParameters* layerTilesRenderParameters) const = 0;
  public abstract TileImageProvider createTileImageProvider(G3MRenderContext rc, LayerTilesRenderParameters layerTilesRenderParameters);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void setInfo(const java.util.ArrayList<const Info*>& info) const
  public final void setInfo(java.util.ArrayList<const Info> info)
  {
	final int numInfos = _layerInfo.size();
	for (int i = 0; i < numInfos; i++)
	{
	  final Info inf = _layerInfo.get(i);
	  if (inf != null)
		  inf.dispose();
	}
	_layerInfo.clear();
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
//C++ TO JAVA CONVERTER TODO TASK: There is no direct equivalent to the STL vector 'insert' method in Java:
	_layerInfo.insert(_layerInfo.end(), info.iterator(), info.end());
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	_layerInfo.addAll(info);
//#endif
  
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const java.util.ArrayList<const Info*>& getInfo() const
  public final java.util.ArrayList<Info> getInfo()
  {
	return _layerInfo;
  }

  public final void addInfo(java.util.ArrayList<const Info> info)
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
//C++ TO JAVA CONVERTER TODO TASK: There is no direct equivalent to the STL vector 'insert' method in Java:
	_layerInfo.insert(_layerInfo.end(), info.iterator(), info.end());
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	_layerInfo.addAll(info);
//#endif
  }

  public final void addInfo(Info info)
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
//C++ TO JAVA CONVERTER TODO TASK: There is no direct equivalent to the STL vector 'insert' method in Java:
	_layerInfo.insert(_layerInfo.end(), info);
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	_layerInfo.add(info);
//#endif
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual const java.util.ArrayList<URL*> getDownloadURLs(const Tile* tile) const = 0;
  public abstract java.util.ArrayList<URL> getDownloadURLs(Tile tile);

}
