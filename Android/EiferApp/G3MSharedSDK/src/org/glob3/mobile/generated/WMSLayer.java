package org.glob3.mobile.generated; 
public class WMSLayer extends RasterLayer
{

  private final URL _mapServerURL;
  private final URL _queryServerURL;

  private final String _mapLayer;
  private final WMSServerVersion _mapServerVersion;
  private final String _queryLayer;
  private final WMSServerVersion _queryServerVersion;
  private final Sector _dataSector ;
  private final String _format;
  private final String _srs;
  private final String _style;
  private final boolean _isTransparent;
  private String _extraParameter;

  private double toBBOXLongitude(Angle longitude)
  {
    return (_parameters._mercator) ? MercatorUtils.longitudeToMeters(longitude) : longitude._degrees;
  }
  private double toBBOXLatitude(Angle latitude)
  {
    return (_parameters._mercator) ? MercatorUtils.latitudeToMeters(latitude) : latitude._degrees;
  }

  protected final String getLayerType()
  {
    return "WMS";
  }

  protected final boolean rawIsEquals(Layer that)
  {
    WMSLayer t = (WMSLayer) that;
  
    if (!(_mapServerURL.isEquals(t._mapServerURL)))
    {
      return false;
    }
  
    if (!(_queryServerURL.isEquals(t._queryServerURL)))
    {
      return false;
    }
  
    if (!_mapLayer.equals(t._mapLayer))
    {
      return false;
    }
  
    if (_mapServerVersion != t._mapServerVersion)
    {
      return false;
    }
  
    if (!_queryLayer.equals(t._queryLayer))
    {
      return false;
    }
  
    if (_queryServerVersion != t._queryServerVersion)
    {
      return false;
    }
  
    if (!(_dataSector.isEquals(t._dataSector)))
    {
      return false;
    }
  
    if (!_format.equals(t._format))
    {
      return false;
    }
  
    if (_queryServerVersion != t._queryServerVersion)
    {
      return false;
    }
  
    if (!_srs.equals(t._srs))
    {
      return false;
    }
  
    if (!_style.equals(t._style))
    {
      return false;
    }
  
    if (_isTransparent != t._isTransparent)
    {
      return false;
    }
  
    if (!_extraParameter.equals(t._extraParameter))
    {
      return false;
    }
  
    return true;
  }


  protected final TileImageContribution rawContribution(Tile tile)
  {
    final Tile tileP = getParentTileOfSuitableLevel(tile);
    if (tileP == null)
    {
      return null;
    }
  
    final Sector requestedImageSector = tileP._sector;
  
    if (!_dataSector.touchesWith(requestedImageSector))
    {
      return null;
    }
  
  
    else if (_dataSector.fullContains(requestedImageSector) && (tile == tileP))
    {
      //Most common case tile of suitable level being fully coveraged by layer
      return ((_isTransparent || (_transparency < 1)) ? TileImageContribution.fullCoverageTransparent(_transparency) : TileImageContribution.fullCoverageOpaque());
    }
    else
    {
      final Sector contributionSector = _dataSector.intersection(requestedImageSector);
      if (contributionSector.hasNoArea())
      {
        return null;
      }
  
      return ((_isTransparent || (_transparency < 1)) ? TileImageContribution.partialCoverageTransparent(contributionSector, _transparency) : TileImageContribution.partialCoverageOpaque(contributionSector));
    }
  }

