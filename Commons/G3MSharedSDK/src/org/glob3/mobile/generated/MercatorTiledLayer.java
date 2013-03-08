package org.glob3.mobile.generated; 
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



public class MercatorTiledLayer extends Layer
{
  private final String _domain;
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final java.util.ArrayList<String> _subdomains = new java.util.ArrayList<String>();
//#endif
  private final java.util.ArrayList<String> _subdomains;
  private final String _imageFormat;

  private final Sector _sector ;
  private final int _initialMercatorLevel;



  /*
  
   Implementation details:
  
   - - from http: //wiki.openstreetmap.org/wiki/Slippy_map_tilenames
   - - the G3M level 0 is mapped to initialMercatorLevel
   - - so maxLevel is maxMercatorLevel-initialOSMLevel
   - - and splitsByLatitude and splitsByLongitude are set to 2^initialMercatorLevel
  
   */
  
  public MercatorTiledLayer(String name, String domain, java.util.ArrayList<String> subdomains, String imageFormat, TimeInterval timeToCache, Sector sector, int initialMercatorLevel, int maxMercatorLevel, LayerCondition condition)
  {
     super(condition, name, timeToCache, new LayerTilesRenderParameters(Sector.fullSphere(), (int) IMathUtils.instance().pow(2.0, initialMercatorLevel), (int) IMathUtils.instance().pow(2.0, initialMercatorLevel), maxMercatorLevel - initialMercatorLevel, new Vector2I(256, 256), LayerTilesRenderParameters.defaultTileMeshResolution(), true));
     _domain = domain;
     _subdomains = subdomains;
     _imageFormat = imageFormat;
     _sector = new Sector(sector);
     _initialMercatorLevel = initialMercatorLevel;
  
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
    if (sector.getDeltaLatitude().isZero() || sector.getDeltaLongitude().isZero())
    {
      return petitions;
    }
  
    // http://[abc].tile.openstreetmap.org/zoom/x/y.png
    // http://[abc].tiles.mapbox.com/v3/examples.map-vyofok3q/9/250/193.png
  
    final int level = tile.getLevel() + _initialMercatorLevel;
    final int column = tile.getColumn();
    final int numRows = (int) IMathUtils.instance().pow(2.0, level);
    final int row = numRows - tile.getRow() - 1;
  
    IStringBuilder isb = IStringBuilder.newStringBuilder();
  
    isb.addString("http://");
  
    final int subdomainsSize = _subdomains.size();
    if (subdomainsSize > 0)
    {
      // select subdomain based on fixed data (instead of round-robin) to be cache friendly
      final int subdomainsIndex = (level + column + row) % subdomainsSize;
      isb.addString(_subdomains.get(subdomainsIndex));
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
  
    petitions.add(new Petition(tileSector, new URL(path, false), _timeToCache, true));
  
    return petitions;
  }

}