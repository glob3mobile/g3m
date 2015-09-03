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
//class TiledVectorLayerTileImageProvider;

public class TiledVectorLayer extends VectorLayer
{
  private GEORasterSymbolizer _symbolizer;
  private final String _urlTemplate;
  private final Sector _dataSector ;
  private final TimeInterval _timeToCache;
  private final boolean _readExpired;

  private IMathUtils   _mu;
  private IStringUtils _su;
  private TiledVectorLayerTileImageProvider _tileImageProvider;

  private TiledVectorLayer(GEORasterSymbolizer symbolizer, String urlTemplate, Sector dataSector, java.util.ArrayList<LayerTilesRenderParameters> parametersVector, TimeInterval timeToCache, boolean readExpired, float transparency, LayerCondition condition, java.util.ArrayList<Info> layerInfo)
  {
     super(parametersVector, transparency, condition, layerInfo);
     _symbolizer = symbolizer;
     _urlTemplate = urlTemplate;
     _dataSector = new Sector(dataSector);
     _timeToCache = timeToCache;
     _readExpired = readExpired;
     _tileImageProvider = null;
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
  
  
    final LayerTilesRenderParameters parameters = _parametersVector.get(_selectedLayerTilesRenderParametersIndex);
  
    final Vector2I tileTextureResolution = parameters._tileTextureResolution;
  
    final int level = tile._level;
    final int column = tile._column;
    final int numRows = (int)(parameters._topSectorSplitsByLatitude * _mu.pow(2.0, level));
    final int row = numRows - tile._row - 1;
  
    final double north = MercatorUtils.latitudeToMeters(sector._upper._latitude);
    final double south = MercatorUtils.latitudeToMeters(sector._lower._latitude);
    final double east = MercatorUtils.longitudeToMeters(sector._upper._longitude);
    final double west = MercatorUtils.longitudeToMeters(sector._lower._longitude);
  
    String path = _urlTemplate;
    path = _su.replaceAll(path, "{width}", _su.toString(tileTextureResolution._x));
    path = _su.replaceAll(path, "{height}", _su.toString(tileTextureResolution._y));
    path = _su.replaceAll(path, "{x}", _su.toString(column));
    path = _su.replaceAll(path, "{y}", _su.toString(row));
    path = _su.replaceAll(path, "{y2}", _su.toString(tile._row));
    path = _su.replaceAll(path, "{level}", _su.toString(level));
    path = _su.replaceAll(path, "{lowerLatitude}", _su.toString(sector._lower._latitude._degrees));
    path = _su.replaceAll(path, "{lowerLongitude}", _su.toString(sector._lower._longitude._degrees));
    path = _su.replaceAll(path, "{upperLatitude}", _su.toString(sector._upper._latitude._degrees));
    path = _su.replaceAll(path, "{upperLongitude}", _su.toString(sector._upper._longitude._degrees));
    path = _su.replaceAll(path, "{north}", _su.toString(north));
    path = _su.replaceAll(path, "{south}", _su.toString(south));
    path = _su.replaceAll(path, "{west}", _su.toString(west));
    path = _su.replaceAll(path, "{east}", _su.toString(east));
  
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
  
    return _dataSector.isEquals(t._dataSector);
  }



