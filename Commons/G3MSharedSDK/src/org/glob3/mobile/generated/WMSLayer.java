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



  public WMSLayer(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, String queryLayer, URL queryServerURL, WMSServerVersion queryServerVersion, Sector sector, String format, String srs, String style, boolean isTransparent, LayerCondition condition, TimeInterval timeToCache)
  {
     super(condition, mapLayer, timeToCache);
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
     super(condition, mapLayer, timeToCache);
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

  public final java.util.ArrayList<Petition> getMapPetitions(G3MRenderContext rc, Tile tile, Vector2I tileTextureResolution)
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
  
     //Server name
    String req = _mapServerURL.getPath();
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
        isb.addDouble(sector.lower().latitude()._degrees);
        isb.addString(",");
        isb.addDouble(sector.lower().longitude()._degrees);
        isb.addString(",");
        isb.addDouble(sector.upper().latitude()._degrees);
        isb.addString(",");
        isb.addDouble(sector.upper().longitude()._degrees);
  
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
        isb.addDouble(sector.lower().longitude()._degrees);
        isb.addString(",");
        isb.addDouble(sector.lower().latitude()._degrees);
        isb.addString(",");
        isb.addDouble(sector.upper().longitude()._degrees);
        isb.addString(",");
        isb.addDouble(sector.upper().latitude()._degrees);
  
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
  
    Petition petition = new Petition(sector, new URL(req, false), _timeToCache);
    petitions.add(petition);
  
     return petitions;
  }

//  bool isTransparent() const{
//    return _isTransparent;
//  }

  public final URL getFeatureInfoURL(Geodetic2D g, IFactory factory, Sector tileSector, int width, int height)
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
        isb.addInt(width);
        isb.addString("&HEIGHT=");
        isb.addInt(height);
  
        isb.addString("&BBOX=");
        isb.addDouble(sector.lower().latitude()._degrees);
        isb.addString(",");
        isb.addDouble(sector.lower().longitude()._degrees);
        isb.addString(",");
        isb.addDouble(sector.upper().latitude()._degrees);
        isb.addString(",");
        isb.addDouble(sector.upper().longitude()._degrees);
  
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
        isb.addInt(width);
        isb.addString("&HEIGHT=");
        isb.addInt(height);
  
        isb.addString("&BBOX=");
        isb.addDouble(sector.lower().longitude()._degrees);
        isb.addString(",");
        isb.addDouble(sector.lower().latitude()._degrees);
        isb.addString(",");
        isb.addDouble(sector.upper().longitude()._degrees);
        isb.addString(",");
        isb.addDouble(sector.upper().latitude()._degrees);
  
        req += isb.getString();
  
        if (isb != null)
           isb.dispose();
        break;
      }
    }
    req += "&LAYERS=" + _queryLayer;
  
    //req += "&LAYERS=" + _queryLayers;
    req += "&QUERY_LAYERS=" + _queryLayer;
  
    req += "&INFO_FORMAT=text/plain";
  
    //X and Y
    Vector2D pixel = tileSector.getUVCoordinates(g);
    int x = (int) IMathUtils.instance().round((pixel._x * width));
    int y = (int) IMathUtils.instance().round(((1.0 - pixel._y) * height));
  
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