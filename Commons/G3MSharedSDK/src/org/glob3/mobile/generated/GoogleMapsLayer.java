package org.glob3.mobile.generated; 
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

  protected final String getLayerType()
  {
    return "GoogleMaps";
  }

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
    return ((_transparency < 1) ? TileImageContribution.partialCoverageTransparent(requestedImageSector, _transparency) : TileImageContribution.partialCoverageOpaque(requestedImageSector));
  }

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
     this(key, timeToCache, readExpired, initialLevel, transparency, condition, new java.util.ArrayList<Info>());
  }
  public GoogleMapsLayer(String key, TimeInterval timeToCache, boolean readExpired, int initialLevel, float transparency)
  {
     this(key, timeToCache, readExpired, initialLevel, transparency, null, new java.util.ArrayList<Info>());
  }
  public GoogleMapsLayer(String key, TimeInterval timeToCache, boolean readExpired, int initialLevel)
  {
     this(key, timeToCache, readExpired, initialLevel, 1, null, new java.util.ArrayList<Info>());
  }
  public GoogleMapsLayer(String key, TimeInterval timeToCache, boolean readExpired)
  {
     this(key, timeToCache, readExpired, 2, 1, null, new java.util.ArrayList<Info>());
  }
  public GoogleMapsLayer(String key, TimeInterval timeToCache)
  {
     this(key, timeToCache, true, 2, 1, null, new java.util.ArrayList<Info>());
  }
  public GoogleMapsLayer(String key, TimeInterval timeToCache, boolean readExpired, int initialLevel, float transparency, LayerCondition condition, java.util.ArrayList<Info> layerInfo)
  {
     super(timeToCache, readExpired, new LayerTilesRenderParameters(Sector.fullSphere(), 1, 1, initialLevel, 20, new Vector2I(256, 256), LayerTilesRenderParameters.defaultTileMeshResolution(), true), transparency, condition, layerInfo);
     _key = key;
     _initialLevel = initialLevel;
  }

  public final URL getFeatureInfoURL(Geodetic2D position, Sector sector)
  {
    return new URL();
  }


  public final String description()
  {
    return "[GoogleMapsLayer]";
  }

  public final GoogleMapsLayer copy()
  {
    return new GoogleMapsLayer(_key, _timeToCache, _readExpired, _initialLevel, _transparency, (_condition == null) ? null : _condition.copy(), _layerInfo);
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

  public final Sector getDataSector()
  {
    return Sector.fullSphere();
  }

}