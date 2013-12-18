package org.glob3.mobile.generated; 
//
//  URLTemplateLayer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/29/13.
//
//

//
//  URLTemplateLayer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/29/13.
//
//



public class URLTemplateLayer extends Layer
{
  private final String _urlTemplate;
  private final Sector _sector ;
  private final boolean _isTransparent;

  private IMathUtils   _mu;
  private IStringUtils _su;

  private URLTemplateLayer(String urlTemplate, Sector sector, boolean isTransparent, TimeInterval timeToCache, boolean readExpired, LayerCondition condition, LayerTilesRenderParameters parameters)
  {
     this(urlTemplate, sector, isTransparent, timeToCache, readExpired, condition, parameters, 1.0);
  }
  private URLTemplateLayer(String urlTemplate, Sector sector, boolean isTransparent, TimeInterval timeToCache, boolean readExpired, LayerCondition condition, LayerTilesRenderParameters parameters, double transparency)
  {
     super(condition, "URLTemplate", timeToCache, readExpired, parameters, transparency);
     _urlTemplate = urlTemplate;
     _sector = new Sector(sector);
     _isTransparent = isTransparent;
     _su = null;
     _mu = null;
  
  }

  private String getPath(LayerTilesRenderParameters layerTilesRenderParameters, Tile tile, Sector sector)
  {
  
    if (_mu == null)
    {
      _mu = IMathUtils.instance();
    }
  
    if (_su == null)
    {
      _su = IStringUtils.instance();
    }
  
    final Vector2I tileTextureResolution = _parameters._tileTextureResolution;
  
    final int level = tile._level;
    final int column = tile._column;
    final int numRows = (int)(layerTilesRenderParameters._topSectorSplitsByLatitude * _mu.pow(2.0, level));
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
    path = _su.replaceSubstring(path, "{level}", _su.toString(level));
    path = _su.replaceSubstring(path, "{lowerLatitude}", _su.toString(sector._lower._latitude._degrees));
    path = _su.replaceSubstring(path, "{lowerLongitude}", _su.toString(sector._lower._longitude._degrees));
    path = _su.replaceSubstring(path, "{upperLatitude}", _su.toString(sector._upper._latitude._degrees));
    path = _su.replaceSubstring(path, "{upperLongitude}", _su.toString(sector._upper._longitude._degrees));
    path = _su.replaceSubstring(path, "{north}", _su.toString(north));
    path = _su.replaceSubstring(path, "{south}", _su.toString(south));
    path = _su.replaceSubstring(path, "{west}", _su.toString(west));
    path = _su.replaceSubstring(path, "{east}", _su.toString(east));
  
    return path;
  }

  protected final String getLayerType()
  {
    return "URLTemplate";
  }

  protected final boolean rawIsEquals(Layer that)
  {
    URLTemplateLayer t = (URLTemplateLayer) that;
  
    if (_isTransparent != t._isTransparent)
    {
      return false;
    }
  
    if (!_urlTemplate.equals(t._urlTemplate))
    {
      return false;
    }
  
    return (_sector.isEquals(t._sector));
  }

  public static URLTemplateLayer newMercator(String urlTemplate, Sector sector, boolean isTransparent, int firstLevel, int maxLevel, TimeInterval timeToCache, boolean readExpired, LayerCondition condition)
  {
     return newMercator(urlTemplate, sector, isTransparent, firstLevel, maxLevel, timeToCache, readExpired, condition, 1.0);
  }
  public static URLTemplateLayer newMercator(String urlTemplate, Sector sector, boolean isTransparent, int firstLevel, int maxLevel, TimeInterval timeToCache, boolean readExpired)
  {
     return newMercator(urlTemplate, sector, isTransparent, firstLevel, maxLevel, timeToCache, readExpired, null, 1.0);
  }
  public static URLTemplateLayer newMercator(String urlTemplate, Sector sector, boolean isTransparent, int firstLevel, int maxLevel, TimeInterval timeToCache)
  {
     return newMercator(urlTemplate, sector, isTransparent, firstLevel, maxLevel, timeToCache, true, null, 1.0);
  }
  public static URLTemplateLayer newMercator(String urlTemplate, Sector sector, boolean isTransparent, int firstLevel, int maxLevel, TimeInterval timeToCache, boolean readExpired, LayerCondition condition, double transparency)
  {
    return new URLTemplateLayer(urlTemplate, sector, isTransparent, timeToCache, readExpired, (condition == null) ? new LevelTileCondition(firstLevel, maxLevel) : condition, LayerTilesRenderParameters.createDefaultMercator(2, maxLevel), transparency);
  }

