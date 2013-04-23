package org.glob3.mobile.generated; 
public class WMSLayer extends Layer
{

  private final URL _mapServerURL;
  private final URL _queryServerURL;

  private final String _mapLayer;
  private final WMSServerVersion _mapServerVersion;
  private final String _queryLayer;
  private final WMSServerVersion _queryServerVersion;

  private Sector _sector ;

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


  public WMSLayer(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, String queryLayer, URL queryServerURL, WMSServerVersion queryServerVersion, Sector sector, String format, String srs, String style, boolean isTransparent, LayerCondition condition, TimeInterval timeToCache)
  {
     this(mapLayer, mapServerURL, mapServerVersion, queryLayer, queryServerURL, queryServerVersion, sector, format, srs, style, isTransparent, condition, timeToCache, null);
  }
  public WMSLayer(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, String queryLayer, URL queryServerURL, WMSServerVersion queryServerVersion, Sector sector, String format, String srs, String style, boolean isTransparent, LayerCondition condition, TimeInterval timeToCache, LayerTilesRenderParameters parameters)
  {
     super(condition, mapLayer, timeToCache, (parameters == null) ? LayerTilesRenderParameters.createDefaultNonMercator(Sector.fullSphere()) : parameters);
     _mapLayer = mapLayer;
     _mapServerURL = mapServerURL;
     _mapServerVersion = mapServerVersion;
     _queryLayer = queryLayer;
     _queryServerURL = queryServerURL;
     _queryServerVersion = queryServerVersion;
     _sector = new Sector(sector);
     _format = format;
     _srs = srs;
     _style = style;
     _isTransparent = isTransparent;
     _extraParameter = "";
  
  }

  public WMSLayer(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, Sector sector, String format, String srs, String style, boolean isTransparent, LayerCondition condition, TimeInterval timeToCache)
  {
     this(mapLayer, mapServerURL, mapServerVersion, sector, format, srs, style, isTransparent, condition, timeToCache, null);
  }
  public WMSLayer(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, Sector sector, String format, String srs, String style, boolean isTransparent, LayerCondition condition, TimeInterval timeToCache, LayerTilesRenderParameters parameters)
  {
     super(condition, mapLayer, timeToCache, (parameters == null) ? LayerTilesRenderParameters.createDefaultNonMercator(Sector.fullSphere()) : parameters);
     _mapLayer = mapLayer;
     _mapServerURL = mapServerURL;
     _mapServerVersion = mapServerVersion;
     _queryLayer = mapLayer;
     _queryServerURL = mapServerURL;
     _queryServerVersion = mapServerVersion;
     _sector = new Sector(sector);
     _format = format;
     _srs = srs;
     _style = style;
     _isTransparent = isTransparent;
     _extraParameter = "";
  
  }