  protected final URL createURL(Tile tile)
  {
  
    final String path = _mapServerURL._path;
    //  if (path.empty()) {
    //    return petitions;
    //  }
  
    final Sector tileSector = tile._sector;
    //  if (!_sector.touchesWith(tileSector)) {
    //    return petitions;
    //  }
    //
    final Sector sector = tileSector.intersection(_dataSector);
    //  if (sector._deltaLatitude.isZero() ||
    //      sector._deltaLongitude.isZero() ) {
    //    return petitions;
    //  }
  
    //TODO: MUST SCALE WIDTH,HEIGHT
  
    final int width = _parameters._tileTextureResolution._x;
    final int height = _parameters._tileTextureResolution._y;
  
     //Server name
    String req = path;
     if (req.charAt(req.length() - 1) != '?')
     {
        req += '?';
     }
  
    //  //If the server refer to itself as localhost...
    //  const int localhostPos = req.find("localhost");
    //  if (localhostPos != -1) {
    //    req = req.substr(localhostPos+9);
    //
    //    const int slashPos = req.find("/", 8);
    //    std::string newHost = req.substr(0, slashPos);
    //
    //    req = newHost + req;
    //  }
  
    req += "REQUEST=GetMap&SERVICE=WMS";
  
  
    switch (_mapServerVersion)
    {
      case WMS_1_3_0:
      {
        req += "&VERSION=1.3.0";
  
        if (!_srs.equals(""))
        {
          req += "&CRS=" + _srs;
        }
        else
        {
          req += "&CRS=EPSG:4326";
  
        }
        IStringBuilder isb = IStringBuilder.newStringBuilder();
  
        isb.addString("&WIDTH=");
        isb.addInt(width);
        isb.addString("&HEIGHT=");
        isb.addInt(height);
  
        isb.addString("&BBOX=");
        isb.addDouble(toBBOXLatitude(sector._lower._latitude));
        isb.addString(",");
        isb.addDouble(toBBOXLongitude(sector._lower._longitude));
        isb.addString(",");
        isb.addDouble(toBBOXLatitude(sector._upper._latitude));
        isb.addString(",");
        isb.addDouble(toBBOXLongitude(sector._upper._longitude));
  
        req += isb.getString();
        if (isb != null)
           isb.dispose();
  
        break;
      }
      case WMS_1_1_0:
      default:
      {
        // default is 1.1.1
        req += "&VERSION=1.1.1";
  
        if (!_srs.equals(""))
        {
          req += "&SRS=" + _srs;
        }
        else
        {
          req += "&SRS=EPSG:4326";
        }
  
        IStringBuilder isb = IStringBuilder.newStringBuilder();
  
        isb.addString("&WIDTH=");
        isb.addInt(width);
        isb.addString("&HEIGHT=");
        isb.addInt(height);
  
        isb.addString("&BBOX=");
        isb.addDouble(toBBOXLongitude(sector._lower._longitude));
        isb.addString(",");
        isb.addDouble(toBBOXLatitude(sector._lower._latitude));
        isb.addString(",");
        isb.addDouble(toBBOXLongitude(sector._upper._longitude));
        isb.addString(",");
        isb.addDouble(toBBOXLatitude(sector._upper._latitude));
  
        req += isb.getString();
        if (isb != null)
           isb.dispose();
        break;
      }
    }
  
    req += "&LAYERS=" + _mapLayer;
  
     req += "&FORMAT=" + _format;
  
    //Style
    if (!_style.equals(""))
    {
      req += "&STYLES=" + _style;
    }
     else
     {
      req += "&STYLES=";
    }
  
    //ASKING TRANSPARENCY
    if (_isTransparent)
    {
      req += "&TRANSPARENT=TRUE";
    }
    else
    {
      req += "&TRANSPARENT=FALSE";
    }
  
    if (_extraParameter.compareTo("") != 0)
    {
      req += "&";
      req += _extraParameter;
    }
  
    return new URL(req, false);
  }


