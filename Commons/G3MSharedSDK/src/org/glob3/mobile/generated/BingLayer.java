package org.glob3.mobile.generated; 
public class BingLayer extends Layer
{

  private final Sector _sector ;
  private URL _mapServerURL = new URL();
  private final String _key;
  private final Language _locale;
  private final MapType _mapType;
  private String _tilePetitionString;
  private java.util.ArrayList<String> _subDomains = new java.util.ArrayList<String>();
  private boolean _isReady;






  public BingLayer(URL mapServerURL, LayerCondition condition, Sector sector, MapType mapType, Language locale, String key)
  {
	  super(condition);
	  _sector = new Sector(sector);
	  _mapServerURL = new URL(mapServerURL);
	  _mapType = mapType;
	  _locale = locale;
	  _key = key;
	  _tilePetitionString = new String();
	  _isReady = false;

  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isReady()const
  public final boolean isReady()
  {
	return _isReady;
  }

  public final void setTilePetitionString(String tilePetitionString)
  {
	_tilePetitionString = tilePetitionString;
	_isReady = true;
  }

  public final void setSubDomains(java.util.ArrayList<String> subDomains)
  {
	_subDomains = subDomains;
  }


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: String getLocale()const
  public final String getLocale()
  {
	if (_locale == Language.English)
	{
	  return "en-US";
	}
	if (_locale == Language.Spanish)
	{
	  return "es-ES";
	}
	if (_locale == Language.German)
	{
	  return "de-DE";
	}
	if (_locale == Language.French)
	{
	  return "fr-FR";
	}
	if (_locale == Language.Dutch)
	{
	  return "nl-BE";
	}
	if (_locale == Language.Italian)
	{
	  return "it-IT";
	}
	return "en-US";
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: String getMapTypeString() const
  public final String getMapTypeString()
  {
	if (_mapType == MapType.Road)
	{
	  return "Road";
	}
	if (_mapType == MapType.Aerial)
	{
	  return "Aerial";
	}
	if (_mapType == MapType.Hybrid)
	{
	  return "AerialWithLabels";
	}
	return "Aerial";
  }

  public final void initialize(InitializationContext ic)
  {
  
	String tileURL = "";
	tileURL+=_mapServerURL.getPath();
	tileURL+="/";
	tileURL+=getMapTypeString();
	tileURL+="?key=";
	tileURL+=_key;
  
	final URL url = new URL(tileURL, false);
	ic.getDownloader().requestBuffer(url, 100000000, new TokenDownloadListener(this), true);
  
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
  
  
	//Server name
	String req = _mapServerURL.getPath();
  
	//If the server refer to itself as localhost...
	int pos = req.indexOf("localhost");
	if (pos != -1)
	{
	  req = req.substring(pos+9);
  
	  int pos2 = req.indexOf("/", 8);
	  String newHost = req.substring(0, pos2);
  
	  req = newHost + req;
	}
  
	//Key:AgOLISvN2b3012i-odPJjVxhB1dyU6avZ2vG9Ub6Z9-mEpgZHre-1rE8o-DUinUH
  
	//TODO: calculate the level correctly
	int level = tile.getLevel()+2;
  
	xyTuple lowerTileXY = getTileXY(tileSector.lower(), level);
	xyTuple upperTileXY = getTileXY(tileSector.upper(), level);
  
	int deltaX = upperTileXY.x - lowerTileXY.x;
	int deltaY = lowerTileXY.y - upperTileXY.y;
  
	java.util.ArrayList<Integer> requiredTiles = new java.util.ArrayList<Integer>();
  
	int currentSubDomain = 0;
	int numSubDomains = _subDomains.size();
  
	for(int x = lowerTileXY.x; x<= lowerTileXY.x+deltaX; x++)
	{
	  for(int y = upperTileXY.y; y<=upperTileXY.y+deltaY; y++)
	  {
		int[] tileXY = new int[2];
		tileXY[0] = x;
		tileXY[1] = y;
		Sector bingSector = getBingTileAsSector(tileXY, level);
  
		if (!bingSector.touchesWith(tileSector))
		{
		  continue;
		}
  
		//set the quadkey
		String url = IStringUtils.instance().replaceSubstring(_tilePetitionString, "{quadkey}", getQuadKey(tileXY, level));
  
		//set the subDomain (round-robbin)
		url = IStringUtils.instance().replaceSubstring(url, "{subdomain}", _subDomains.get(currentSubDomain % numSubDomains));
		currentSubDomain++;
		petitions.add(new Petition(bingSector, new URL(url, false)));
  
	  }
  
	}
	if (lowerTileXY != null)
		lowerTileXY.dispose();
	if (upperTileXY != null)
		upperTileXY.dispose();
	return petitions;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: String getQuadKey(const int tileXY[], const int level)const
  public final String getQuadKey(int[] tileXY, int level)
  {
  
	int tileX = tileXY[0];
	int tileY = tileXY[1];
	String quadKey = "";
	for (int i = level; i>0; i--)
	{
	  byte digit = (byte)'0';
	  int mask = 1 << (i-1);
	  if ((tileX & mask) != 0)
	  {
		digit++;
	  }
	  if ((tileY & mask) != 0)
	  {
		digit++;
		digit++;
	  }
	  quadKey+=digit;
	}
  
	return quadKey;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: xyTuple* getTileXY(const Geodetic2D latLon, const int level)const
  public final xyTuple getTileXY(Geodetic2D latLon, int level)
  {
  
	//LatLon to Pixels XY
	int mapSize = (int) 256 << level;
	double lonDeg = latLon.longitude()._degrees;
	double latDeg = latLon.latitude()._degrees;
	if (latDeg < -85.05112878)
	{
	  latDeg = -85.05112878;
	}
	if (latDeg > 85.05112878)
	{
	  latDeg = 85.05112878;
	}
  
	double x = (lonDeg +180.0)/360;
	double sinLat = IMathUtils.instance().sin(latDeg *IMathUtils.instance().pi()/180.0);
	double y = 0.5-IMathUtils.instance().log((1+sinLat)/(1-sinLat))/(4.0 *IMathUtils.instance().pi());
  
	x = x * mapSize +0.5;
	y = y * mapSize +0.5;
  
  
	if (x<0)
		x = 0;
	if (y<0)
		y = 0;
	if (x>(mapSize-1))
		x = mapSize-1;
	if (y>(mapSize-1))
		y = mapSize-1;
  
	int pixelX = (int)x;
	int pixelY = (int)y;
  
	//Pixel XY to Tile XY
	int tileX = pixelX / 256;
	int tileY = pixelY / 256;
  
	xyTuple tileXY = new xyTuple();
  
	tileXY.x = tileX;
	tileXY.y = tileY;
  
	return tileXY;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Sector getBingTileAsSector(const int tileXY[], const int level)const
  public final Sector getBingTileAsSector(int[] tileXY, int level)
  {
  
  
	Geodetic2D topLeft = getLatLon(tileXY, level);
	int maxTile = ((int)IMathUtils.instance().pow((double)2, (double)level))-1;
  
	Angle lowerLon = topLeft.longitude();
	Angle upperLat = topLeft.latitude();
  
	int[] tileBelow = new int[2];
	tileBelow[0] = tileXY[0];
	double lowerLatDeg;
	if (tileXY[1]+1 > maxTile)
	{
	  lowerLatDeg = -85.05112878;
	}
	else
	{
	  tileBelow[1] = tileXY[1]+1;
	  lowerLatDeg = getLatLon(tileBelow, level).latitude()._degrees;
	}
  
  
	int[] tileRight = new int[2];
	double upperLonDeg;
	tileRight[1] = tileXY[1];
	if (tileXY[0]+1 > maxTile)
	{
	  upperLonDeg = 180.0;
	}
	else
	{
	  tileRight[0] = tileXY[0]+1;
	  upperLonDeg = getLatLon(tileRight, level).longitude()._degrees;
	}
  
	return new Sector(new Geodetic2D(Angle.fromDegrees(lowerLatDeg), lowerLon), new Geodetic2D(upperLat, Angle.fromDegrees(upperLonDeg)));
  
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Geodetic2D getLatLon(const int tileXY[], const int level)const
  public final Geodetic2D getLatLon(int[] tileXY, int level)
  {
  
  
	int pixelX = tileXY[0]*256;
	int pixelY = tileXY[1]*256;
  
	//Pixel XY to LatLon
	int mapSize = (int) 256 << level;
	if (pixelX < 0)
		pixelX = 0;
	if (pixelY < 0)
		pixelY = 0;
	if (pixelX > mapSize-1)
		pixelX = mapSize-1;
	if (pixelY > mapSize-1)
		pixelY = mapSize-1;
	double x = (((double)pixelX)/((double)mapSize)) - 0.5;
	double y = 0.5 - (((double)pixelY)/((double)mapSize));
  
	double latDeg = 90.0 - 360.0 * IMathUtils.instance().atan(IMathUtils.instance().exp(-y *2.0 *IMathUtils.instance().pi())) / IMathUtils.instance().pi();
	double lonDeg = 360.0 * x;
  
	return new Geodetic2D(Angle.fromDegrees(latDeg), Angle.fromDegrees(lonDeg));
  
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: URL getFeatureInfoURL(const Geodetic2D& g, const IFactory* factory, const Sector& tileSector, int width, int height) const
  public final URL getFeatureInfoURL(Geodetic2D g, IFactory factory, Sector tileSector, int width, int height)
  {
	return URL.nullURL();
  
  }




//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isTransparent() const
  public final boolean isTransparent()
  {
	return false;
  }
}