package org.glob3.mobile.generated; 
//
//  OSMLayer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/6/13.
//
//

//
//  OSMLayer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/6/13.
//
//



public class OSMLayer extends Layer
{
  private final Sector _sector ;
  private final int _initialOSMLevel;



  /*
   Implementation details:
  
   - - from http: //wiki.openstreetmap.org/wiki/Slippy_map_tilenames
   - - the G3M level 0 is mapped to OSM level initialOSMLevel
   - - so maxLevel is 18-initialOSMLevel
   - - and splitsByLatitude and splitsByLongitude are set to 2^initialOSMLevel
  
   */
  
  
  public OSMLayer(TimeInterval timeToCache, int initialOSMLevel)
  {
     this(timeToCache, initialOSMLevel, null);
  }
  public OSMLayer(TimeInterval timeToCache)
  {
     this(timeToCache, 1, null);
  }
  public OSMLayer(TimeInterval timeToCache, int initialOSMLevel, LayerCondition condition)
  {
     _initialOSMLevel = initialOSMLevel;
     super(condition, "OpenStreetMap", timeToCache, new LayerTilesRenderParameters(Sector.fullSphere(), (int) IMathUtils.instance().pow(2.0, initialOSMLevel), (int) IMathUtils.instance().pow(2.0, initialOSMLevel), 18 - initialOSMLevel, new Vector2I(256, 256), LayerTilesRenderParameters.defaultTileMeshResolution(), true));
     _sector = new Sector(Sector.fullSphere());
  
  }


  public java.util.ArrayList<Petition> createTileMapPetitions(G3MRenderContext rc, Tile tile)
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
  
    // http://[abc].tile.openstreetmap.org/zoom/x/y.png
  
    final int tileLevel = tile.getLevel();
  
    IStringBuilder isb = IStringBuilder.newStringBuilder();
  
    isb.addString("http://"); // protocol
  
    // subdomains
    final int abcCounter = tileLevel % 3;
    switch (abcCounter)
    {
      case 0:
        isb.addString("a");
        break;
      case 1:
        isb.addString("b");
        break;
      case 2:
        isb.addString("c");
        break;
    }
  
    // domain
    isb.addString(".tile.openstreetmap.org/");
  
    final int osmLevel = tileLevel + _initialOSMLevel;
    // zoom
    isb.addInt(osmLevel);
    isb.addString("/");
  
    // x
    isb.addInt(tile.getColumn());
    isb.addString("/");
  
    // y
    final int numRows = (int) IMathUtils.instance().pow(2.0, osmLevel);
    isb.addInt(numRows - tile.getRow() - 1);
    isb.addString(".png");
  
    final String path = isb.getString();
  
    if (isb != null)
       isb.dispose();
  
    petitions.add(new Petition(tileSector, new URL(path, false), _timeToCache, true));
  
    return petitions;
  }

  public URL getFeatureInfoURL(Geodetic2D position, Sector sector)
  {
    return new URL();
  }

}