  public static WMSLayer newMercator(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, String queryLayer, URL queryServerURL, WMSServerVersion queryServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel, int maxLevel, LayerCondition condition, TimeInterval timeToCache, boolean readExpired, float transparency)
  {
     return newMercator(mapLayer, mapServerURL, mapServerVersion, queryLayer, queryServerURL, queryServerVersion, dataSector, format, style, isTransparent, firstLevel, maxLevel, condition, timeToCache, readExpired, transparency, new java.util.ArrayList<Info>());
  }
  public static WMSLayer newMercator(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, String queryLayer, URL queryServerURL, WMSServerVersion queryServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel, int maxLevel, LayerCondition condition, TimeInterval timeToCache, boolean readExpired)
  {
     return newMercator(mapLayer, mapServerURL, mapServerVersion, queryLayer, queryServerURL, queryServerVersion, dataSector, format, style, isTransparent, firstLevel, maxLevel, condition, timeToCache, readExpired, 1, new java.util.ArrayList<Info>());
  }
  public static WMSLayer newMercator(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, String queryLayer, URL queryServerURL, WMSServerVersion queryServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel, int maxLevel, LayerCondition condition, TimeInterval timeToCache)
  {
     return newMercator(mapLayer, mapServerURL, mapServerVersion, queryLayer, queryServerURL, queryServerVersion, dataSector, format, style, isTransparent, firstLevel, maxLevel, condition, timeToCache, true, 1, new java.util.ArrayList<Info>());
  }
  public static WMSLayer newMercator(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, String queryLayer, URL queryServerURL, WMSServerVersion queryServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel, int maxLevel, LayerCondition condition)
  {
     return newMercator(mapLayer, mapServerURL, mapServerVersion, queryLayer, queryServerURL, queryServerVersion, dataSector, format, style, isTransparent, firstLevel, maxLevel, condition, TimeInterval.fromDays(30), true, 1, new java.util.ArrayList<Info>());
  }
  public static WMSLayer newMercator(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, String queryLayer, URL queryServerURL, WMSServerVersion queryServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel, int maxLevel)
  {
     return newMercator(mapLayer, mapServerURL, mapServerVersion, queryLayer, queryServerURL, queryServerVersion, dataSector, format, style, isTransparent, firstLevel, maxLevel, null, TimeInterval.fromDays(30), true, 1, new java.util.ArrayList<Info>());
  }
  public static WMSLayer newMercator(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, String queryLayer, URL queryServerURL, WMSServerVersion queryServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel)
  {
     return newMercator(mapLayer, mapServerURL, mapServerVersion, queryLayer, queryServerURL, queryServerVersion, dataSector, format, style, isTransparent, firstLevel, 17, null, TimeInterval.fromDays(30), true, 1, new java.util.ArrayList<Info>());
  }
  public static WMSLayer newMercator(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, String queryLayer, URL queryServerURL, WMSServerVersion queryServerVersion, Sector dataSector, String format, String style, boolean isTransparent)
  {
     return newMercator(mapLayer, mapServerURL, mapServerVersion, queryLayer, queryServerURL, queryServerVersion, dataSector, format, style, isTransparent, 2, 17, null, TimeInterval.fromDays(30), true, 1, new java.util.ArrayList<Info>());
  }
  public static WMSLayer newMercator(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, String queryLayer, URL queryServerURL, WMSServerVersion queryServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel, int maxLevel, LayerCondition condition, TimeInterval timeToCache, boolean readExpired, float transparency, java.util.ArrayList<Info> layerInfo)
  {
  //  if (srs.compare("EPSG:4326") == 0) {
  //    layerTilesRenderParameters = LayerTilesRenderParameters::createDefaultWGS84(0, 17);
  //  }
  //  else if (srs.compare("EPSG:3857") == 0) {
  //    layerTilesRenderParameters = LayerTilesRenderParameters::createDefaultMercator(0, 17);
  //  }
    return new WMSLayer(mapLayer, mapServerURL, mapServerVersion, queryLayer, queryServerURL, queryServerVersion, dataSector, format, "EPSG:3857", style, isTransparent, condition, timeToCache, readExpired, LayerTilesRenderParameters.createDefaultMercator(firstLevel, maxLevel), transparency, layerInfo);
  }

