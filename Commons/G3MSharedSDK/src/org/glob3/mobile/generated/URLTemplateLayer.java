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



//class IStringUtils;


public class URLTemplateLayer extends RasterLayer
{
  private final String _urlTemplate;
  private final boolean _isTransparent;

  private IMathUtils   _mu;
  private IStringUtils _su;

  protected final String getLayerType()
  {
    return "URLTemplate";
  }

  protected String getPath(LayerTilesRenderParameters layerTilesRenderParameters, Tile tile, Sector sector)
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
  
    return path;
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
  
    return (_dataSector.isEquals(t._dataSector));
  }

<<<<<<< HEAD
  protected final TileImageContribution rawContribution(Tile tile)
  {
    final Sector tileSector = tile._sector;
  
    if (!_sector.touchesWith(tileSector))
    {
      return null;
    }
    else if (_sector.fullContains(tileSector))
    {
      return ((_isTransparent || (_transparency < 1)) ? TileImageContribution.fullCoverageTransparent(_transparency) : TileImageContribution.fullCoverageOpaque());
    }
    else
    {
      final Sector contributionSector = _sector.intersection(tileSector);
      return ((_isTransparent || (_transparency < 1)) ? TileImageContribution.partialCoverageTransparent(contributionSector, _transparency) : TileImageContribution.partialCoverageOpaque(contributionSector));
    }
  }

  protected final URL createURL(Tile tile)
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

  public static URLTemplateLayer newMercator(String urlTemplate, Sector sector, boolean isTransparent, int firstLevel, int maxLevel, TimeInterval timeToCache, boolean readExpired, float transparency)
  {
     return newMercator(urlTemplate, sector, isTransparent, firstLevel, maxLevel, timeToCache, readExpired, transparency, null);
=======
  public static URLTemplateLayer newMercator(String urlTemplate, Sector dataSector, boolean isTransparent, int firstLevel, int maxLevel, TimeInterval timeToCache, boolean readExpired, LayerCondition condition, float transparency)
  {
     return newMercator(urlTemplate, dataSector, isTransparent, firstLevel, maxLevel, timeToCache, readExpired, condition, transparency, "");
>>>>>>> purgatory
  }
  public static URLTemplateLayer newMercator(String urlTemplate, Sector dataSector, boolean isTransparent, int firstLevel, int maxLevel, TimeInterval timeToCache, boolean readExpired, LayerCondition condition)
  {
<<<<<<< HEAD
     return newMercator(urlTemplate, sector, isTransparent, firstLevel, maxLevel, timeToCache, readExpired, 1, null);
=======
     return newMercator(urlTemplate, dataSector, isTransparent, firstLevel, maxLevel, timeToCache, readExpired, condition, (float)1.0, "");
>>>>>>> purgatory
  }
  public static URLTemplateLayer newMercator(String urlTemplate, Sector dataSector, boolean isTransparent, int firstLevel, int maxLevel, TimeInterval timeToCache, boolean readExpired)
  {
<<<<<<< HEAD
     return newMercator(urlTemplate, sector, isTransparent, firstLevel, maxLevel, timeToCache, true, 1, null);
  }
  public static URLTemplateLayer newMercator(String urlTemplate, Sector sector, boolean isTransparent, int firstLevel, int maxLevel, TimeInterval timeToCache, boolean readExpired, float transparency, LayerCondition condition)
=======
     return newMercator(urlTemplate, dataSector, isTransparent, firstLevel, maxLevel, timeToCache, readExpired, null, (float)1.0, "");
  }
  public static URLTemplateLayer newMercator(String urlTemplate, Sector dataSector, boolean isTransparent, int firstLevel, int maxLevel, TimeInterval timeToCache)