  public final java.util.ArrayList<Petition> createTileMapPetitions(G3MRenderContext rc, Tile tile)
  {
    java.util.ArrayList<Petition> petitions = new java.util.ArrayList<Petition>();
  
    final Sector tileSector = tile.getSector();
    if (!_sector.touchesWith(tileSector))
    {
      return petitions;
    }
  
    final Sector sector = tileSector.intersection(_sector);
    if (sector.getDeltaLatitude().isZero() || sector.getDeltaLongitude().isZero())
    {
      return petitions;
    }
  
    final Vector2I tileTextureResolution = _parameters._tileTextureResolution;
  
     //Server name
    String req = _mapServerURL.getPath();
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
  
        IStringBuilder isb = IStringBuilder.newStringBuilder();
  
        isb.addString("&WIDTH=");
        isb.addInt(tileTextureResolution._x);
        isb.addString("&HEIGHT=");
        isb.addInt(tileTextureResolution._y);
  
        isb.addString("&BBOX=");
        isb.addDouble(toBBOXLatitude(sector.lower().latitude()));
        isb.addString(",");
        isb.addDouble(toBBOXLongitude(sector.lower().longitude()));
        isb.addString(",");
        isb.addDouble(toBBOXLatitude(sector.upper().latitude()));
        isb.addString(",");
        isb.addDouble(toBBOXLongitude(sector.upper().longitude()));
  
        req += isb.getString();
        if (isb != null)
           isb.dispose();
  
        req += "&CRS=EPSG:4326";
  
        break;
      }
      case WMS_1_1_0:
      default:
      {
        // default is 1.1.1
        req += "&VERSION=1.1.1";
  
        IStringBuilder isb = IStringBuilder.newStringBuilder();
  
        isb.addString("&WIDTH=");
        isb.addInt(tileTextureResolution._x);
        isb.addString("&HEIGHT=");
        isb.addInt(tileTextureResolution._y);
  
        isb.addString("&BBOX=");
        isb.addDouble(toBBOXLongitude(sector.lower().longitude()));
        isb.addString(",");
        isb.addDouble(toBBOXLatitude(sector.lower().latitude()));
        isb.addString(",");
        isb.addDouble(toBBOXLongitude(sector.upper().longitude()));
        isb.addString(",");
        isb.addDouble(toBBOXLatitude(sector.upper().latitude()));
  
        req += isb.getString();
        if (isb != null)
           isb.dispose();
        break;
      }
    }
  
    req += "&LAYERS=" + _mapLayer;
  
     req += "&FORMAT=" + _format;
  
    if (!_srs.equals(""))
    {
      req += "&SRS=" + _srs;
    }
     else
     {
      req += "&SRS=EPSG:4326";
    }
  
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
  
  //  printf("Request: %s\n", req.c_str());
  
    Petition petition = new Petition(sector, new URL(req, false), getTimeToCache(), _isTransparent);
    petitions.add(petition);
  
     return petitions;
  }

  //  bool isTransparent() const{
  //    return _isTransparent;
  //  }

  public final URL getFeatureInfoURL(Geodetic2D position, Sector tileSector)
  {
    if (!_sector.touchesWith(tileSector))
    {
      return URL.nullURL();
    }
  
    final Sector sector = tileSector.intersection(_sector);
  
     //Server name
    String req = _queryServerURL.getPath();
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
  
    //SRS
    if (!_srs.equals(""))
    {
      req += "&SRS=" + _srs;
    }
     else
     {
      req += "&SRS=EPSG:4326";
    }
  
    switch (_queryServerVersion)
    {
      case WMS_1_3_0:
      {
        req += "&VERSION=1.3.0";
  
        IStringBuilder isb = IStringBuilder.newStringBuilder();
  
        isb.addString("&WIDTH=");
        isb.addInt(_parameters._tileTextureResolution._x);
        isb.addString("&HEIGHT=");
        isb.addInt(_parameters._tileTextureResolution._y);
  
        isb.addString("&BBOX=");
        isb.addDouble(toBBOXLatitude(sector.lower().latitude()));
        isb.addString(",");
        isb.addDouble(toBBOXLongitude(sector.lower().longitude()));
        isb.addString(",");
        isb.addDouble(toBBOXLatitude(sector.upper().latitude()));
        isb.addString(",");
        isb.addDouble(toBBOXLongitude(sector.upper().longitude()));
  
        req += isb.getString();
  
        if (isb != null)
           isb.dispose();
  
        req += "&CRS=EPSG:4326";
  
        break;
      }
      case WMS_1_1_0:
      default:
      {
        // default is 1.1.1
        req += "&VERSION=1.1.1";
  
        IStringBuilder isb = IStringBuilder.newStringBuilder();
  
        isb.addString("&WIDTH=");
        isb.addInt(_parameters._tileTextureResolution._x);
        isb.addString("&HEIGHT=");
        isb.addInt(_parameters._tileTextureResolution._y);
  
        isb.addString("&BBOX=");
        isb.addDouble(toBBOXLongitude(sector.lower().longitude()));
        isb.addString(",");
        isb.addDouble(toBBOXLatitude(sector.lower().latitude()));
        isb.addString(",");
        isb.addDouble(toBBOXLongitude(sector.upper().longitude()));
        isb.addString(",");
        isb.addDouble(toBBOXLatitude(sector.upper().latitude()));
  
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
      u = sector.getUCoordinates(position.longitude());
      v = MercatorUtils.getMercatorV(position.latitude());
    }
    else
    {
      final Vector2D uv = sector.getUVCoordinates(position);
      u = uv._x;
      v = uv._y;
    }
  
    //X and Y
    //const Vector2D uv = sector.getUVCoordinates(position);
  //  const int x = (int) mu->round( (uv._x * _parameters->_tileTextureResolution._x) );
  //  const int y = (int) mu->round( (uv._y * _parameters->_tileTextureResolution._y) );
    final int x = (int) mu.round((u * _parameters._tileTextureResolution._x));
    final int y = (int) mu.round((v * _parameters._tileTextureResolution._y));
    // const int y = (int) mu->round( ((1.0 - uv._y) * _parameters->_tileTextureResolution._y) );
  
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("&X=");
    isb.addInt(x);
    isb.addString("&Y=");
    isb.addInt(y);
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

}