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
  protected final String _protocol;
  protected final String _domain;
  protected final java.util.ArrayList<String> _subdomains;
  protected final String _imageFormat;

  protected final Sector _sector ;
  protected final int _initialLevel;
  protected final int _maxLevel;

  protected String getLayerType()
  {
    return "MercatorTiled";
  }

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
  
    if (!_sector.isEquals(t._sector))
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
      final String thisSubdomain = _subdomains.get(i);
      final String thatSubdomain = t._subdomains.get(i);
      if (thisSubdomain != thatSubdomain)
      {
        return false;
      }
    }
  
    return true;
  }


  /*
   Implementation details: http: //wiki.openstreetmap.org/wiki/Slippy_map_tilenames
   */
  
  public MercatorTiledLayer(String name, String protocol, String domain, java.util.ArrayList<String> subdomains, String imageFormat, TimeInterval timeToCache, boolean readExpired, Sector sector, int initialLevel, int maxLevel, LayerCondition condition)
  {
     super(condition, name, timeToCache, readExpired, new LayerTilesRenderParameters(Sector.fullSphere(), 1, 1, initialLevel, maxLevel, new Vector2I(256, 256), LayerTilesRenderParameters.defaultTileMeshResolution(), true));
     _protocol = protocol;
     _domain = domain;
     _subdomains = subdomains;
     _imageFormat = imageFormat;
     _sector = new Sector(sector);
     _initialLevel = initialLevel;
     _maxLevel = maxLevel;
  
  }

  public final URL getFeatureInfoURL(Geodetic2D position, Sector sector)
  {
    return new URL();
  }

  public final java.util.ArrayList<Petition> createTileMapPetitions(G3MRenderContext rc, Tile tile)
  {
    final IMathUtils mu = IMathUtils.instance();
  
    java.util.ArrayList<Petition> petitions = new java.util.ArrayList<Petition>();
  
    final Sector tileSector = tile._sector;
    if (!_sector.touchesWith(tileSector))
    {
      return petitions;
    }
  
    final Sector sector = tileSector.intersection(_sector);
    if (sector._deltaLatitude.isZero() || sector._deltaLongitude.isZero())
    {
      return petitions;
    }
  
    // http://[abc].tile.openstreetmap.org/zoom/x/y.png
    // http://[abc].tiles.mapbox.com/v3/examples.map-vyofok3q/9/250/193.png
  
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
  
    petitions.add(new Petition(tileSector, new URL(path, false), getTimeToCache(), getReadExpired(), true));
  
    return petitions;
  }

  public String description()
  {
    return "[MercatorTiledLayer]";
  }


  public MercatorTiledLayer copy()
  {
    return new MercatorTiledLayer(_name, _protocol, _domain, _subdomains, _imageFormat, TimeInterval.fromMilliseconds(_timeToCacheMS), _readExpired, _sector, _initialLevel, _maxLevel, (_condition == null) ? null : _condition.copy());
  }

}