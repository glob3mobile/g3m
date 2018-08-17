package org.glob3.mobile.generated;import java.util.*;

//
//  MercatorTiledLayer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/8/13.
//
//

//
//  MercatorTiledLayer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/8/13.
//
//




public class MercatorTiledLayer extends RasterLayer
{
  protected final String _protocol;
  protected final String _domain;
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  protected final java.util.ArrayList<String> _subdomains = new java.util.ArrayList<String>();
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  protected final java.util.ArrayList<String> _subdomains = new protected();
//#endif
  protected final String _imageFormat;

  protected final int _initialLevel;
  protected final int _maxLevel;
  protected final boolean _isTransparent;

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual String getLayerType() const
  protected String getLayerType()
  {
	return "MercatorTiled";
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean rawIsEquals(const Layer* that) const
  protected boolean rawIsEquals(Layer that)
  {
	MercatorTiledLayer t = (MercatorTiledLayer) that;
  
	if (!_protocol.equals(t._protocol))
	{
	  return false;
	}
  
	if (!_domain.equals(t._domain))
	{
	  return false;
	}
  
	if (!_imageFormat.equals(t._imageFormat))
	{
	  return false;
	}
  
	if (_initialLevel != t._initialLevel)
	{
	  return false;
	}
  
	if (_maxLevel != t._maxLevel)
	{
	  return false;
	}
  
	final int thisSubdomainsSize = _subdomains.size();
	final int thatSubdomainsSize = t._subdomains.size();
  
	if (thisSubdomainsSize != thatSubdomainsSize)
	{
	  return false;
	}
  
	for (int i = 0; i < thisSubdomainsSize; i++)
	{
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	  final String thisSubdomain = _subdomains.get(i);
	  final String thatSubdomain = t._subdomains.get(i);
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	  final String thisSubdomain = _subdomains.get(i);
	  final String thatSubdomain = t._subdomains.get(i);
//#endif
	  if (!thisSubdomain.equals(thatSubdomain))
	  {
		return false;
	  }
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
  
	if (tile == tileP)
	{
	  //Most common case tile of suitable level being fully coveraged by layer
	  return ((_transparency < 1) ? TileImageContribution.fullCoverageTransparent(_transparency) : TileImageContribution.fullCoverageOpaque());
	}
  
	final Sector requestedImageSector = tileP._sector;
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return ((_transparency < 1) ? TileImageContribution::partialCoverageTransparent(requestedImageSector, _transparency) : TileImageContribution::partialCoverageOpaque(requestedImageSector));
	return ((_transparency < 1) ? TileImageContribution.partialCoverageTransparent(new Sector(requestedImageSector), _transparency) : TileImageContribution.partialCoverageOpaque(new Sector(requestedImageSector)));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const URL createURL(const Tile* tile) const
  protected final URL createURL(Tile tile)
  {
	final IMathUtils mu = IMathUtils.instance();
  
	final int level = tile._level;
	final int column = tile._column;
	final int numRows = (int) mu.pow(2.0, level);
	final int row = numRows - tile._row - 1;
  
	IStringBuilder isb = IStringBuilder.newStringBuilder();
  
	isb.addString(_protocol);
  
	final int subdomainsSize = _subdomains.size();
	if (subdomainsSize > 0)
	{
	  // select subdomain based on fixed data (instead of round-robin) to be cache friendly
	  final int subdomainsIndex = mu.abs(level + column + row) % subdomainsSize;
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	  isb.addString(_subdomains.get(subdomainsIndex));
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	  isb.addString(_subdomains.get(subdomainsIndex));
//#endif
	}
  
	isb.addString(_domain);
	isb.addString("/");
  
	isb.addInt(level);
	isb.addString("/");
  
	isb.addInt(column);
	isb.addString("/");
  
	isb.addInt(row);
	isb.addString(".");
	isb.addString(_imageFormat);
  
	final String path = isb.getString();
  
	if (isb != null)
		isb.dispose();
  
	return new URL(path, false);
  }


  /*
   Implementation details: http: //wiki.openstreetmap.org/wiki/Slippy_map_tilenames
   */
  
  public MercatorTiledLayer(String protocol, String domain, java.util.ArrayList<String> subdomains, String imageFormat, TimeInterval timeToCache, boolean readExpired, int initialLevel, int maxLevel, boolean isTransparent, float transparency, LayerCondition condition)
  {
	  this(protocol, domain, subdomains, imageFormat, timeToCache, readExpired, initialLevel, maxLevel, isTransparent, transparency, condition, new java.util.ArrayList<const Info*>());
  }
  public MercatorTiledLayer(String protocol, String domain, java.util.ArrayList<String> subdomains, String imageFormat, TimeInterval timeToCache, boolean readExpired, int initialLevel, int maxLevel, boolean isTransparent, float transparency)
  {
	  this(protocol, domain, subdomains, imageFormat, timeToCache, readExpired, initialLevel, maxLevel, isTransparent, transparency, null, new java.util.ArrayList<const Info*>());
  }
  public MercatorTiledLayer(String protocol, String domain, java.util.ArrayList<String> subdomains, String imageFormat, TimeInterval timeToCache, boolean readExpired, int initialLevel, int maxLevel, boolean isTransparent)
  {
	  this(protocol, domain, subdomains, imageFormat, timeToCache, readExpired, initialLevel, maxLevel, isTransparent, 1, null, new java.util.ArrayList<const Info*>());
  }
  public MercatorTiledLayer(String protocol, String domain, java.util.ArrayList<String> subdomains, String imageFormat, TimeInterval timeToCache, boolean readExpired, int initialLevel, int maxLevel)
  {
	  this(protocol, domain, subdomains, imageFormat, timeToCache, readExpired, initialLevel, maxLevel, false, 1, null, new java.util.ArrayList<const Info*>());
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: MercatorTiledLayer(const String& protocol, const String& domain, const java.util.ArrayList<String>& subdomains, const String& imageFormat, const TimeInterval& timeToCache, const boolean readExpired, const int initialLevel, const int maxLevel, const boolean isTransparent = false, const float transparency = 1, const LayerCondition* condition = null, java.util.ArrayList<const Info*>* layerInfo = new java.util.ArrayList<const Info*>()) : RasterLayer(timeToCache, readExpired, new LayerTilesRenderParameters(Sector::fullSphere(), 1, 1, initialLevel, maxLevel, Vector2I(256, 256), LayerTilesRenderParameters::defaultTileMeshResolution(), true), transparency, condition, layerInfo), _protocol(protocol), _domain(domain), _subdomains(subdomains), _imageFormat(imageFormat), _initialLevel(initialLevel), _maxLevel(maxLevel), _isTransparent(isTransparent)
  public MercatorTiledLayer(String protocol, String domain, java.util.ArrayList<String> subdomains, String imageFormat, TimeInterval timeToCache, boolean readExpired, int initialLevel, int maxLevel, boolean isTransparent, float transparency, LayerCondition condition, java.util.ArrayList<Info> layerInfo)
  {
	  super(timeToCache, readExpired, new LayerTilesRenderParameters(Sector.fullSphere(), 1, 1, initialLevel, maxLevel, new Vector2I(256, 256), LayerTilesRenderParameters.defaultTileMeshResolution(), true), transparency, condition, layerInfo);
	  _protocol = protocol;
	  _domain = domain;
	  _subdomains = subdomains;
	  _imageFormat = imageFormat;
	  _initialLevel = initialLevel;
	  _maxLevel = maxLevel;
	  _isTransparent = isTransparent;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: URL getFeatureInfoURL(const Geodetic2D& position, const Sector& sector) const
  public final URL getFeatureInfoURL(Geodetic2D position, Sector sector)
  {
	return new URL();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual const String description() const
  public String description()
  {
	return "[MercatorTiledLayer]";
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual MercatorTiledLayer* copy() const
  public MercatorTiledLayer copy()
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return new MercatorTiledLayer(_protocol, _domain, _subdomains, _imageFormat, _timeToCache, _readExpired, _initialLevel, _maxLevel, _isTransparent, _transparency, (_condition == null) ? null : _condition->copy(), _layerInfo);
	return new MercatorTiledLayer(_protocol, _domain, _subdomains, _imageFormat, new TimeInterval(_timeToCache), _readExpired, _initialLevel, _maxLevel, _isTransparent, _transparency, (_condition == null) ? null : _condition.copy(), _layerInfo);
  }

  public RenderState getRenderState()
  {
	return RenderState.ready();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Sector getDataSector() const
  public final Sector getDataSector()
  {
	return Sector.fullSphere();
  }

}