  public static TiledVectorLayer newMercator(GEORasterSymbolizer symbolizer, String urlTemplate, Sector dataSector, int firstLevel, int maxLevel, TimeInterval timeToCache, boolean readExpired, float transparency, LayerCondition condition)
  {
     return newMercator(symbolizer, urlTemplate, dataSector, firstLevel, maxLevel, timeToCache, readExpired, transparency, condition, new java.util.ArrayList<Info>());
  }
  public static TiledVectorLayer newMercator(GEORasterSymbolizer symbolizer, String urlTemplate, Sector dataSector, int firstLevel, int maxLevel, TimeInterval timeToCache, boolean readExpired, float transparency)
  {
     return newMercator(symbolizer, urlTemplate, dataSector, firstLevel, maxLevel, timeToCache, readExpired, transparency, null, new java.util.ArrayList<Info>());
  }
  public static TiledVectorLayer newMercator(GEORasterSymbolizer symbolizer, String urlTemplate, Sector dataSector, int firstLevel, int maxLevel, TimeInterval timeToCache, boolean readExpired)
  {
     return newMercator(symbolizer, urlTemplate, dataSector, firstLevel, maxLevel, timeToCache, readExpired, 1, null, new java.util.ArrayList<Info>());
  }
  public static TiledVectorLayer newMercator(GEORasterSymbolizer symbolizer, String urlTemplate, Sector dataSector, int firstLevel, int maxLevel, TimeInterval timeToCache)
  {
     return newMercator(symbolizer, urlTemplate, dataSector, firstLevel, maxLevel, timeToCache, true, 1, null, new java.util.ArrayList<Info>());
  }
  public static TiledVectorLayer newMercator(GEORasterSymbolizer symbolizer, String urlTemplate, Sector dataSector, int firstLevel, int maxLevel)
  {
     return newMercator(symbolizer, urlTemplate, dataSector, firstLevel, maxLevel, TimeInterval.fromDays(30), true, 1, null, new java.util.ArrayList<Info>());
  }
  public static TiledVectorLayer newMercator(GEORasterSymbolizer symbolizer, String urlTemplate, Sector dataSector, int firstLevel, int maxLevel, TimeInterval timeToCache, boolean readExpired, float transparency, LayerCondition condition, java.util.ArrayList<Info> layerInfo)
  {
    final java.util.ArrayList<LayerTilesRenderParameters> parametersVector = new java.util.ArrayList<LayerTilesRenderParameters>();
    parametersVector.add(LayerTilesRenderParameters.createDefaultMercator(firstLevel, maxLevel));
    return new TiledVectorLayer(symbolizer, urlTemplate, dataSector, parametersVector, timeToCache, readExpired, transparency, condition, layerInfo);
  }

  public void dispose()
  {
    _symbolizer = null;
    if (_tileImageProvider != null)
    {
      _tileImageProvider.layerDeleted(this);
      _tileImageProvider._release();
    }
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
  
    return new TiledVectorLayer(_symbolizer.copy(), _urlTemplate, _dataSector, createParametersVectorCopy(), _timeToCache, _readExpired, _transparency, (_condition == null) ? null : _condition.copy(), _layerInfo);
  }

  public final TileImageContribution contribution(Tile tile)
  {
    if ((_condition == null) || _condition.isAvailable(tile))
    {
      return (_dataSector.touchesWith(tile._sector) ? TileImageContribution.fullCoverageTransparent(_transparency) : null);
    }
    return null;
  }

  public final TileImageProvider createTileImageProvider(G3MRenderContext rc, LayerTilesRenderParameters layerTilesRenderParameters)
  {
    if (_tileImageProvider == null)
    {
      _tileImageProvider = new TiledVectorLayerTileImageProvider(this, rc.getDownloader(), rc.getThreadUtils());
    }
    _tileImageProvider._retain();
    return _tileImageProvider;
  }

  public static class RequestGEOJSONBufferData
  {
    public final URL          _url;
    public final TimeInterval _timeToCache;
    public final boolean _readExpired;

    public RequestGEOJSONBufferData(URL url, TimeInterval timeToCache, boolean readExpired)
    {
       _url = url;
       _timeToCache = timeToCache;
       _readExpired = readExpired;
    }
  }

  public final TiledVectorLayer.RequestGEOJSONBufferData getRequestGEOJSONBufferData(Tile tile)
  {
    final LayerTilesRenderParameters parameters = _parametersVector.get(_selectedLayerTilesRenderParametersIndex);
  
    if (tile._level > parameters._maxLevel)
    {
      final Tile parentTile = tile.getParent();
      if (parentTile != null)
      {
        return getRequestGEOJSONBufferData(parentTile);
      }
    }
  
    return new RequestGEOJSONBufferData(createURL(tile), _timeToCache, _readExpired);
  }

  public final GEORasterSymbolizer symbolizerCopy()
  {
    return _symbolizer.copy();
  }

  public final Sector getDataSector()
  {
    return _dataSector;
  }

  public final void setSymbolizer(GEORasterSymbolizer symbolizer, boolean deletePrevious)
  {
    if (_symbolizer != symbolizer)
    {
      if (deletePrevious)
      {
        _symbolizer = null;
      }
      _symbolizer = symbolizer;
      notifyChanges();
    }
  }

  public final java.util.ArrayList<URL> getDownloadURLs(Tile tile)
  {
    java.util.ArrayList<URL> result = new java.util.ArrayList<URL>();
    result.add(new URL(createURL(tile)));
    return result;
  }

}