  public static WMSLayer newMercator(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel, int maxLevel, LayerCondition condition, TimeInterval timeToCache, boolean readExpired, float transparency)
  {
     return newMercator(mapLayer, mapServerURL, mapServerVersion, dataSector, format, style, isTransparent, firstLevel, maxLevel, condition, timeToCache, readExpired, transparency, new java.util.ArrayList<Info>());
  }
  public static WMSLayer newMercator(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel, int maxLevel, LayerCondition condition, TimeInterval timeToCache, boolean readExpired)
  {
     return newMercator(mapLayer, mapServerURL, mapServerVersion, dataSector, format, style, isTransparent, firstLevel, maxLevel, condition, timeToCache, readExpired, 1, new java.util.ArrayList<Info>());
  }
  public static WMSLayer newMercator(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel, int maxLevel, LayerCondition condition, TimeInterval timeToCache)
  {
     return newMercator(mapLayer, mapServerURL, mapServerVersion, dataSector, format, style, isTransparent, firstLevel, maxLevel, condition, timeToCache, true, 1, new java.util.ArrayList<Info>());
  }
  public static WMSLayer newMercator(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel, int maxLevel, LayerCondition condition)
  {
     return newMercator(mapLayer, mapServerURL, mapServerVersion, dataSector, format, style, isTransparent, firstLevel, maxLevel, condition, TimeInterval.fromDays(30), true, 1, new java.util.ArrayList<Info>());
  }
  public static WMSLayer newMercator(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel, int maxLevel)
  {
     return newMercator(mapLayer, mapServerURL, mapServerVersion, dataSector, format, style, isTransparent, firstLevel, maxLevel, null, TimeInterval.fromDays(30), true, 1, new java.util.ArrayList<Info>());
  }
  public static WMSLayer newMercator(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel)
  {
     return newMercator(mapLayer, mapServerURL, mapServerVersion, dataSector, format, style, isTransparent, firstLevel, 17, null, TimeInterval.fromDays(30), true, 1, new java.util.ArrayList<Info>());
  }
  public static WMSLayer newMercator(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, Sector dataSector, String format, String style, boolean isTransparent)
  {
     return newMercator(mapLayer, mapServerURL, mapServerVersion, dataSector, format, style, isTransparent, 2, 17, null, TimeInterval.fromDays(30), true, 1, new java.util.ArrayList<Info>());
  }
  public static WMSLayer newMercator(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel, int maxLevel, LayerCondition condition, TimeInterval timeToCache, boolean readExpired, float transparency, java.util.ArrayList<Info> layerInfo)
  {
    return new WMSLayer(mapLayer, mapServerURL, mapServerVersion, dataSector, format, "EPSG:3857", style, isTransparent, condition, timeToCache, readExpired, LayerTilesRenderParameters.createDefaultMercator(firstLevel, maxLevel), transparency, layerInfo);
  }

