package org.glob3.mobile.generated;import java.util.*;

//
//  LayerSet.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 23/07/12.
//

//
//  LayerSet.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 23/07/12.
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Layer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class ChangedListener;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class G3MContext;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TileImageProvider;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class G3MRenderContext;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class LayerTilesRenderParameters;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Tile;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class G3MEventContext;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Geodetic3D;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class RenderState;


public class LayerSet extends ChangedInfoListener
{
  private java.util.ArrayList<Layer> _layers = new java.util.ArrayList<Layer>();

  private ChangedListener _listener;

  private ChangedInfoListener _changedInfoListener;

  //  mutable LayerTilesRenderParameters* _layerTilesRenderParameters;
  private java.util.ArrayList<String> _errors = new java.util.ArrayList<String>();

  private final java.util.ArrayList<Info> _infos = new java.util.ArrayList<Info>();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void layersChanged() const
  private void layersChanged()
  {
	if (_tileImageProvider != null)
	{
	  _tileImageProvider._release();
	  _tileImageProvider = null;
	}
	if (_listener != null)
	{
	  _listener.changed();
	}
  }

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final G3MContext _context;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public G3MContext _context = new internal();
//#endif

  private TileImageProvider _tileImageProvider;

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: TileImageProvider* createTileImageProvider(const G3MRenderContext* rc, const LayerTilesRenderParameters* layerTilesRenderParameters) const
  private TileImageProvider createTileImageProvider(G3MRenderContext rc, LayerTilesRenderParameters layerTilesRenderParameters)
  {
	TileImageProvider singleTileImageProvider = null;
	CompositeTileImageProvider compositeTileImageProvider = null;
  
	final int layersSize = _layers.size();
	for (int i = 0; i < layersSize; i++)
	{
	  Layer layer = _layers.get(i);
	  if (layer.isEnable())
	  {
		TileImageProvider layerTileImageProvider = layer.createTileImageProvider(rc, layerTilesRenderParameters);
		if (layerTileImageProvider != null)
		{
		  if (compositeTileImageProvider != null)
		  {
			compositeTileImageProvider.addProvider(layerTileImageProvider);
		  }
		  else if (singleTileImageProvider == null)
		  {
			singleTileImageProvider = layerTileImageProvider;
		  }
		  else
		  {
			compositeTileImageProvider = new CompositeTileImageProvider();
			compositeTileImageProvider.addProvider(singleTileImageProvider);
			compositeTileImageProvider.addProvider(layerTileImageProvider);
		  }
		}
	  }
	}
  
	return (compositeTileImageProvider == null) ? singleTileImageProvider : compositeTileImageProvider;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean checkLayersDataSector(const boolean forceFirstLevelTilesRenderOnStart, java.util.ArrayList<String>& errors) const
  private boolean checkLayersDataSector(boolean forceFirstLevelTilesRenderOnStart, tangible.RefObject<java.util.ArrayList<String>> errors)
  {
  
	if (forceFirstLevelTilesRenderOnStart)
	{
	  Sector biggestDataSector = null;
  
	  final int layersCount = _layers.size();
	  double biggestArea = 0;
	  for (int i = 0; i < layersCount; i++)
	  {
		Layer layer = _layers.get(i);
		if (layer.isEnable())
		{
		  final double layerArea = layer.getDataSector().getAngularAreaInSquaredDegrees();
		  if (layerArea > biggestArea)
		  {
			if (biggestDataSector != null)
				biggestDataSector.dispose();
			biggestDataSector = new Sector(layer.getDataSector());
			biggestArea = layerArea;
		  }
		}
	  }
  
	  if (biggestDataSector != null)
	  {
		boolean dataSectorsInconsistency = false;
		for (int i = 0; i < layersCount; i++)
		{
		  Layer layer = _layers.get(i);
		  if (layer.isEnable())
		  {
			if (!biggestDataSector.fullContains(layer.getDataSector()))
			{
			  dataSectorsInconsistency = true;
			  break;
			}
		  }
		}
  
		if (biggestDataSector != null)
			biggestDataSector.dispose();
  
		if (dataSectorsInconsistency)
		{
		  errors.argvalue.add("Inconsistency in layers data sectors");
		  return false;
		}
	  }
	}
  
	return true;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean checkLayersRenderState(java.util.ArrayList<String>& errors, java.util.ArrayList<Layer*>& enableLayers) const
  private boolean checkLayersRenderState(tangible.RefObject<java.util.ArrayList<String>> errors, tangible.RefObject<java.util.ArrayList<Layer>> enableLayers)
  {
	boolean layerSetNotReadyFlag = false;
	for (int i = 0; i < _layers.size(); i++)
	{
	  Layer layer = _layers.get(i);
  
	  if (layer.isEnable())
	  {
		enableLayers.argvalue.add(layer);
  
		final RenderState layerRenderState = layer.getRenderState();
		final RenderState_Type layerRenderStateType = layerRenderState._type;
		if (layerRenderStateType != RenderState_Type.RENDER_READY)
		{
		  if (layerRenderStateType == RenderState_Type.RENDER_ERROR)
		  {
			final java.util.ArrayList<String> layerErrors = layerRenderState.getErrors();
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
//C++ TO JAVA CONVERTER TODO TASK: There is no direct equivalent to the STL vector 'insert' method in Java:
			errors.argvalue.insert(errors.argvalue.end(), layerErrors.iterator(), layerErrors.end());
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
			errors.argvalue.addAll(layerErrors);
//#endif
		  }
		  layerSetNotReadyFlag = true;
		}
	  }
	}
  
	return !layerSetNotReadyFlag;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: LayerTilesRenderParameters* checkAndComposeLayerTilesRenderParameters(const boolean forceFirstLevelTilesRenderOnStart, const java.util.ArrayList<Layer*>& enableLayers, java.util.ArrayList<String>& errors) const
  private LayerTilesRenderParameters checkAndComposeLayerTilesRenderParameters(boolean forceFirstLevelTilesRenderOnStart, java.util.ArrayList<Layer> enableLayers, tangible.RefObject<java.util.ArrayList<String>> errors)
  {
  
	MutableLayerTilesRenderParameters mutableLayerTilesRenderParameters = new MutableLayerTilesRenderParameters();
  
	java.util.ArrayList<Layer> multiProjectionLayers = new java.util.ArrayList<Layer>();
  
	for (int i = 0; i < enableLayers.size(); i++)
	{
	  Layer layer = enableLayers.get(i);
  
	  final java.util.ArrayList<const LayerTilesRenderParameters> layerParametersVector = layer.getLayerTilesRenderParametersVector();
  
	  final int layerParametersVectorSize = layerParametersVector.size();
	  if (layerParametersVectorSize == 0)
	  {
		continue;
	  }
	  else if (layerParametersVectorSize == 1)
	  {
		if (!mutableLayerTilesRenderParameters.update(layerParametersVector.get(0), errors))
		{
		  return null;
		}
	  }
	  else
	  {
		multiProjectionLayers.add(layer);
	  }
	}
  
	for (int i = 0; i < multiProjectionLayers.size(); i++)
	{
	  Layer layer = multiProjectionLayers.get(i);
	  if (!mutableLayerTilesRenderParameters.update(layer, errors))
	  {
		return null;
	  }
	}
  
	return mutableLayerTilesRenderParameters.create(errors);
  }

  public LayerSet()
  //  _layerTilesRenderParameters(NULL),
  {
	  _listener = null;
	  _context = null;
	  _tileImageProvider = null;
	  _changedInfoListener = null;
  }

  public void dispose()
  {
	for (int i = 0; i < _layers.size(); i++)
	{
	  if (_layers.get(i) != null)
		  _layers.get(i).dispose();
	}
  
	if (_tileImageProvider != null)
	{
	  _tileImageProvider._release();
	}
  }

  public final void removeAllLayers(boolean deleteLayers)
  {
	final int layersSize = _layers.size();
	if (layersSize > 0)
	{
	  for (int i = 0; i < layersSize; i++)
	  {
		Layer layer = _layers.get(i);
		layer.removeLayerSet(this);
		if (deleteLayers)
		{
		  if (layer != null)
			  layer.dispose();
		}
	  }
	  _layers.clear();
  
	  layersChanged();
	  changedInfo(getInfo());
	}
  }

  public final void addLayer(Layer layer)
  {
	layer.setLayerSet(this);
	_layers.add(layer);
  
	if (_context != null)
	{
	  layer.initialize(_context);
	}
  
	layersChanged();
	changedInfo(layer.getInfo());
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean onTerrainTouchEvent(const G3MEventContext* ec, const Geodetic3D& position, const Tile* tile) const
  public final boolean onTerrainTouchEvent(G3MEventContext ec, Geodetic3D position, Tile tile)
  {
  
	for (int i = _layers.size()-1; i >= 0; i--)
	{
	  Layer layer = _layers.get(i);
	  if (layer.isAvailable(tile))
	  {
		LayerTouchEvent tte = new LayerTouchEvent(position, tile._sector, layer);
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: if (layer->onLayerTouchEventListener(ec, tte))
		if (layer.onLayerTouchEventListener(ec, new LayerTouchEvent(tte)))
		{
		  return true;
		}
	  }
	}
  
	return false;
  }

  public final RenderState getRenderState()
  {
	_errors.clear();
	boolean busyFlag = false;
	boolean errorFlag = false;
	final int layersCount = _layers.size();
  
	for (int i = 0; i < layersCount; i++)
	{
	  Layer child = _layers.get(i);
	  if (child.isEnable())
	  {
		final RenderState childRenderState = child.getRenderState();
  
		final RenderState_Type childRenderStateType = childRenderState._type;
  
		if (childRenderStateType == RenderState_Type.RENDER_ERROR)
		{
		  errorFlag = true;
  
		  final java.util.ArrayList<String> childErrors = childRenderState.getErrors();
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
//C++ TO JAVA CONVERTER TODO TASK: There is no direct equivalent to the STL vector 'insert' method in Java:
		  _errors.insert(_errors.end(), childErrors.iterator(), childErrors.end());
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
		  _errors.addAll(childErrors);
//#endif
		}
		else if (childRenderStateType == RenderState_Type.RENDER_BUSY)
		{
		  busyFlag = true;
		}
	  }
	}
  
	if (errorFlag)
	{
	  return RenderState.error(_errors);
	}
	else if (busyFlag)
	{
	  return RenderState.busy();
	}
	return RenderState.ready();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void initialize(const G3MContext* context) const
  public final void initialize(G3MContext context)
  {
	_context = context;
  
	final int layersCount = _layers.size();
	for (int i = 0; i < layersCount; i++)
	{
	  _layers.get(i).initialize(context);
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int size() const
  public final int size()
  {
	return _layers.size();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void layerChanged(const Layer* layer) const
  public final void layerChanged(Layer layer)
  {
	layersChanged();
  }

  public final void setChangeListener(ChangedListener listener)
  {
	if (_listener != null)
	{
	  ILogger.instance().logError("Listener already set");
	}
	_listener = listener;
  }

  public final void setTileImageProvider(TileImageProvider tileImageProvider)
  {
	if (_tileImageProvider != null)
	{
	  ILogger.instance().logError("TileImageProvider already set");
	}
	_tileImageProvider = tileImageProvider;
  }


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Layer* getLayer(int index) const
  public final Layer getLayer(int index)
  {
	if (index < _layers.size())
	{
	  return _layers.get(index);
	}
  
	return null;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Layer* getLayerByTitle(const String& title) const
  public final Layer getLayerByTitle(String title)
  {
	final int layersCount = _layers.size();
	for (int i = 0; i < layersCount; i++)
	{
	  if (title.equals(_layers.get(i).getTitle()))
	  {
		return _layers.get(i);
	  }
	}
	return null;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: LayerTilesRenderParameters* createLayerTilesRenderParameters(const boolean forceFirstLevelTilesRenderOnStart, java.util.ArrayList<String>& errors) const
  public final LayerTilesRenderParameters createLayerTilesRenderParameters(boolean forceFirstLevelTilesRenderOnStart, tangible.RefObject<java.util.ArrayList<String>> errors)
  {
  
	if (!checkLayersDataSector(forceFirstLevelTilesRenderOnStart, errors))
	{
	  return null;
	}
  
	java.util.ArrayList<Layer> enableLayers = new java.util.ArrayList<Layer>();
	tangible.RefObject<java.util.ArrayList<Layer>> tempRef_enableLayers = new tangible.RefObject<java.util.ArrayList<Layer>>(enableLayers);
	boolean tempVar = !checkLayersRenderState(errors, tempRef_enableLayers);
	enableLayers = tempRef_enableLayers.argvalue;
	if (tempVar)
	{
	  return null;
	}
  
	return checkAndComposeLayerTilesRenderParameters(forceFirstLevelTilesRenderOnStart, enableLayers, errors);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isEquals(const LayerSet* that) const
  public final boolean isEquals(LayerSet that)
  {
	if (that == null)
	{
	  return false;
	}
  
	final int thisSize = size();
	final int thatSize = that.size();
  
	if (thisSize != thatSize)
	{
	  return false;
	}
  
	for (int i = 0; i < thisSize; i++)
	{
	  Layer thisLayer = getLayer(i);
	  Layer thatLayer = that.getLayer(i);
  
	  if (!thisLayer.isEquals(thatLayer))
	  {
		return false;
	  }
	}
  
	return true;
  }

  public final void takeLayersFrom(LayerSet that)
  {
	if (that == null)
	{
	  return;
	}
  
	java.util.ArrayList<Layer> thatLayers = new java.util.ArrayList<Layer>();
	final int thatSize = that.size();
	for (int i = 0; i < thatSize; i++)
	{
	  thatLayers.add(that.getLayer(i));
	}
  
	that.removeAllLayers(false);
  
	for (int i = 0; i < thatSize; i++)
	{
	  addLayer(thatLayers.get(i));
	}
  }

  public final void disableAllLayers()
  {
	final int layersCount = _layers.size();
	for (int i = 0; i < layersCount; i++)
	{
	  _layers.get(i).setEnable(false);
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: TileImageProvider* getTileImageProvider(const G3MRenderContext* rc, const LayerTilesRenderParameters* layerTilesRenderParameters) const
  public final TileImageProvider getTileImageProvider(G3MRenderContext rc, LayerTilesRenderParameters layerTilesRenderParameters)
  {
	if (_tileImageProvider == null)
	{
	  _tileImageProvider = createTileImageProvider(rc, layerTilesRenderParameters);
	}
	return _tileImageProvider;
  }


  public final void setChangedInfoListener(ChangedInfoListener changedInfoListener)
  {
	if (_changedInfoListener != null)
	{
	  ILogger.instance().logError("Changed Info Listener of LayerSet already set");
	  return;
	}
	_changedInfoListener = changedInfoListener;
	if (_changedInfoListener != null)
	{
	  _changedInfoListener.changedInfo(getInfo());
	}
  }

  public final java.util.ArrayList<Info> getInfo()
  {
	_infos.clear();
	final int layersCount = _layers.size();
	for (int i = 0; i < layersCount; i++)
	{
	  Layer layer = _layers.get(i);
	  if (layer.isEnable())
	  {
		final java.util.ArrayList<const Info> layerInfo = layer.getInfo();
		final int infoSize = layerInfo.size();
		for (int j = 0; j < infoSize; j++)
		{
		  _infos.add(layerInfo.get(j));
		}
	  }
	}
	return _infos;
  }

  public final void changedInfo(java.util.ArrayList<const Info> info)
  {
	if (_changedInfoListener != null)
	{
	  _changedInfoListener.changedInfo(getInfo());
	}
  }

}