  public static URLTemplateLayer newWGS84(String urlTemplate, Sector sector, boolean isTransparent, int firstLevel, int maxLevel, TimeInterval timeToCache, boolean readExpired, LayerCondition condition)
  {
     return newWGS84(urlTemplate, sector, isTransparent, firstLevel, maxLevel, timeToCache, readExpired, condition, 1.0);
  }
  public static URLTemplateLayer newWGS84(String urlTemplate, Sector sector, boolean isTransparent, int firstLevel, int maxLevel, TimeInterval timeToCache, boolean readExpired)
  {
     return newWGS84(urlTemplate, sector, isTransparent, firstLevel, maxLevel, timeToCache, readExpired, null, 1.0);
  }
  public static URLTemplateLayer newWGS84(String urlTemplate, Sector sector, boolean isTransparent, int firstLevel, int maxLevel, TimeInterval timeToCache)
  {
     return newWGS84(urlTemplate, sector, isTransparent, firstLevel, maxLevel, timeToCache, true, null, 1.0);
  }
  public static URLTemplateLayer newWGS84(String urlTemplate, Sector sector, boolean isTransparent, int firstLevel, int maxLevel, TimeInterval timeToCache, boolean readExpired, LayerCondition condition, double transparency)
  {
    return new URLTemplateLayer(urlTemplate, sector, isTransparent, timeToCache, readExpired, (condition == null) ? new LevelTileCondition(firstLevel, maxLevel) : condition, LayerTilesRenderParameters.createDefaultWGS84(sector, firstLevel, maxLevel), transparency);
  }

  public final String description()
  {
    return "[URLTemplateLayer urlTemplate=\"" + _urlTemplate + "\"]";
  }

  public final URLTemplateLayer copy()
  {
    return new URLTemplateLayer(_urlTemplate, _sector, _isTransparent, TimeInterval.fromMilliseconds(_timeToCacheMS), _readExpired, (_condition == null) ? null : _condition.copy(), _parameters.copy());
  }

  public final URL getFeatureInfoURL(Geodetic2D position, Sector sector)
  {
    return new URL();
  }

  public final java.util.ArrayList<Petition> createTileMapPetitions(G3MRenderContext rc, LayerTilesRenderParameters layerTilesRenderParameters, Tile tile)
  {
    java.util.ArrayList<Petition> petitions = new java.util.ArrayList<Petition>();
  
    final Sector tileSector = tile._sector;
    if (!_sector.touchesWith(tileSector))
    {
      return petitions;
    }
  
    final Sector sector = tileSector.intersection(_sector);
    if (sector._deltaLatitude.isZero() || sector._deltaLongitude.isZero())
    {
      return petitions;
    }
  
    final String path = getPath(layerTilesRenderParameters, tile, sector);
  
    petitions.add(new Petition(sector, new URL(path, false), TimeInterval.fromMilliseconds(_timeToCacheMS), _readExpired, _isTransparent, _transparency));
  
    return petitions;
  }

  public final RenderState getRenderState()
  {
    _errors.clear();
    if (_urlTemplate.compareTo("") == 0)
    {
      _errors.add("Missing layer parameter: urlTemplate");
    }
  
    if (_errors.size() > 0)
    {
      return RenderState.error(_errors);
    }
    return RenderState.ready();
  }
}