  public static WMSLayer newWGS84(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, String queryLayer, URL queryServerURL, WMSServerVersion queryServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel, int maxLevel, LayerCondition condition, TimeInterval timeToCache, boolean readExpired, float transparency)
  {
     return newWGS84(mapLayer, mapServerURL, mapServerVersion, queryLayer, queryServerURL, queryServerVersion, dataSector, format, style, isTransparent, firstLevel, maxLevel, condition, timeToCache, readExpired, transparency, new java.util.ArrayList<Info>());
  }
  public static WMSLayer newWGS84(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, String queryLayer, URL queryServerURL, WMSServerVersion queryServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel, int maxLevel, LayerCondition condition, TimeInterval timeToCache, boolean readExpired)
  {
     return newWGS84(mapLayer, mapServerURL, mapServerVersion, queryLayer, queryServerURL, queryServerVersion, dataSector, format, style, isTransparent, firstLevel, maxLevel, condition, timeToCache, readExpired, 1, new java.util.ArrayList<Info>());
  }
  public static WMSLayer newWGS84(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, String queryLayer, URL queryServerURL, WMSServerVersion queryServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel, int maxLevel, LayerCondition condition, TimeInterval timeToCache)
  {
     return newWGS84(mapLayer, mapServerURL, mapServerVersion, queryLayer, queryServerURL, queryServerVersion, dataSector, format, style, isTransparent, firstLevel, maxLevel, condition, timeToCache, true, 1, new java.util.ArrayList<Info>());
  }
  public static WMSLayer newWGS84(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, String queryLayer, URL queryServerURL, WMSServerVersion queryServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel, int maxLevel, LayerCondition condition)
  {
     return newWGS84(mapLayer, mapServerURL, mapServerVersion, queryLayer, queryServerURL, queryServerVersion, dataSector, format, style, isTransparent, firstLevel, maxLevel, condition, TimeInterval.fromDays(30), true, 1, new java.util.ArrayList<Info>());
  }
  public static WMSLayer newWGS84(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, String queryLayer, URL queryServerURL, WMSServerVersion queryServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel, int maxLevel)
  {
     return newWGS84(mapLayer, mapServerURL, mapServerVersion, queryLayer, queryServerURL, queryServerVersion, dataSector, format, style, isTransparent, firstLevel, maxLevel, null, TimeInterval.fromDays(30), true, 1, new java.util.ArrayList<Info>());
  }
  public static WMSLayer newWGS84(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, String queryLayer, URL queryServerURL, WMSServerVersion queryServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel)
  {
     return newWGS84(mapLayer, mapServerURL, mapServerVersion, queryLayer, queryServerURL, queryServerVersion, dataSector, format, style, isTransparent, firstLevel, 17, null, TimeInterval.fromDays(30), true, 1, new java.util.ArrayList<Info>());
  }
  public static WMSLayer newWGS84(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, String queryLayer, URL queryServerURL, WMSServerVersion queryServerVersion, Sector dataSector, String format, String style, boolean isTransparent)
  {
     return newWGS84(mapLayer, mapServerURL, mapServerVersion, queryLayer, queryServerURL, queryServerVersion, dataSector, format, style, isTransparent, 1, 17, null, TimeInterval.fromDays(30), true, 1, new java.util.ArrayList<Info>());
  }
  public static WMSLayer newWGS84(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, String queryLayer, URL queryServerURL, WMSServerVersion queryServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel, int maxLevel, LayerCondition condition, TimeInterval timeToCache, boolean readExpired, float transparency, java.util.ArrayList<Info> layerInfo)
  {
    return new WMSLayer(mapLayer, mapServerURL, mapServerVersion, queryLayer, queryServerURL, queryServerVersion, dataSector, format, "EPSG:4326", style, isTransparent, condition, timeToCache, readExpired, LayerTilesRenderParameters.createDefaultWGS84(firstLevel, maxLevel), transparency, layerInfo);
  }

