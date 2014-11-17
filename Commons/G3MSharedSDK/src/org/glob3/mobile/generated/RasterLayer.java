package org.glob3.mobile.generated; 
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



//class IDownloader;
//class IImageDownloadListener;
//class RasterLayerTileImageProvider;

public abstract class RasterLayer extends Layer
{
  private RasterLayerTileImageProvider _tileImageProvider;

  protected LayerTilesRenderParameters _parameters;

  protected final TimeInterval _timeToCache;
  protected final boolean _readExpired;

  protected RasterLayer(TimeInterval timeToCache, boolean readExpired, LayerTilesRenderParameters parameters, float transparency, LayerCondition condition, java.util.ArrayList<Info> layerInfo)
  {
     super(transparency, condition, layerInfo);
     _timeToCache = timeToCache;
     _readExpired = readExpired;
     _parameters = parameters;
     _tileImageProvider = null;
  }

  protected final TimeInterval getTimeToCache()
  {
    return _timeToCache;
  }

  protected final boolean getReadExpired()
  {
    return _readExpired;
  }

  protected abstract TileImageContribution rawContribution(Tile tile);

  protected abstract URL createURL(Tile tile);

  protected final void setParameters(LayerTilesRenderParameters parameters)
  {
    if (_parameters != parameters)
    {
      _parameters = null;
      _parameters = parameters;
      notifyChanges();
    }
  }

  public void dispose()
  {
    _parameters = null;
    if (_tileImageProvider != null)
    {
      _tileImageProvider.layerDeleted(this);
      _tileImageProvider._release();
    }
    super.dispose();
  }

  protected final Tile getParentTileOfSuitableLevel(Tile tile)
  {
    final int maxLevel = _parameters._maxLevel;
    Tile result = tile;
    while ((result != null) && (result._level > maxLevel))
    {
      result = result.getParent();
    }
    return result;
  }

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
    throw new RuntimeException("Logic error");
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
  
    if (!super.isEquals(that))
    {
      return false;
    }
  
    RasterLayer rasterThat = (RasterLayer) that;
  
    return ((_timeToCache.milliseconds() == rasterThat._timeToCache.milliseconds()) && (_readExpired == rasterThat._readExpired));
  }

  public final TileImageProvider createTileImageProvider(G3MRenderContext rc, LayerTilesRenderParameters layerTilesRenderParameters)
  {
    if (_tileImageProvider == null)
    {
      _tileImageProvider = new RasterLayerTileImageProvider(this, rc.getDownloader());
    }
    _tileImageProvider._retain();
    return _tileImageProvider;
  }

  public final TileImageContribution contribution(Tile tile)
  {
    if ((_condition == null) || _condition.isAvailable(tile))
    {
      return rawContribution(tile);
    }
    return null;
  }

  public final long requestImage(Tile tile, IDownloader downloader, long tileDownloadPriority, boolean logDownloadActivity, IImageDownloadListener listener, boolean deleteListener)
  {
    final Tile suitableTile = getParentTileOfSuitableLevel(tile);
  
    final URL url = createURL(suitableTile);
    if (logDownloadActivity)
    {
      ILogger.instance().logInfo("Downloading %s", url._path);
    }
    return downloader.requestImage(url, tileDownloadPriority, _timeToCache, _readExpired, listener, deleteListener);
  }

  public final java.util.ArrayList<URL> getDownloadURLs(Tile tile)
  {
    java.util.ArrayList<URL> result = new java.util.ArrayList<URL>();
    result.add(new URL(createURL(tile)));
    return result;
  }

}