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



//class TimeInterval;
//class IDownloader;
//class IImageDownloadListener;

public abstract class RasterLayer extends Layer
{
  protected final TimeInterval _timeToCache = new TimeInterval();
  protected final boolean _readExpired;

  protected RasterLayer(TimeInterval timeToCache, boolean readExpired, LayerTilesRenderParameters parameters, float transparency, LayerCondition condition)
  {
     super(parameters, transparency, condition);
     _timeToCache = new TimeInterval(timeToCache);
     _readExpired = readExpired;
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
    return new RasterLayerTileImageProvider(this, rc.getDownloader());
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
    final URL url = createURL(tile);
    if (logDownloadActivity)
    {
      ILogger.instance().logInfo("Downloading %s", url._path);
    }
    return downloader.requestImage(url, tileDownloadPriority, _timeToCache, _readExpired, listener, deleteListener);
  }

}