  public static WMSLayer newWGS84(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel, int maxLevel, LayerCondition condition, TimeInterval timeToCache, boolean readExpired, float transparency)
  {
     return newWGS84(mapLayer, mapServerURL, mapServerVersion, dataSector, format, style, isTransparent, firstLevel, maxLevel, condition, timeToCache, readExpired, transparency, new java.util.ArrayList<Info>());
  }
  public static WMSLayer newWGS84(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel, int maxLevel, LayerCondition condition, TimeInterval timeToCache, boolean readExpired)
  {
     return newWGS84(mapLayer, mapServerURL, mapServerVersion, dataSector, format, style, isTransparent, firstLevel, maxLevel, condition, timeToCache, readExpired, 1, new java.util.ArrayList<Info>());
  }
  public static WMSLayer newWGS84(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel, int maxLevel, LayerCondition condition, TimeInterval timeToCache)
  {
     return newWGS84(mapLayer, mapServerURL, mapServerVersion, dataSector, format, style, isTransparent, firstLevel, maxLevel, condition, timeToCache, true, 1, new java.util.ArrayList<Info>());
  }
  public static WMSLayer newWGS84(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel, int maxLevel, LayerCondition condition)
  {
     return newWGS84(mapLayer, mapServerURL, mapServerVersion, dataSector, format, style, isTransparent, firstLevel, maxLevel, condition, TimeInterval.fromDays(30), true, 1, new java.util.ArrayList<Info>());
  }
  public static WMSLayer newWGS84(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel, int maxLevel)
  {
     return newWGS84(mapLayer, mapServerURL, mapServerVersion, dataSector, format, style, isTransparent, firstLevel, maxLevel, null, TimeInterval.fromDays(30), true, 1, new java.util.ArrayList<Info>());
  }
  public static WMSLayer newWGS84(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel)
  {
     return newWGS84(mapLayer, mapServerURL, mapServerVersion, dataSector, format, style, isTransparent, firstLevel, 17, null, TimeInterval.fromDays(30), true, 1, new java.util.ArrayList<Info>());
  }
  public static WMSLayer newWGS84(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, Sector dataSector, String format, String style, boolean isTransparent)
  {
     return newWGS84(mapLayer, mapServerURL, mapServerVersion, dataSector, format, style, isTransparent, 1, 17, null, TimeInterval.fromDays(30), true, 1, new java.util.ArrayList<Info>());
  }
  public static WMSLayer newWGS84(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel, int maxLevel, LayerCondition condition, TimeInterval timeToCache, boolean readExpired, float transparency, java.util.ArrayList<Info> layerInfo)
  {
    return new WMSLayer(mapLayer, mapServerURL, mapServerVersion, dataSector, format, "EPSG:4326", style, isTransparent, condition, timeToCache, readExpired, LayerTilesRenderParameters.createDefaultWGS84(firstLevel, maxLevel), transparency, layerInfo);
  }

  public WMSLayer(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, String queryLayer, URL queryServerURL, WMSServerVersion queryServerVersion, Sector dataSector, String format, String srs, String style, boolean isTransparent, LayerCondition condition, TimeInterval timeToCache, boolean readExpired, LayerTilesRenderParameters parameters, float transparency)
  {
     this(mapLayer, mapServerURL, mapServerVersion, queryLayer, queryServerURL, queryServerVersion, dataSector, format, srs, style, isTransparent, condition, timeToCache, readExpired, parameters, transparency, new java.util.ArrayList<Info>());
  }
  public WMSLayer(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, String queryLayer, URL queryServerURL, WMSServerVersion queryServerVersion, Sector dataSector, String format, String srs, String style, boolean isTransparent, LayerCondition condition, TimeInterval timeToCache, boolean readExpired, LayerTilesRenderParameters parameters)
  {
     this(mapLayer, mapServerURL, mapServerVersion, queryLayer, queryServerURL, queryServerVersion, dataSector, format, srs, style, isTransparent, condition, timeToCache, readExpired, parameters, 1, new java.util.ArrayList<Info>());
  }
  public WMSLayer(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, String queryLayer, URL queryServerURL, WMSServerVersion queryServerVersion, Sector dataSector, String format, String srs, String style, boolean isTransparent, LayerCondition condition, TimeInterval timeToCache, boolean readExpired)
  {
     this(mapLayer, mapServerURL, mapServerVersion, queryLayer, queryServerURL, queryServerVersion, dataSector, format, srs, style, isTransparent, condition, timeToCache, readExpired, null, 1, new java.util.ArrayList<Info>());
  }
  public WMSLayer(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, String queryLayer, URL queryServerURL, WMSServerVersion queryServerVersion, Sector dataSector, String format, String srs, String style, boolean isTransparent, LayerCondition condition, TimeInterval timeToCache, boolean readExpired, LayerTilesRenderParameters parameters, float transparency, java.util.ArrayList<Info> layerInfo)
  {
     super(timeToCache, readExpired, (parameters == null) ? LayerTilesRenderParameters.createDefaultWGS84(Sector.fullSphere(), 0, 17) : parameters, transparency, condition, layerInfo);
     _mapLayer = mapLayer;
     _mapServerURL = mapServerURL;
     _mapServerVersion = mapServerVersion;
     _dataSector = new Sector(dataSector);
     _queryLayer = queryLayer;
     _queryServerURL = queryServerURL;
     _queryServerVersion = queryServerVersion;
     _format = format;
     _srs = srs;
     _style = style;
     _isTransparent = isTransparent;
     _extraParameter = "";
  }

