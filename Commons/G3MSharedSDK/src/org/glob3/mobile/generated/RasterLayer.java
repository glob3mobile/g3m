package org.glob3.mobile.generated;import java.util.*;

//
//  RasterLayer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/22/14.
//
//

//
//  RasterLayer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/22/14.
//
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IDownloader;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IImageDownloadListener;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class RasterLayerTileImageProvider;

public abstract class RasterLayer extends Layer
{
  private RasterLayerTileImageProvider _tileImageProvider;

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  protected final LayerTilesRenderParameters _parameters;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  protected LayerTilesRenderParameters _parameters = new protected();
//#endif

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  protected final TimeInterval _timeToCache = new TimeInterval();
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  protected final TimeInterval _timeToCache = new protected();
//#endif
  protected final boolean _readExpired;

  protected RasterLayer(TimeInterval timeToCache, boolean readExpired, LayerTilesRenderParameters parameters, float transparency, LayerCondition condition, java.util.ArrayList<Info> layerInfo)
  {
	  super(transparency, condition, layerInfo);
	  _timeToCache = new TimeInterval(timeToCache);
	  _readExpired = readExpired;
	  _parameters = parameters;
	  _tileImageProvider = null;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const TimeInterval getTimeToCache() const
  protected final TimeInterval getTimeToCache()
  {
	return _timeToCache;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean getReadExpired() const
  protected final boolean getReadExpired()
  {
	return _readExpired;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual const TileImageContribution* rawContribution(const Tile* tile) const = 0;
  protected abstract TileImageContribution rawContribution(Tile tile);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual const URL createURL(const Tile* tile) const = 0;
  protected abstract URL createURL(Tile tile);

  protected final void setParameters(LayerTilesRenderParameters parameters)
  {
	if (_parameters != parameters)
	{
	  if (_parameters != null)
		  _parameters.dispose();
	  _parameters = parameters;
	  notifyChanges();
	}
  }

  public void dispose()
  {
	if (_parameters != null)
		_parameters.dispose();
	if (_tileImageProvider != null)
	{
	  _tileImageProvider.layerDeleted(this);
	  _tileImageProvider._release();
	}
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Tile* getParentTileOfSuitableLevel(const Tile* tile) const
  protected final Tile getParentTileOfSuitableLevel(Tile tile)
  {
	final int maxLevel = _parameters._maxLevel;
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	final Tile result = tile;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: Tile result = tile;
	Tile result = new Tile(tile);
//#endif
	while ((result != null) && (result._level > maxLevel))
	{
	  result = result.getParent();
	}
	return result;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const java.util.ArrayList<const LayerTilesRenderParameters*> getLayerTilesRenderParametersVector() const
  public final java.util.ArrayList<LayerTilesRenderParameters> getLayerTilesRenderParametersVector()
  {
	final java.util.ArrayList<LayerTilesRenderParameters> parametersVector = new java.util.ArrayList<LayerTilesRenderParameters>();
	if (_parameters != null)
	{
	  parametersVector.add(_parameters);
	}
	return parametersVector;
  }

  public final void selectLayerTilesRenderParameters(int index)
  {
	THROW_EXCEPTION("Logic error");
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isEquals(const Layer* that) const
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
  
	if (!super.isEquals(that))
	{
	  return false;
	}
  
	RasterLayer rasterThat = (RasterLayer) that;
  
	return ((_timeToCache.milliseconds() == rasterThat._timeToCache.milliseconds()) && (_readExpired == rasterThat._readExpired));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: TileImageProvider* createTileImageProvider(const G3MRenderContext* rc, const LayerTilesRenderParameters* layerTilesRenderParameters) const
  public final TileImageProvider createTileImageProvider(G3MRenderContext rc, LayerTilesRenderParameters layerTilesRenderParameters)
  {
	if (_tileImageProvider == null)
	{
	  _tileImageProvider = new RasterLayerTileImageProvider(this, rc.getDownloader());
	}
	_tileImageProvider._retain();
	return _tileImageProvider;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const TileImageContribution* contribution(const Tile* tile) const
  public final TileImageContribution contribution(Tile tile)
  {
	if ((_condition == null) || _condition.isAvailable(tile))
	{
	  return rawContribution(tile);
	}
	return null;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: long requestImage(const Tile* tile, IDownloader* downloader, long tileDownloadPriority, boolean logDownloadActivity, IImageDownloadListener* listener, boolean deleteListener) const
  public final long requestImage(Tile tile, IDownloader downloader, long tileDownloadPriority, boolean logDownloadActivity, IImageDownloadListener listener, boolean deleteListener)
  {
	final Tile suitableTile = getParentTileOfSuitableLevel(tile);
  
	final URL url = createURL(suitableTile);
	if (logDownloadActivity)
	{
	  ILogger.instance().logInfo("Downloading %s", url._path.c_str());
	}
	return downloader.requestImage(url, tileDownloadPriority, _timeToCache, _readExpired, listener, deleteListener);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const java.util.ArrayList<URL*> getDownloadURLs(const Tile* tile) const
  public final java.util.ArrayList<URL> getDownloadURLs(Tile tile)
  {
	java.util.ArrayList<URL> result = new java.util.ArrayList<URL>();
	result.add(new URL(createURL(tile)));
	return result;
  }

}