>>>>>>> purgatory
  {
     return newMercator(urlTemplate, dataSector, isTransparent, firstLevel, maxLevel, timeToCache, true, null, (float)1.0, "");
  }
  public static URLTemplateLayer newMercator(String urlTemplate, Sector dataSector, boolean isTransparent, int firstLevel, int maxLevel, TimeInterval timeToCache, boolean readExpired, LayerCondition condition, float transparency, String disclaimerInfo)
  {
    return new URLTemplateLayer(urlTemplate, dataSector, isTransparent, timeToCache, readExpired, (condition == null) ? new LevelTileCondition(firstLevel, maxLevel) : condition, LayerTilesRenderParameters.createDefaultMercator(2, maxLevel), transparency, disclaimerInfo);
  }

  public static URLTemplateLayer newWGS84(String urlTemplate, Sector dataSector, boolean isTransparent, int firstLevel, int maxLevel, TimeInterval timeToCache, boolean readExpired, LayerCondition condition, float transparency)
  {
     return newWGS84(urlTemplate, dataSector, isTransparent, firstLevel, maxLevel, timeToCache, readExpired, condition, transparency, "");
  }
  public static URLTemplateLayer newWGS84(String urlTemplate, Sector dataSector, boolean isTransparent, int firstLevel, int maxLevel, TimeInterval timeToCache, boolean readExpired, LayerCondition condition)
  {
<<<<<<< HEAD
     return newWGS84(urlTemplate, sector, isTransparent, firstLevel, maxLevel, timeToCache, readExpired, condition, 1);
=======
     return newWGS84(urlTemplate, dataSector, isTransparent, firstLevel, maxLevel, timeToCache, readExpired, condition, (float)1.0, "");
>>>>>>> purgatory
  }
  public static URLTemplateLayer newWGS84(String urlTemplate, Sector dataSector, boolean isTransparent, int firstLevel, int maxLevel, TimeInterval timeToCache, boolean readExpired)
  {
<<<<<<< HEAD
     return newWGS84(urlTemplate, sector, isTransparent, firstLevel, maxLevel, timeToCache, readExpired, null, 1);
=======
     return newWGS84(urlTemplate, dataSector, isTransparent, firstLevel, maxLevel, timeToCache, readExpired, null, (float)1.0, "");
>>>>>>> purgatory
  }
  public static URLTemplateLayer newWGS84(String urlTemplate, Sector dataSector, boolean isTransparent, int firstLevel, int maxLevel, TimeInterval timeToCache)
  {
<<<<<<< HEAD
     return newWGS84(urlTemplate, sector, isTransparent, firstLevel, maxLevel, timeToCache, true, null, 1);
=======
     return newWGS84(urlTemplate, dataSector, isTransparent, firstLevel, maxLevel, timeToCache, true, null, (float)1.0, "");
>>>>>>> purgatory
  }
  public static URLTemplateLayer newWGS84(String urlTemplate, Sector dataSector, boolean isTransparent, int firstLevel, int maxLevel, TimeInterval timeToCache, boolean readExpired, LayerCondition condition, float transparency, String disclaimerInfo)
  {
    return new URLTemplateLayer(urlTemplate, dataSector, isTransparent, timeToCache, readExpired, (condition == null) ? new LevelTileCondition(firstLevel, maxLevel) : condition, LayerTilesRenderParameters.createDefaultWGS84(dataSector, firstLevel, maxLevel), transparency, disclaimerInfo);
  }

  public URLTemplateLayer(String urlTemplate, Sector dataSector, boolean isTransparent, TimeInterval timeToCache, boolean readExpired, LayerCondition condition, LayerTilesRenderParameters parameters, float transparency)
  {
     this(urlTemplate, dataSector, isTransparent, timeToCache, readExpired, condition, parameters, transparency, "");
  }
  public URLTemplateLayer(String urlTemplate, Sector dataSector, boolean isTransparent, TimeInterval timeToCache, boolean readExpired, LayerCondition condition, LayerTilesRenderParameters parameters)
  {
<<<<<<< HEAD
     this(urlTemplate, sector, isTransparent, timeToCache, readExpired, condition, parameters, 1);
=======
     this(urlTemplate, dataSector, isTransparent, timeToCache, readExpired, condition, parameters, (float)1.0, "");
>>>>>>> purgatory
  }
  public URLTemplateLayer(String urlTemplate, Sector dataSector, boolean isTransparent, TimeInterval timeToCache, boolean readExpired, LayerCondition condition, LayerTilesRenderParameters parameters, float transparency, String disclaimerInfo)
  {
<<<<<<< HEAD
     super(timeToCache, readExpired, parameters, transparency, condition);
=======
     super(condition, "URLTemplate", timeToCache, readExpired, parameters, dataSector, transparency, disclaimerInfo);
>>>>>>> purgatory
     _urlTemplate = urlTemplate;
     _isTransparent = isTransparent;
     _su = null;
     _mu = null;
  }

  public final String description()
  {
    return "[URLTemplateLayer urlTemplate=\"" + _urlTemplate + "\"]";
  }

  public final URLTemplateLayer copy()
  {
<<<<<<< HEAD
    return new URLTemplateLayer(_urlTemplate, _sector, _isTransparent, _timeToCache, _readExpired, (_condition == null) ? null : _condition.copy(), _parameters.copy());
=======
    return new URLTemplateLayer(_urlTemplate, _dataSector, _isTransparent, TimeInterval.fromMilliseconds(_timeToCacheMS), _readExpired, (_condition == null) ? null : _condition.copy(), _parameters.copy(), _transparency, _disclaimerInfo);
>>>>>>> purgatory
  }

  public final URL getFeatureInfoURL(Geodetic2D position, Sector sector)
  {
    return new URL();
  }

  public final java.util.ArrayList<Petition> createTileMapPetitions(G3MRenderContext rc, LayerTilesRenderParameters layerTilesRenderParameters, Tile tile)
  {
    java.util.ArrayList<Petition> petitions = new java.util.ArrayList<Petition>();
  
    final Sector tileSector = tile._sector;
    if (!_dataSector.touchesWith(tileSector))
    {
      return petitions;
    }
  
    final Sector sector = tileSector.intersection(_dataSector);
    if (sector._deltaLatitude.isZero() || sector._deltaLongitude.isZero())
    {
      return petitions;
    }
  
    final String path = getPath(layerTilesRenderParameters, tile, sector);
  
    petitions.add(new Petition(sector, new URL(path, false), _timeToCache, _readExpired, _isTransparent, _transparency));
  
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