package org.glob3.mobile.generated; 
//
//  TiledVectorLayer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/30/14.
//
//

//
//  TiledVectorLayer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/30/14.
//
//


//class TileImageContribution;
//class IDownloader;
//class IBufferDownloadListener;
//class IStringUtils;
//class GEORasterSymbolizer;

public class TiledVectorLayer extends VectorLayer
{
  private final GEORasterSymbolizer _symbolizer;
  private final String _urlTemplate;
  private final Sector _sector ;
  private final TimeInterval _timeToCache;
  private final boolean _readExpired;

  private IMathUtils   _mu;
  private IStringUtils _su;

  private TiledVectorLayer(GEORasterSymbolizer symbolizer, String urlTemplate, Sector sector, LayerTilesRenderParameters parameters, TimeInterval timeToCache, boolean readExpired, float transparency, LayerCondition condition)
  {
     super(parameters, transparency, condition);
     _symbolizer = symbolizer;
     _urlTemplate = urlTemplate;
     _sector = new Sector(sector);
     _timeToCache = timeToCache;
     _readExpired = readExpired;
     _su = null;
     _mu = null;
  }

  private URL createURL(Tile tile)
  {
  
    if (_mu == null)
    {
      _mu = IMathUtils.instance();
    }
  
    if (_su == null)
    {
      _su = IStringUtils.instance();
    }
  
    final Sector sector = tile._sector;
  
    final Vector2I tileTextureResolution = _parameters._tileTextureResolution;
  
    final int level = tile._level;
    final int column = tile._column;
    final int numRows = (int)(_parameters._topSectorSplitsByLatitude * _mu.pow(2.0, level));
    final int row = numRows - tile._row - 1;
  
    final double north = MercatorUtils.latitudeToMeters(sector._upper._latitude);
    final double south = MercatorUtils.latitudeToMeters(sector._lower._latitude);
    final double east = MercatorUtils.longitudeToMeters(sector._upper._longitude);
    final double west = MercatorUtils.longitudeToMeters(sector._lower._longitude);
  
    String path = _urlTemplate;
    path = _su.replaceSubstring(path, "{width}", _su.toString(tileTextureResolution._x));
    path = _su.replaceSubstring(path, "{height}", _su.toString(tileTextureResolution._y));
    path = _su.replaceSubstring(path, "{x}", _su.toString(column));
    path = _su.replaceSubstring(path, "{y}", _su.toString(row));
    path = _su.replaceSubstring(path, "{y2}", _su.toString(tile._row));
    path = _su.replaceSubstring(path, "{level}", _su.toString(level));
    path = _su.replaceSubstring(path, "{lowerLatitude}", _su.toString(sector._lower._latitude._degrees));
    path = _su.replaceSubstring(path, "{lowerLongitude}", _su.toString(sector._lower._longitude._degrees));
    path = _su.replaceSubstring(path, "{upperLatitude}", _su.toString(sector._upper._latitude._degrees));
    path = _su.replaceSubstring(path, "{upperLongitude}", _su.toString(sector._upper._longitude._degrees));
    path = _su.replaceSubstring(path, "{north}", _su.toString(north));
    path = _su.replaceSubstring(path, "{south}", _su.toString(south));
    path = _su.replaceSubstring(path, "{west}", _su.toString(west));
    path = _su.replaceSubstring(path, "{east}", _su.toString(east));
  
    return new URL(path, false);
  }

  protected final String getLayerType()
  {
    return "TiledVectorLayer";
  }

  protected final boolean rawIsEquals(Layer that)
  {
    final TiledVectorLayer t = (TiledVectorLayer) that;
  
    if (!_urlTemplate.equals(t._urlTemplate))
    {
      return false;
    }
  
    return _sector.isEquals(t._sector);
  }


  public static TiledVectorLayer newMercator(GEORasterSymbolizer symbolizer, String urlTemplate, Sector sector, int firstLevel, int maxLevel, TimeInterval timeToCache, boolean readExpired, float transparency)
  {
     return newMercator(symbolizer, urlTemplate, sector, firstLevel, maxLevel, timeToCache, readExpired, transparency, null);
  }
  public static TiledVectorLayer newMercator(GEORasterSymbolizer symbolizer, String urlTemplate, Sector sector, int firstLevel, int maxLevel, TimeInterval timeToCache, boolean readExpired)
  {
     return newMercator(symbolizer, urlTemplate, sector, firstLevel, maxLevel, timeToCache, readExpired, 1, null);
  }
  public static TiledVectorLayer newMercator(GEORasterSymbolizer symbolizer, String urlTemplate, Sector sector, int firstLevel, int maxLevel, TimeInterval timeToCache, boolean readExpired, float transparency, LayerCondition condition)
  {
    return new TiledVectorLayer(symbolizer, urlTemplate, sector, LayerTilesRenderParameters.createDefaultMercator(firstLevel, maxLevel), timeToCache, readExpired, transparency, condition);
  }

  public void dispose()
  {
    if (_symbolizer != null)
       _symbolizer.dispose();
    super.dispose();
  }

  public final URL getFeatureInfoURL(Geodetic2D position, Sector sector)
  {
    return new URL();
  }

  public final RenderState getRenderState()
  {
    return RenderState.ready();
  }

  public final String description()
  {
    return "[TiledVectorLayer urlTemplate=\"" + _urlTemplate + "\"]";
  }

  public final TiledVectorLayer copy()
  {
    return new TiledVectorLayer(_symbolizer.copy(), _urlTemplate, _sector, _parameters.copy(), _timeToCache, _readExpired, _transparency, (_condition == null) ? null : _condition.copy());
  }

  public final TileImageContribution contribution(Tile tile)
  {
    if ((_condition == null) || _condition.isAvailable(tile))
    {
      return (_sector.touchesWith(tile._sector) ? TileImageContribution.fullCoverageTransparent(_transparency) : null);
    }
    return null;
  }

  public final java.util.ArrayList<Petition> createTileMapPetitions(G3MRenderContext rc, LayerTilesRenderParameters layerTilesRenderParameters, Tile tile)
  {
    java.util.ArrayList<Petition> petitions = new java.util.ArrayList<Petition>();
    return petitions;
  }

  public final TileImageProvider createTileImageProvider(G3MRenderContext rc, LayerTilesRenderParameters layerTilesRenderParameters)
  {
    return new TiledVectorLayerTileImageProvider(this, rc.getDownloader(), rc.getThreadUtils());
  }

  public final long requestGEOJSONBuffer(Tile tile, IDownloader downloader, long tileDownloadPriority, boolean logDownloadActivity, IBufferDownloadListener listener, boolean deleteListener)
  {
    final URL url = createURL(tile);
    if (logDownloadActivity)
    {
      ILogger.instance().logInfo("Downloading %s", url._path);
    }
    return downloader.requestBuffer(url, tileDownloadPriority, _timeToCache, _readExpired, listener, deleteListener);
  }

  public final GEORasterSymbolizer symbolizerCopy()
  {
    return _symbolizer.copy();
  }
}