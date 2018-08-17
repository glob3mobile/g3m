package org.glob3.mobile.generated;import java.util.*;

//
//  GoogleMapsLayer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/8/13.
//
//

//
//  GoogleMapsLayer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/8/13.
//
//



public class GoogleMapsLayer extends RasterLayer
{
  private final String _key;
  private final int _initialLevel;

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: String getLayerType() const
  protected final String getLayerType()
  {
	return "GoogleMaps";
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean rawIsEquals(const Layer* that) const
  protected final boolean rawIsEquals(Layer that)
  {
	GoogleMapsLayer t = (GoogleMapsLayer) that;
  
	if (!_key.equals(t._key))
	{
	  return false;
	}
  
	if (_initialLevel != t._initialLevel)
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
	final Sector tileSector = tile._sector;
  
	IStringBuilder isb = IStringBuilder.newStringBuilder();
  
	// http://maps.googleapis.com/maps/api/staticmap?center=New+York,NY&zoom=13&size=600x300&key=AIzaSyC9pospBjqsfpb0Y9N3E3uNMD8ELoQVOrc&sensor=false
  
	/*
	 http: //maps.googleapis.com/maps/api/staticmap
	 ?center=New+York,NY
	 &zoom=13
	 &size=600x300
	 &key=AIzaSyC9pospBjqsfpb0Y9N3E3uNMD8ELoQVOrc
	 &sensor=false
	 */
  
	isb.addString("http://maps.googleapis.com/maps/api/staticmap?sensor=false");
  
	isb.addString("&center=");
	isb.addDouble(tileSector._center._latitude._degrees);
	isb.addString(",");
	isb.addDouble(tileSector._center._longitude._degrees);
  
	final int level = tile._level;
	isb.addString("&zoom=");
	isb.addInt(level);
  
	isb.addString("&size=");
	isb.addInt(_parameters._tileTextureResolution._x);
	isb.addString("x");
	isb.addInt(_parameters._tileTextureResolution._y);
  
	isb.addString("&format=jpg");
  
  
	//  isb->addString("&maptype=roadmap);
	//  isb->addString("&maptype=satellite");
	isb.addString("&maptype=hybrid");
	//  isb->addString("&maptype=terrain");
  
  
	isb.addString("&key=");
	isb.addString(_key);
  
  
	final String path = isb.getString();
  
	if (isb != null)
		isb.dispose();
	return new URL(path, false);
  }


  public GoogleMapsLayer(String key, TimeInterval timeToCache, boolean readExpired, int initialLevel, float transparency, LayerCondition condition)
  {
	  this(key, timeToCache, readExpired, initialLevel, transparency, condition, new java.util.ArrayList<const Info*>());
  }
  public GoogleMapsLayer(String key, TimeInterval timeToCache, boolean readExpired, int initialLevel, float transparency)
  {
	  this(key, timeToCache, readExpired, initialLevel, transparency, null, new java.util.ArrayList<const Info*>());
  }
  public GoogleMapsLayer(String key, TimeInterval timeToCache, boolean readExpired, int initialLevel)
  {
	  this(key, timeToCache, readExpired, initialLevel, 1, null, new java.util.ArrayList<const Info*>());
  }
  public GoogleMapsLayer(String key, TimeInterval timeToCache, boolean readExpired)
  {
	  this(key, timeToCache, readExpired, 2, 1, null, new java.util.ArrayList<const Info*>());
  }
  public GoogleMapsLayer(String key, TimeInterval timeToCache)
  {
	  this(key, timeToCache, true, 2, 1, null, new java.util.ArrayList<const Info*>());
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: GoogleMapsLayer(const String& key, const TimeInterval& timeToCache, const boolean readExpired = true, const int initialLevel = 2, const float transparency = 1, const LayerCondition* condition = null, java.util.ArrayList<const Info*>* layerInfo = new java.util.ArrayList<const Info*>()) : RasterLayer(timeToCache, readExpired, new LayerTilesRenderParameters(Sector::fullSphere(), 1, 1, initialLevel, 20, Vector2I(256, 256), LayerTilesRenderParameters::defaultTileMeshResolution(), true), transparency, condition, layerInfo), _key(key), _initialLevel(initialLevel)
  public GoogleMapsLayer(String key, TimeInterval timeToCache, boolean readExpired, int initialLevel, float transparency, LayerCondition condition, java.util.ArrayList<Info> layerInfo)
  {
	  super(timeToCache, readExpired, new LayerTilesRenderParameters(Sector.fullSphere(), 1, 1, initialLevel, 20, new Vector2I(256, 256), LayerTilesRenderParameters.defaultTileMeshResolution(), true), transparency, condition, layerInfo);
	  _key = key;
	  _initialLevel = initialLevel;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: URL getFeatureInfoURL(const Geodetic2D& position, const Sector& sector) const
  public final URL getFeatureInfoURL(Geodetic2D position, Sector sector)
  {
	return new URL();
  }


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String description() const
  public final String description()
  {
	return "[GoogleMapsLayer]";
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: GoogleMapsLayer* copy() const
  public final GoogleMapsLayer copy()
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return new GoogleMapsLayer(_key, _timeToCache, _readExpired, _initialLevel, _transparency, (_condition == null) ? null : _condition->copy(), _layerInfo);
	return new GoogleMapsLayer(_key, new TimeInterval(_timeToCache), _readExpired, _initialLevel, _transparency, (_condition == null) ? null : _condition.copy(), _layerInfo);
  }

  public final RenderState getRenderState()
  {
	_errors.clear();
	if (_key.compareTo("") == 0)
	{
	  _errors.add("Missing layer parameter: key");
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
	return Sector.fullSphere();
  }

}
