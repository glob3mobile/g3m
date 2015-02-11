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




public class MercatorTiledLayer extends RasterLayer
{
  protected final String _protocol;
  protected final String _domain;
  protected final java.util.ArrayList<String> _subdomains;
  protected final String _imageFormat;

  protected final int _initialLevel;
  protected final int _maxLevel;
  protected final boolean _isTransparent;

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
  
    return new URL(path, false);
  }


  /*
   Implementation details: http: //wiki.openstreetmap.org/wiki/Slippy_map_tilenames
   */
  
  public MercatorTiledLayer(String protocol, String domain, java.util.ArrayList<String> subdomains, String imageFormat, TimeInterval timeToCache, boolean readExpired, int initialLevel, int maxLevel, boolean isTransparent, float transparency, LayerCondition condition)
  {
     this(protocol, domain, subdomains, imageFormat, timeToCache, readExpired, initialLevel, maxLevel, isTransparent, transparency, condition, new java.util.ArrayList<Info>());
  }
  public MercatorTiledLayer(String protocol, String domain, java.util.ArrayList<String> subdomains, String imageFormat, TimeInterval timeToCache, boolean readExpired, int initialLevel, int maxLevel, boolean isTransparent, float transparency)
  {
     this(protocol, domain, subdomains, imageFormat, timeToCache, readExpired, initialLevel, maxLevel, isTransparent, transparency, null, new java.util.ArrayList<Info>());
  }
  public MercatorTiledLayer(String protocol, String domain, java.util.ArrayList<String> subdomains, String imageFormat, TimeInterval timeToCache, boolean readExpired, int initialLevel, int maxLevel, boolean isTransparent)
  {
     this(protocol, domain, subdomains, imageFormat, timeToCache, readExpired, initialLevel, maxLevel, isTransparent, 1, null, new java.util.ArrayList<Info>());
  }
  public MercatorTiledLayer(String protocol, String domain, java.util.ArrayList<String> subdomains, String imageFormat, TimeInterval timeToCache, boolean readExpired, int initialLevel, int maxLevel)
  {
     this(protocol, domain, subdomains, imageFormat, timeToCache, readExpired, initialLevel, maxLevel, false, 1, null, new java.util.ArrayList<Info>());
  }
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

  public final URL getFeatureInfoURL(Geodetic2D position, Sector sector)
  {
    return new URL();
  }

  public String description()
  {
    return "[MercatorTiledLayer]";
  }

  public MercatorTiledLayer copy()
  {
    return new MercatorTiledLayer(_protocol, _domain, _subdomains, _imageFormat, _timeToCache, _readExpired, _initialLevel, _maxLevel, _isTransparent, _transparency, (_condition == null) ? null : _condition.copy(), _layerInfo);
  }

  public RenderState getRenderState()
  {
    return RenderState.ready();
  }

  public final Sector getDataSector()
  {
    return Sector.fullSphere();
  }

}