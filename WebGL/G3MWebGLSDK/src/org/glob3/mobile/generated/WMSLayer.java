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



  public WMSLayer(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, String queryLayer, URL queryServerURL, WMSServerVersion queryServerVersion, Sector sector, String format, String srs, String style, boolean isTransparent, LayerCondition condition)
  {
	  super(condition);
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

  }

  public WMSLayer(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, Sector sector, String format, String srs, String style, boolean isTransparent, LayerCondition condition)
  {
	  super(condition);
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

  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: java.util.ArrayList<Petition*> getMapPetitions(const RenderContext* rc, const Tile* tile, int width, int height) const
  public final java.util.ArrayList<Petition> getMapPetitions(RenderContext rc, Tile tile, int width, int height)
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
  
		isb.add("&WIDTH=").add(width);
		isb.add("&HEIGHT=").add(height);
  
		isb.add("&BBOX=").add(sector.lower().latitude().degrees()).add(",");
		isb.add(sector.lower().longitude().degrees()).add(",");
		isb.add(sector.upper().latitude().degrees()).add(",");
		isb.add(sector.upper().longitude().degrees());
  
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
  
		isb.add("&WIDTH=").add(width);
		isb.add("&HEIGHT=").add(height);
  
		isb.add("&BBOX=").add(sector.lower().longitude().degrees()).add(",");
		isb.add(sector.lower().latitude().degrees()).add(",");
		isb.add(sector.upper().longitude().degrees()).add(",");
		isb.add(sector.upper().latitude().degrees());
  
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
  
	Petition petition = new Petition(sector, new URL(req));
	petitions.add(petition);
  
	  return petitions;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isTransparent() const
  public final boolean isTransparent()
  {
	return _isTransparent;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: URL getFeatureInfoURL(const Geodetic2D& g, const IFactory* factory, const Sector& tileSector, int width, int height) const
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
  
		isb.add("&WIDTH=").add(width);
		isb.add("&HEIGHT=").add(height);
  
		isb.add("&BBOX=").add(sector.lower().latitude().degrees()).add(",");
		isb.add(sector.lower().longitude().degrees()).add(",");
		isb.add(sector.upper().latitude().degrees()).add(",");
		isb.add(sector.upper().longitude().degrees());
  
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
  
		isb.add("&WIDTH=").add(width);
		isb.add("&HEIGHT=").add(height);
  
		isb.add("&BBOX=").add(sector.lower().longitude().degrees()).add(",");
		isb.add(sector.lower().latitude().degrees()).add(",");
		isb.add(sector.upper().longitude().degrees()).add(",");
		isb.add(sector.upper().latitude().degrees());
  
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
	int x = (int) IMathUtils.instance().round((pixel.x() * width));
	int y = (int) IMathUtils.instance().round(((1.0 - pixel.y()) * height));
  
	IStringBuilder isb = IStringBuilder.newStringBuilder();
	isb.add("&X=").add(x).add("&Y=").add(y);
	req += isb.getString();
	if (isb != null)
		isb.dispose();
  
	  return new URL(req);
  }

}