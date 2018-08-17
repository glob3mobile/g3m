package org.glob3.mobile.generated;import java.util.*;

public class WMSLayer extends RasterLayer
{

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final URL _mapServerURL = new URL();
  private final URL _queryServerURL = new URL();
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public final URL _mapServerURL = new internal();
  public final URL _queryServerURL = new internal();
//#endif

  private final String _mapLayer;
  private final WMSServerVersion _mapServerVersion;
  private final String _queryLayer;
  private final WMSServerVersion _queryServerVersion;
  private final Sector _dataSector = new Sector();
  private final String _format;
  private final String _srs;
  private final String _style;
  private final boolean _isTransparent;
  private String _extraParameter;

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double toBBOXLongitude(const Angle& longitude) const
  private double toBBOXLongitude(Angle longitude)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return (_parameters->_mercator) ? MercatorUtils::longitudeToMeters(longitude) : longitude._degrees;
	return (_parameters._mercator) ? MercatorUtils.longitudeToMeters(new Angle(longitude)) : longitude._degrees;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double toBBOXLatitude(const Angle& latitude) const
  private double toBBOXLatitude(Angle latitude)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return (_parameters->_mercator) ? MercatorUtils::latitudeToMeters(latitude) : latitude._degrees;
	return (_parameters._mercator) ? MercatorUtils.latitudeToMeters(new Angle(latitude)) : latitude._degrees;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: String getLayerType() const
  protected final String getLayerType()
  {
	return "WMS";
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean rawIsEquals(const Layer* that) const
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


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const TileImageContribution* rawContribution(const Tile* tile) const
  protected final TileImageContribution rawContribution(Tile tile)
  {
	final Tile tileP = getParentTileOfSuitableLevel(tile);
	if (tileP == null)
	{
	  return null;
	}
  
	final Sector requestedImageSector = tileP._sector;
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: if (!_dataSector.touchesWith(requestedImageSector))
	if (!_dataSector.touchesWith(new Sector(requestedImageSector)))
	{
	  return null;
	}
  
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: else if (_dataSector.fullContains(requestedImageSector) && (tile == tileP))
	else if (_dataSector.fullContains(new Sector(requestedImageSector)) && (tile == tileP))
	{
	  //Most common case tile of suitable level being fully coveraged by layer
	  return ((_isTransparent || (_transparency < 1)) ? TileImageContribution.fullCoverageTransparent(_transparency) : TileImageContribution.fullCoverageOpaque());
	}
	else
	{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Sector contributionSector = _dataSector.intersection(requestedImageSector);
	  final Sector contributionSector = _dataSector.intersection(new Sector(requestedImageSector));
	  if (contributionSector.hasNoArea())
	  {
		return null;
	  }
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return ((_isTransparent || (_transparency < 1)) ? TileImageContribution::partialCoverageTransparent(contributionSector, _transparency) : TileImageContribution::partialCoverageOpaque(contributionSector));
	  return ((_isTransparent || (_transparency < 1)) ? TileImageContribution.partialCoverageTransparent(new Sector(contributionSector), _transparency) : TileImageContribution.partialCoverageOpaque(new Sector(contributionSector)));
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const URL createURL(const Tile* tile) const
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
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Sector sector = tileSector.intersection(_dataSector);
	final Sector sector = tileSector.intersection(new Sector(_dataSector));
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
	  return newMercator(mapLayer, mapServerURL, mapServerVersion, queryLayer, queryServerURL, queryServerVersion, dataSector, format, style, isTransparent, firstLevel, maxLevel, condition, timeToCache, readExpired, transparency, new java.util.ArrayList<const Info*>());
  }
  public static WMSLayer newMercator(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, String queryLayer, URL queryServerURL, WMSServerVersion queryServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel, int maxLevel, LayerCondition condition, TimeInterval timeToCache, boolean readExpired)
  {
	  return newMercator(mapLayer, mapServerURL, mapServerVersion, queryLayer, queryServerURL, queryServerVersion, dataSector, format, style, isTransparent, firstLevel, maxLevel, condition, timeToCache, readExpired, 1, new java.util.ArrayList<const Info*>());
  }
  public static WMSLayer newMercator(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, String queryLayer, URL queryServerURL, WMSServerVersion queryServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel, int maxLevel, LayerCondition condition, TimeInterval timeToCache)
  {
	  return newMercator(mapLayer, mapServerURL, mapServerVersion, queryLayer, queryServerURL, queryServerVersion, dataSector, format, style, isTransparent, firstLevel, maxLevel, condition, timeToCache, true, 1, new java.util.ArrayList<const Info*>());
  }
  public static WMSLayer newMercator(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, String queryLayer, URL queryServerURL, WMSServerVersion queryServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel, int maxLevel, LayerCondition condition)
  {
	  return newMercator(mapLayer, mapServerURL, mapServerVersion, queryLayer, queryServerURL, queryServerVersion, dataSector, format, style, isTransparent, firstLevel, maxLevel, condition, TimeInterval.fromDays(30), true, 1, new java.util.ArrayList<const Info*>());
  }
  public static WMSLayer newMercator(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, String queryLayer, URL queryServerURL, WMSServerVersion queryServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel, int maxLevel)
  {
	  return newMercator(mapLayer, mapServerURL, mapServerVersion, queryLayer, queryServerURL, queryServerVersion, dataSector, format, style, isTransparent, firstLevel, maxLevel, null, TimeInterval.fromDays(30), true, 1, new java.util.ArrayList<const Info*>());
  }
  public static WMSLayer newMercator(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, String queryLayer, URL queryServerURL, WMSServerVersion queryServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel)
  {
	  return newMercator(mapLayer, mapServerURL, mapServerVersion, queryLayer, queryServerURL, queryServerVersion, dataSector, format, style, isTransparent, firstLevel, 17, null, TimeInterval.fromDays(30), true, 1, new java.util.ArrayList<const Info*>());
  }
  public static WMSLayer newMercator(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, String queryLayer, URL queryServerURL, WMSServerVersion queryServerVersion, Sector dataSector, String format, String style, boolean isTransparent)
  {
	  return newMercator(mapLayer, mapServerURL, mapServerVersion, queryLayer, queryServerURL, queryServerVersion, dataSector, format, style, isTransparent, 2, 17, null, TimeInterval.fromDays(30), true, 1, new java.util.ArrayList<const Info*>());
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: static WMSLayer* newMercator(const String& mapLayer, const URL& mapServerURL, const WMSServerVersion mapServerVersion, const String& queryLayer, const URL& queryServerURL, const WMSServerVersion queryServerVersion, const Sector& dataSector, const String& format, const String& style, const boolean isTransparent, const int firstLevel = 2, const int maxLevel = 17, const LayerCondition* condition = null, const TimeInterval& timeToCache = TimeInterval::fromDays(30), const boolean readExpired = true, const float transparency = 1, java.util.ArrayList<const Info*>* layerInfo = new java.util.ArrayList<const Info*>())
  public static WMSLayer newMercator(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, String queryLayer, URL queryServerURL, WMSServerVersion queryServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel, int maxLevel, LayerCondition condition, TimeInterval timeToCache, boolean readExpired, float transparency, java.util.ArrayList<Info> layerInfo)
  {
  //  if (srs.compare("EPSG:4326") == 0) {
  //    layerTilesRenderParameters = LayerTilesRenderParameters::createDefaultWGS84(0, 17);
  //  }
  //  else if (srs.compare("EPSG:3857") == 0) {
  //    layerTilesRenderParameters = LayerTilesRenderParameters::createDefaultMercator(0, 17);
  //  }
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return new WMSLayer(mapLayer, mapServerURL, mapServerVersion, queryLayer, queryServerURL, queryServerVersion, dataSector, format, "EPSG:3857", style, isTransparent, condition, timeToCache, readExpired, LayerTilesRenderParameters::createDefaultMercator(firstLevel, maxLevel), transparency, layerInfo);
	return new WMSLayer(mapLayer, new URL(mapServerURL), mapServerVersion, queryLayer, new URL(queryServerURL), queryServerVersion, new Sector(dataSector), format, "EPSG:3857", style, isTransparent, condition, new TimeInterval(timeToCache), readExpired, LayerTilesRenderParameters.createDefaultMercator(firstLevel, maxLevel), transparency, layerInfo);
  }

  public static WMSLayer newMercator(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel, int maxLevel, LayerCondition condition, TimeInterval timeToCache, boolean readExpired, float transparency)
  {
	  return newMercator(mapLayer, mapServerURL, mapServerVersion, dataSector, format, style, isTransparent, firstLevel, maxLevel, condition, timeToCache, readExpired, transparency, new java.util.ArrayList<const Info*>());
  }
  public static WMSLayer newMercator(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel, int maxLevel, LayerCondition condition, TimeInterval timeToCache, boolean readExpired)
  {
	  return newMercator(mapLayer, mapServerURL, mapServerVersion, dataSector, format, style, isTransparent, firstLevel, maxLevel, condition, timeToCache, readExpired, 1, new java.util.ArrayList<const Info*>());
  }
  public static WMSLayer newMercator(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel, int maxLevel, LayerCondition condition, TimeInterval timeToCache)
  {
	  return newMercator(mapLayer, mapServerURL, mapServerVersion, dataSector, format, style, isTransparent, firstLevel, maxLevel, condition, timeToCache, true, 1, new java.util.ArrayList<const Info*>());
  }
  public static WMSLayer newMercator(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel, int maxLevel, LayerCondition condition)
  {
	  return newMercator(mapLayer, mapServerURL, mapServerVersion, dataSector, format, style, isTransparent, firstLevel, maxLevel, condition, TimeInterval.fromDays(30), true, 1, new java.util.ArrayList<const Info*>());
  }
  public static WMSLayer newMercator(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel, int maxLevel)
  {
	  return newMercator(mapLayer, mapServerURL, mapServerVersion, dataSector, format, style, isTransparent, firstLevel, maxLevel, null, TimeInterval.fromDays(30), true, 1, new java.util.ArrayList<const Info*>());
  }
  public static WMSLayer newMercator(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel)
  {
	  return newMercator(mapLayer, mapServerURL, mapServerVersion, dataSector, format, style, isTransparent, firstLevel, 17, null, TimeInterval.fromDays(30), true, 1, new java.util.ArrayList<const Info*>());
  }
  public static WMSLayer newMercator(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, Sector dataSector, String format, String style, boolean isTransparent)
  {
	  return newMercator(mapLayer, mapServerURL, mapServerVersion, dataSector, format, style, isTransparent, 2, 17, null, TimeInterval.fromDays(30), true, 1, new java.util.ArrayList<const Info*>());
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: static WMSLayer* newMercator(const String& mapLayer, const URL& mapServerURL, const WMSServerVersion mapServerVersion, const Sector& dataSector, const String& format, const String& style, const boolean isTransparent, const int firstLevel = 2, const int maxLevel = 17, const LayerCondition* condition = null, const TimeInterval& timeToCache = TimeInterval::fromDays(30), const boolean readExpired = true, const float transparency = 1, java.util.ArrayList<const Info*>* layerInfo = new java.util.ArrayList<const Info*>())
  public static WMSLayer newMercator(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel, int maxLevel, LayerCondition condition, TimeInterval timeToCache, boolean readExpired, float transparency, java.util.ArrayList<Info> layerInfo)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return new WMSLayer(mapLayer, mapServerURL, mapServerVersion, dataSector, format, "EPSG:3857", style, isTransparent, condition, timeToCache, readExpired, LayerTilesRenderParameters::createDefaultMercator(firstLevel, maxLevel), transparency, layerInfo);
	return new WMSLayer(mapLayer, new URL(mapServerURL), mapServerVersion, new Sector(dataSector), format, "EPSG:3857", style, isTransparent, condition, new TimeInterval(timeToCache), readExpired, LayerTilesRenderParameters.createDefaultMercator(firstLevel, maxLevel), transparency, layerInfo);
  }

  public static WMSLayer newWGS84(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, String queryLayer, URL queryServerURL, WMSServerVersion queryServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel, int maxLevel, LayerCondition condition, TimeInterval timeToCache, boolean readExpired, float transparency)
  {
	  return newWGS84(mapLayer, mapServerURL, mapServerVersion, queryLayer, queryServerURL, queryServerVersion, dataSector, format, style, isTransparent, firstLevel, maxLevel, condition, timeToCache, readExpired, transparency, new java.util.ArrayList<const Info*>());
  }
  public static WMSLayer newWGS84(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, String queryLayer, URL queryServerURL, WMSServerVersion queryServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel, int maxLevel, LayerCondition condition, TimeInterval timeToCache, boolean readExpired)
  {
	  return newWGS84(mapLayer, mapServerURL, mapServerVersion, queryLayer, queryServerURL, queryServerVersion, dataSector, format, style, isTransparent, firstLevel, maxLevel, condition, timeToCache, readExpired, 1, new java.util.ArrayList<const Info*>());
  }
  public static WMSLayer newWGS84(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, String queryLayer, URL queryServerURL, WMSServerVersion queryServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel, int maxLevel, LayerCondition condition, TimeInterval timeToCache)
  {
	  return newWGS84(mapLayer, mapServerURL, mapServerVersion, queryLayer, queryServerURL, queryServerVersion, dataSector, format, style, isTransparent, firstLevel, maxLevel, condition, timeToCache, true, 1, new java.util.ArrayList<const Info*>());
  }
  public static WMSLayer newWGS84(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, String queryLayer, URL queryServerURL, WMSServerVersion queryServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel, int maxLevel, LayerCondition condition)
  {
	  return newWGS84(mapLayer, mapServerURL, mapServerVersion, queryLayer, queryServerURL, queryServerVersion, dataSector, format, style, isTransparent, firstLevel, maxLevel, condition, TimeInterval.fromDays(30), true, 1, new java.util.ArrayList<const Info*>());
  }
  public static WMSLayer newWGS84(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, String queryLayer, URL queryServerURL, WMSServerVersion queryServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel, int maxLevel)
  {
	  return newWGS84(mapLayer, mapServerURL, mapServerVersion, queryLayer, queryServerURL, queryServerVersion, dataSector, format, style, isTransparent, firstLevel, maxLevel, null, TimeInterval.fromDays(30), true, 1, new java.util.ArrayList<const Info*>());
  }
  public static WMSLayer newWGS84(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, String queryLayer, URL queryServerURL, WMSServerVersion queryServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel)
  {
	  return newWGS84(mapLayer, mapServerURL, mapServerVersion, queryLayer, queryServerURL, queryServerVersion, dataSector, format, style, isTransparent, firstLevel, 17, null, TimeInterval.fromDays(30), true, 1, new java.util.ArrayList<const Info*>());
  }
  public static WMSLayer newWGS84(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, String queryLayer, URL queryServerURL, WMSServerVersion queryServerVersion, Sector dataSector, String format, String style, boolean isTransparent)
  {
	  return newWGS84(mapLayer, mapServerURL, mapServerVersion, queryLayer, queryServerURL, queryServerVersion, dataSector, format, style, isTransparent, 1, 17, null, TimeInterval.fromDays(30), true, 1, new java.util.ArrayList<const Info*>());
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: static WMSLayer* newWGS84(const String& mapLayer, const URL& mapServerURL, const WMSServerVersion mapServerVersion, const String& queryLayer, const URL& queryServerURL, const WMSServerVersion queryServerVersion, const Sector& dataSector, const String& format, const String& style, const boolean isTransparent, const int firstLevel = 1, const int maxLevel = 17, const LayerCondition* condition = null, const TimeInterval& timeToCache = TimeInterval::fromDays(30), const boolean readExpired = true, const float transparency = 1, java.util.ArrayList<const Info*>* layerInfo = new java.util.ArrayList<const Info*>())
  public static WMSLayer newWGS84(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, String queryLayer, URL queryServerURL, WMSServerVersion queryServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel, int maxLevel, LayerCondition condition, TimeInterval timeToCache, boolean readExpired, float transparency, java.util.ArrayList<Info> layerInfo)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return new WMSLayer(mapLayer, mapServerURL, mapServerVersion, queryLayer, queryServerURL, queryServerVersion, dataSector, format, "EPSG:4326", style, isTransparent, condition, timeToCache, readExpired, LayerTilesRenderParameters::createDefaultWGS84(firstLevel, maxLevel), transparency, layerInfo);
	return new WMSLayer(mapLayer, new URL(mapServerURL), mapServerVersion, queryLayer, new URL(queryServerURL), queryServerVersion, new Sector(dataSector), format, "EPSG:4326", style, isTransparent, condition, new TimeInterval(timeToCache), readExpired, LayerTilesRenderParameters.createDefaultWGS84(firstLevel, maxLevel), transparency, layerInfo);
  }

  public static WMSLayer newWGS84(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel, int maxLevel, LayerCondition condition, TimeInterval timeToCache, boolean readExpired, float transparency)
  {
	  return newWGS84(mapLayer, mapServerURL, mapServerVersion, dataSector, format, style, isTransparent, firstLevel, maxLevel, condition, timeToCache, readExpired, transparency, new java.util.ArrayList<const Info*>());
  }
  public static WMSLayer newWGS84(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel, int maxLevel, LayerCondition condition, TimeInterval timeToCache, boolean readExpired)
  {
	  return newWGS84(mapLayer, mapServerURL, mapServerVersion, dataSector, format, style, isTransparent, firstLevel, maxLevel, condition, timeToCache, readExpired, 1, new java.util.ArrayList<const Info*>());
  }
  public static WMSLayer newWGS84(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel, int maxLevel, LayerCondition condition, TimeInterval timeToCache)
  {
	  return newWGS84(mapLayer, mapServerURL, mapServerVersion, dataSector, format, style, isTransparent, firstLevel, maxLevel, condition, timeToCache, true, 1, new java.util.ArrayList<const Info*>());
  }
  public static WMSLayer newWGS84(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel, int maxLevel, LayerCondition condition)
  {
	  return newWGS84(mapLayer, mapServerURL, mapServerVersion, dataSector, format, style, isTransparent, firstLevel, maxLevel, condition, TimeInterval.fromDays(30), true, 1, new java.util.ArrayList<const Info*>());
  }
  public static WMSLayer newWGS84(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel, int maxLevel)
  {
	  return newWGS84(mapLayer, mapServerURL, mapServerVersion, dataSector, format, style, isTransparent, firstLevel, maxLevel, null, TimeInterval.fromDays(30), true, 1, new java.util.ArrayList<const Info*>());
  }
  public static WMSLayer newWGS84(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel)
  {
	  return newWGS84(mapLayer, mapServerURL, mapServerVersion, dataSector, format, style, isTransparent, firstLevel, 17, null, TimeInterval.fromDays(30), true, 1, new java.util.ArrayList<const Info*>());
  }
  public static WMSLayer newWGS84(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, Sector dataSector, String format, String style, boolean isTransparent)
  {
	  return newWGS84(mapLayer, mapServerURL, mapServerVersion, dataSector, format, style, isTransparent, 1, 17, null, TimeInterval.fromDays(30), true, 1, new java.util.ArrayList<const Info*>());
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: static WMSLayer* newWGS84(const String& mapLayer, const URL& mapServerURL, const WMSServerVersion mapServerVersion, const Sector& dataSector, const String& format, const String& style, const boolean isTransparent, const int firstLevel = 1, const int maxLevel = 17, const LayerCondition* condition = null, const TimeInterval& timeToCache = TimeInterval::fromDays(30), const boolean readExpired = true, const float transparency = 1, java.util.ArrayList<const Info*>* layerInfo = new java.util.ArrayList<const Info*>())
  public static WMSLayer newWGS84(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, Sector dataSector, String format, String style, boolean isTransparent, int firstLevel, int maxLevel, LayerCondition condition, TimeInterval timeToCache, boolean readExpired, float transparency, java.util.ArrayList<Info> layerInfo)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return new WMSLayer(mapLayer, mapServerURL, mapServerVersion, dataSector, format, "EPSG:4326", style, isTransparent, condition, timeToCache, readExpired, LayerTilesRenderParameters::createDefaultWGS84(firstLevel, maxLevel), transparency, layerInfo);
	return new WMSLayer(mapLayer, new URL(mapServerURL), mapServerVersion, new Sector(dataSector), format, "EPSG:4326", style, isTransparent, condition, new TimeInterval(timeToCache), readExpired, LayerTilesRenderParameters.createDefaultWGS84(firstLevel, maxLevel), transparency, layerInfo);
  }

  public WMSLayer(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, String queryLayer, URL queryServerURL, WMSServerVersion queryServerVersion, Sector dataSector, String format, String srs, String style, boolean isTransparent, LayerCondition condition, TimeInterval timeToCache, boolean readExpired, LayerTilesRenderParameters parameters, float transparency)
  {
	  this(mapLayer, mapServerURL, mapServerVersion, queryLayer, queryServerURL, queryServerVersion, dataSector, format, srs, style, isTransparent, condition, timeToCache, readExpired, parameters, transparency, new java.util.ArrayList<const Info*>());
  }
  public WMSLayer(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, String queryLayer, URL queryServerURL, WMSServerVersion queryServerVersion, Sector dataSector, String format, String srs, String style, boolean isTransparent, LayerCondition condition, TimeInterval timeToCache, boolean readExpired, LayerTilesRenderParameters parameters)
  {
	  this(mapLayer, mapServerURL, mapServerVersion, queryLayer, queryServerURL, queryServerVersion, dataSector, format, srs, style, isTransparent, condition, timeToCache, readExpired, parameters, 1, new java.util.ArrayList<const Info*>());
  }
  public WMSLayer(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, String queryLayer, URL queryServerURL, WMSServerVersion queryServerVersion, Sector dataSector, String format, String srs, String style, boolean isTransparent, LayerCondition condition, TimeInterval timeToCache, boolean readExpired)
  {
	  this(mapLayer, mapServerURL, mapServerVersion, queryLayer, queryServerURL, queryServerVersion, dataSector, format, srs, style, isTransparent, condition, timeToCache, readExpired, null, 1, new java.util.ArrayList<const Info*>());
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: WMSLayer(const String& mapLayer, const URL& mapServerURL, const WMSServerVersion mapServerVersion, const String& queryLayer, const URL& queryServerURL, const WMSServerVersion queryServerVersion, const Sector& dataSector, const String& format, const String& srs, const String& style, const boolean isTransparent, const LayerCondition* condition, const TimeInterval& timeToCache, const boolean readExpired, const LayerTilesRenderParameters* parameters = null, const float transparency = 1, java.util.ArrayList<const Info*>* layerInfo = new java.util.ArrayList<const Info*>()): RasterLayer(timeToCache, readExpired, (parameters == null) ? LayerTilesRenderParameters::createDefaultWGS84(Sector::fullSphere(), 0, 17) : parameters, transparency, condition, layerInfo), _mapLayer(mapLayer), _mapServerURL(mapServerURL), _mapServerVersion(mapServerVersion), _dataSector(dataSector), _queryLayer(queryLayer), _queryServerURL(queryServerURL), _queryServerVersion(queryServerVersion), _format(format), _srs(srs), _style(style), _isTransparent(isTransparent), _extraParameter("")
  public WMSLayer(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, String queryLayer, URL queryServerURL, WMSServerVersion queryServerVersion, Sector dataSector, String format, String srs, String style, boolean isTransparent, LayerCondition condition, TimeInterval timeToCache, boolean readExpired, LayerTilesRenderParameters parameters, float transparency, java.util.ArrayList<Info> layerInfo)
  {
	  super(timeToCache, readExpired, (parameters == null) ? LayerTilesRenderParameters.createDefaultWGS84(Sector.fullSphere(), 0, 17) : parameters, transparency, condition, layerInfo);
	  _mapLayer = mapLayer;
	  _mapServerURL = new URL(mapServerURL);
	  _mapServerVersion = mapServerVersion;
	  _dataSector = new Sector(dataSector);
	  _queryLayer = queryLayer;
	  _queryServerURL = new URL(queryServerURL);
	  _queryServerVersion = queryServerVersion;
	  _format = format;
	  _srs = srs;
	  _style = style;
	  _isTransparent = isTransparent;
	  _extraParameter = "";
  }

  public WMSLayer(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, Sector dataSector, String format, String srs, String style, boolean isTransparent, LayerCondition condition, TimeInterval timeToCache, boolean readExpired, LayerTilesRenderParameters parameters, float transparency)
  {
	  this(mapLayer, mapServerURL, mapServerVersion, dataSector, format, srs, style, isTransparent, condition, timeToCache, readExpired, parameters, transparency, new java.util.ArrayList<const Info*>());
  }
  public WMSLayer(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, Sector dataSector, String format, String srs, String style, boolean isTransparent, LayerCondition condition, TimeInterval timeToCache, boolean readExpired, LayerTilesRenderParameters parameters)
  {
	  this(mapLayer, mapServerURL, mapServerVersion, dataSector, format, srs, style, isTransparent, condition, timeToCache, readExpired, parameters, 1, new java.util.ArrayList<const Info*>());
  }
  public WMSLayer(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, Sector dataSector, String format, String srs, String style, boolean isTransparent, LayerCondition condition, TimeInterval timeToCache, boolean readExpired)
  {
	  this(mapLayer, mapServerURL, mapServerVersion, dataSector, format, srs, style, isTransparent, condition, timeToCache, readExpired, null, 1, new java.util.ArrayList<const Info*>());
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: WMSLayer(const String& mapLayer, const URL& mapServerURL, const WMSServerVersion mapServerVersion, const Sector& dataSector, const String& format, const String& srs, const String& style, const boolean isTransparent, const LayerCondition* condition, const TimeInterval& timeToCache, const boolean readExpired, const LayerTilesRenderParameters* parameters = null, const float transparency = 1, java.util.ArrayList<const Info*>* layerInfo = new java.util.ArrayList<const Info*>()): RasterLayer(timeToCache, readExpired, (parameters == null) ? LayerTilesRenderParameters::createDefaultWGS84(Sector::fullSphere(), 0, 17) : parameters, transparency, condition, layerInfo), _mapLayer(mapLayer), _mapServerURL(mapServerURL), _mapServerVersion(mapServerVersion), _dataSector(dataSector), _queryLayer(mapLayer), _queryServerURL(mapServerURL), _queryServerVersion(mapServerVersion), _format(format), _srs(srs), _style(style), _isTransparent(isTransparent), _extraParameter("")
  public WMSLayer(String mapLayer, URL mapServerURL, WMSServerVersion mapServerVersion, Sector dataSector, String format, String srs, String style, boolean isTransparent, LayerCondition condition, TimeInterval timeToCache, boolean readExpired, LayerTilesRenderParameters parameters, float transparency, java.util.ArrayList<Info> layerInfo)
  {
	  super(timeToCache, readExpired, (parameters == null) ? LayerTilesRenderParameters.createDefaultWGS84(Sector.fullSphere(), 0, 17) : parameters, transparency, condition, layerInfo);
	  _mapLayer = mapLayer;
	  _mapServerURL = new URL(mapServerURL);
	  _mapServerVersion = mapServerVersion;
	  _dataSector = new Sector(dataSector);
	  _queryLayer = mapLayer;
	  _queryServerURL = new URL(mapServerURL);
	  _queryServerVersion = mapServerVersion;
	  _format = format;
	  _srs = srs;
	  _style = style;
	  _isTransparent = isTransparent;
	  _extraParameter = "";
  
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: URL getFeatureInfoURL(const Geodetic2D& position, const Sector& tileSector) const
  public final URL getFeatureInfoURL(Geodetic2D position, Sector tileSector)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: if (!_dataSector.touchesWith(tileSector))
	if (!_dataSector.touchesWith(new Sector(tileSector)))
	{
	  return URL.nullURL();
	}
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Sector intersectionSector = tileSector.intersection(_dataSector);
	final Sector intersectionSector = tileSector.intersection(new Sector(_dataSector));
  
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
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector2D uv = intersectionSector.getUVCoordinates(position);
	  final Vector2D uv = intersectionSector.getUVCoordinates(new Geodetic2D(position));
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String description() const
  public final String description()
  {
	return "[WMSLayer]";
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: WMSLayer* copy() const
  public final WMSLayer copy()
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return new WMSLayer(_mapLayer, _mapServerURL, _mapServerVersion, _queryLayer, _queryServerURL, _queryServerVersion, _dataSector, _format, _srs, _style, _isTransparent, (_condition == null) ? null : _condition->copy(), _timeToCache, _readExpired, (_parameters == null) ? null : _parameters->copy(), _transparency, _layerInfo);
	return new WMSLayer(_mapLayer, new URL(_mapServerURL), _mapServerVersion, _queryLayer, new URL(_queryServerURL), _queryServerVersion, new Sector(_dataSector), _format, _srs, _style, _isTransparent, (_condition == null) ? null : _condition.copy(), new TimeInterval(_timeToCache), _readExpired, (_parameters == null) ? null : _parameters.copy(), _transparency, _layerInfo);
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Sector getDataSector() const
  public final Sector getDataSector()
  {
	return _dataSector;
  }

}