  public WMSLayer(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, Sector dataSector, String format, String srs, String style, boolean isTransparent, LayerCondition condition, TimeInterval timeToCache, boolean readExpired, LayerTilesRenderParameters parameters, float transparency)
  {
     this(mapLayer, mapServerURL, mapServerVersion, dataSector, format, srs, style, isTransparent, condition, timeToCache, readExpired, parameters, transparency, new java.util.ArrayList<Info>());
  }
  public WMSLayer(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, Sector dataSector, String format, String srs, String style, boolean isTransparent, LayerCondition condition, TimeInterval timeToCache, boolean readExpired, LayerTilesRenderParameters parameters)
  {
     this(mapLayer, mapServerURL, mapServerVersion, dataSector, format, srs, style, isTransparent, condition, timeToCache, readExpired, parameters, 1, new java.util.ArrayList<Info>());
  }
  public WMSLayer(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, Sector dataSector, String format, String srs, String style, boolean isTransparent, LayerCondition condition, TimeInterval timeToCache, boolean readExpired)
  {
     this(mapLayer, mapServerURL, mapServerVersion, dataSector, format, srs, style, isTransparent, condition, timeToCache, readExpired, null, 1, new java.util.ArrayList<Info>());
  }
  public WMSLayer(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, Sector dataSector, String format, String srs, String style, boolean isTransparent, LayerCondition condition, TimeInterval timeToCache, boolean readExpired, LayerTilesRenderParameters parameters, float transparency, java.util.ArrayList<Info> layerInfo)
  {
     super(timeToCache, readExpired, (parameters == null) ? LayerTilesRenderParameters.createDefaultWGS84(Sector.fullSphere(), 0, 17) : parameters, transparency, condition, layerInfo);
     _mapLayer = mapLayer;
     _mapServerURL = mapServerURL;
     _mapServerVersion = mapServerVersion;
     _dataSector = new Sector(dataSector);
     _queryLayer = mapLayer;
     _queryServerURL = mapServerURL;
     _queryServerVersion = mapServerVersion;
     _format = format;
     _srs = srs;
     _style = style;
     _isTransparent = isTransparent;
     _extraParameter = "";
  
  }

