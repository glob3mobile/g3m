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



public abstract class GoogleMapsLayer extends Layer
{
  private final String _key;
  private final Sector _sector ;



  public GoogleMapsLayer(String key, TimeInterval timeToCache, boolean readExpired, int initialLevel)
  {
     this(key, timeToCache, readExpired, initialLevel, null);
  }
  public GoogleMapsLayer(String key, TimeInterval timeToCache, boolean readExpired)
  {
     this(key, timeToCache, readExpired, 2, null);
  }
  public GoogleMapsLayer(String key, TimeInterval timeToCache)
  {
     this(key, timeToCache, true, 2, null);
  }
  public GoogleMapsLayer(String key, TimeInterval timeToCache, boolean readExpired, int initialLevel, LayerCondition condition)
  {
     super(condition, "GoogleMaps", timeToCache, readExpired, new LayerTilesRenderParameters(Sector.fullSphere(), 1, 1, initialLevel, 20, new Vector2I(256, 256), LayerTilesRenderParameters.defaultTileMeshResolution(), true));
     _key = key;
     _sector = new Sector(Sector.fullSphere());
  
  }

  public final URL getFeatureInfoURL(Geodetic2D position, Sector sector)
  {
    return new URL();
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
    if (sector._deltaLatitude.isZero() || sector._deltaLongitude.isZero())
    {
      return petitions;
    }
  
  
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
  
    final int level = tile.getLevel();
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
  
    petitions.add(new Petition(tileSector, new URL(path, false), getTimeToCache(), getReadExpired(), true));
  
    return petitions;
  }

  public final String description()
  {
    return "[GoogleMapsLayer]";
  }

}