package org.glob3.mobile.generated; 
public class WMSLayer extends Layer
{

  private final String _mapLayers;
  private final String _queryLayers;

  private final String _format;
  private final String _style;
  private final String _srs;
  private Sector _bbox ;
  private final boolean _isTransparent;

  private final Angle _minTileLongitudeDelta ;
  private final Angle _maxTileLongitudeDelta ;


  private final String _serverURL;
  private final WMSServerVersion _serverVersion;



  public WMSLayer(String mapLayers, String queryLayers, String serverURL, WMSServerVersion serverVersion, String format, Sector bbox, String srs, String style, boolean isTransparent, Angle minTileLongitudeDelta, Angle maxTileLongitudeDelta)
  {
	  _mapLayers = mapLayers;
	  _queryLayers = queryLayers;
	  _format = format;
	  _style = style;
	  _bbox = new Sector(bbox);
	  _srs = srs;
	  _serverURL = serverURL;
	  _serverVersion = serverVersion;
	  _isTransparent = isTransparent;
	  _minTileLongitudeDelta = new Angle(minTileLongitudeDelta);
	  _maxTileLongitudeDelta = new Angle(maxTileLongitudeDelta);

  }


  public WMSLayer(String mapLayers, String serverURL, WMSServerVersion serverVersion, String format, Sector bbox, String srs, String style, boolean isTransparent, Angle minTileLongitudeDelta, Angle maxTileLongitudeDelta)
  {
	  _mapLayers = mapLayers;
	  _queryLayers = mapLayers;
	  _format = format;
	  _style = style;
	  _bbox = new Sector(bbox);
	  _srs = srs;
	  _serverURL = serverURL;
	  _serverVersion = serverVersion;
	  _isTransparent = isTransparent;
	  _minTileLongitudeDelta = new Angle(minTileLongitudeDelta);
	  _maxTileLongitudeDelta = new Angle(maxTileLongitudeDelta);

  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean fullContains(const Sector& s) const
  public final boolean fullContains(Sector s)
  {
	return _bbox.fullContains(s);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: java.util.ArrayList<Petition*> getTilePetitions(const RenderContext* rc, const Tile* tile, int width, int height) const
  public final java.util.ArrayList<Petition> getTilePetitions(RenderContext rc, Tile tile, int width, int height)
  {
	java.util.ArrayList<Petition> petitions = new java.util.ArrayList<Petition>();
  
	final Sector tileSector = tile.getSector();
	if (!_bbox.touchesWith(tileSector))
	{
	  return petitions;
	}
  
	final Sector sector = tileSector.intersection(_bbox);
  
	  //Server name
	String req = _serverURL;
	  if (req.charAt(req.length()-1) != '?')
	  {
		  req += '?';
	  }
  
	//If the server refer to itself as localhost...
	int pos = req.indexOf("localhost");
	if (pos != -1)
	{
	  req = req.substring(pos+9);
  
	  int pos2 = _serverURL.indexOf("/", 8);
	  String newHost = _serverURL.substring(0, pos2);
  
	  req = newHost + req;
	}
  
	req += "REQUEST=GetMap&SERVICE=WMS";
  
  
	switch (_serverVersion)
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
  
	req += "&LAYERS=" + _mapLayers;
  
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
  
	Petition petition = new Petition(sector, new URL(req), _isTransparent);
	petitions.add(petition);
  
	//printf("%s\n", req.c_str());
  
	  return petitions;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isAvailable(const RenderContext* rc, const Tile* tile) const
  public final boolean isAvailable(RenderContext rc, Tile tile)
  {
	final Angle dLon = tile.getSector().getDeltaLongitude();
  
	if ((!_minTileLongitudeDelta.isNan() && dLon.lowerThan(_minTileLongitudeDelta)) || (!_maxTileLongitudeDelta.isNan() && dLon.greaterThan(_maxTileLongitudeDelta)))
	{
	  return false;
	}
	else
	{
	  return true;
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isTransparent() const
  public final boolean isTransparent()
  {
	return _isTransparent;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: URL getFeatureURL(const Geodetic2D& g, const IFactory* factory, const Sector& tileSector, int width, int height) const
  public final URL getFeatureURL(Geodetic2D g, IFactory factory, Sector tileSector, int width, int height)
  {
	if (!_bbox.touchesWith(tileSector))
	{
	  return URL.null(_);
	}
  
	final Sector sector = tileSector.intersection(_bbox);
  
	  //Server name
	String req = _serverURL;
	  if (req.charAt(req.length()-1) != '?')
	  {
		  req += '?';
	  }
  
	//If the server refer to itself as localhost...
	int pos = req.indexOf("localhost");
	if (pos != -1)
	{
	  req = req.substring(pos+9);
  
	  int pos2 = _serverURL.indexOf("/", 8);
	  String newHost = _serverURL.substring(0, pos2);
  
	  req = newHost + req;
	}
  
	req += "REQUEST=GetFeatureInfo&SERVICE=WMS";
	switch (_serverVersion)
	{
	  case WMS_1_3_0:
	  {
		req += "&VERSION=1.3.0";
  
		req += "&CRS=EPSG:4326";
	  }
		break;
  
	  case WMS_1_1_0:
	  default:
	  {
		// default is 1.1.1
		req += "&VERSION=1.1.1";
  
		break;
	  }
	}
  
  
	//SRS
	if (!_srs.equals(""))
	{
	  req += "&SRS=" + _srs;
	}
	  else
	  {
	  req += "&SRS=EPSG:4326";
	}
  
	switch (_serverVersion)
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
	req += "&LAYERS=" + _mapLayers;
	//req += "&LAYERS=" + _queryLayers;
	req += "&QUERY_LAYERS=" + _queryLayers;
  
	req += "&INFO_FORMAT=text/plain";
  
	//X and Y
	Vector2D pixel = tileSector.getUVCoordinates(g);
	int x = (int) round((pixel.x() * width));
	int y = (int) round (((1.0 - pixel.y()) * height));
  
	IStringBuilder isb = IStringBuilder.newStringBuilder();
	isb.add("&X=").add(x).add("&Y=").add(y);
	req += isb.getString();
	if (isb != null)
		isb.dispose();
  
	  return new URL(req);
  }

}