  public final URL getFeatureInfoURL(Geodetic2D position, Sector tileSector)
  {
    if (!_dataSector.touchesWith(tileSector))
    {
      return URL.nullURL();
    }
  
    final Sector intersectionSector = tileSector.intersection(_dataSector);
  
     //Server name
    String req = _queryServerURL._path;
     if (req.charAt(req.length()-1) != '?')
     {
        req += '?';
     }
  
    //If the server refer to itself as localhost...
    int pos = req.indexOf("localhost");
    if (pos != -1)
    {
      req = req.substring(pos+9);
  
      int pos2 = req.indexOf("/", 8);
      String newHost = req.substring(0, pos2);
  
      req = newHost + req;
    }
  
    req += "REQUEST=GetFeatureInfo&SERVICE=WMS";
  
  
    switch (_queryServerVersion)
    {
      case WMS_1_3_0:
      {
        req += "&VERSION=1.3.0";
        if (!_srs.equals(""))
        {
          req += "&CRS=" + _srs;
        }
        else
        {
          req += "&CRS=EPSG:4326";
        }
  
        IStringBuilder isb = IStringBuilder.newStringBuilder();
  
        isb.addString("&WIDTH=");
        isb.addInt(_parameters._tileTextureResolution._x);
        isb.addString("&HEIGHT=");
        isb.addInt(_parameters._tileTextureResolution._y);
  
        isb.addString("&BBOX=");
        isb.addDouble(toBBOXLatitude(intersectionSector._lower._latitude));
        isb.addString(",");
        isb.addDouble(toBBOXLongitude(intersectionSector._lower._longitude));
        isb.addString(",");
        isb.addDouble(toBBOXLatitude(intersectionSector._upper._latitude));
        isb.addString(",");
        isb.addDouble(toBBOXLongitude(intersectionSector._upper._longitude));
  
        req += isb.getString();
  
        if (isb != null)
           isb.dispose();
  
        break;
      }
      case WMS_1_1_0:
      default:
      {
        // default is 1.1.1
        req += "&VERSION=1.1.1";
  
        if (!_srs.equals(""))
        {
          req += "&SRS=" + _srs;
        }
        else
        {
          req += "&SRS=EPSG:4326";
        }
  
        IStringBuilder isb = IStringBuilder.newStringBuilder();
  
        isb.addString("&WIDTH=");
        isb.addInt(_parameters._tileTextureResolution._x);
        isb.addString("&HEIGHT=");
        isb.addInt(_parameters._tileTextureResolution._y);
  
        isb.addString("&BBOX=");
        isb.addDouble(toBBOXLongitude(intersectionSector._lower._longitude));
        isb.addString(",");
        isb.addDouble(toBBOXLatitude(intersectionSector._lower._latitude));
        isb.addString(",");
        isb.addDouble(toBBOXLongitude(intersectionSector._upper._longitude));
        isb.addString(",");
        isb.addDouble(toBBOXLatitude(intersectionSector._upper._latitude));
  
        req += isb.getString();
  
        if (isb != null)
           isb.dispose();
        break;
      }
    }
    req += "&LAYERS=" + _queryLayer;
    req += "&QUERY_LAYERS=" + _queryLayer;
  
    req += "&INFO_FORMAT=text/plain";
  
    final IMathUtils mu = IMathUtils.instance();
  
    double u;
    double v;
    if (_parameters._mercator)
    {
      u = intersectionSector.getUCoordinate(position._longitude);
      v = MercatorUtils.getMercatorV(position._latitude);
    }
    else
    {
      final Vector2D uv = intersectionSector.getUVCoordinates(position);
      u = uv._x;
      v = uv._y;
    }
  
    //X and Y
    //const Vector2D uv = sector.getUVCoordinates(position);
    final long x = mu.round((u * _parameters._tileTextureResolution._x));
    final long y = mu.round((v * _parameters._tileTextureResolution._y));
  
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("&X=");
    isb.addLong(x);
    isb.addString("&Y=");
    isb.addLong(y);
    req += isb.getString();
    if (isb != null)
       isb.dispose();
  
     return new URL(req, false);
  }

  public final void setExtraParameter(String extraParameter)
  {
    _extraParameter = extraParameter;
    notifyChanges();
  }

  public final String description()
  {
    return "[WMSLayer]";
  }

  public final WMSLayer copy()
  {
    return new WMSLayer(_mapLayer, _mapServerURL, _mapServerVersion, _queryLayer, _queryServerURL, _queryServerVersion, _dataSector, _format, _srs, _style, _isTransparent, (_condition == null) ? null : _condition.copy(), _timeToCache, _readExpired, (_parameters == null) ? null : _parameters.copy(), _transparency, _layerInfo);
  }

  public final RenderState getRenderState()
  {
    _errors.clear();
    if (_mapLayer.compareTo("") == 0)
    {
      _errors.add("Missing layer parameter: mapLayer");
    }
    final String mapServerUrl = _mapServerURL._path;
    if (mapServerUrl.compareTo("") == 0)
    {
      _errors.add("Missing layer parameter: mapServerURL");
    }
    if (_format.compareTo("") == 0)
    {
      _errors.add("Missing layer parameter: format");
    }
  
    if (_errors.size() > 0)
    {
      return RenderState.error(_errors);
    }
    return RenderState.ready();
  }

  public final Sector getDataSector()
  {
    return _dataSector;
  }

}