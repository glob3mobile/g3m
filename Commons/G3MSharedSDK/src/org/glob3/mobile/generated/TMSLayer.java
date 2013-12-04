package org.glob3.mobile.generated; 
//
//  TMSLayer.cpp
//  G3MiOSSDK
//
//  Created by Eduardo de la Montaña on 05/03/13.
//
//

//
//  TMSLayer.hpp
//  G3MiOSSDK
//
//  Created by Eduardo de la Montaña on 05/03/13.
//
//




public class TMSLayer extends Layer
{

  private final URL _mapServerURL;

  private final String _mapLayer;
  private Sector _sector ;
  private final String _format;
  private final boolean _isTransparent;


  protected final String getLayerType()
  {
    return "TMS";
  }

  protected final boolean rawIsEquals(Layer that)
  {
    TMSLayer t = (TMSLayer) that;
  
    if (!(_mapServerURL.isEquals(t._mapServerURL)))
    {
      return false;
    }
  
    if (!_mapLayer.equals(t._mapLayer))
    {
      return false;
    }
  
    if (!(_sector.isEquals(t._sector)))
    {
      return false;
    }
  
    if (!_format.equals(t._format))
    {
      return false;
    }
  
    if (_isTransparent != t._isTransparent)
    {
      return false;
    }
  
    return true;
  }


  public TMSLayer(String mapLayer, URL mapServerURL, Sector sector, String format, boolean isTransparent, LayerCondition condition, TimeInterval timeToCache, boolean readExpired)
  {
     this(mapLayer, mapServerURL, sector, format, isTransparent, condition, timeToCache, readExpired, null);
  }
  public TMSLayer(String mapLayer, URL mapServerURL, Sector sector, String format, boolean isTransparent, LayerCondition condition, TimeInterval timeToCache, boolean readExpired, LayerTilesRenderParameters parameters)
  {
     super(condition, mapLayer, timeToCache, readExpired, (parameters == null) ? LayerTilesRenderParameters.createDefaultWGS84(sector) : parameters);
     _mapServerURL = mapServerURL;
     _mapLayer = mapLayer;
     _sector = new Sector(sector);
     _format = format;
     _isTransparent = isTransparent;
  }

  public final java.util.ArrayList<Petition> createTileMapPetitions(G3MRenderContext rc, Tile tile)
  {
  
    java.util.ArrayList<Petition> petitions = new java.util.ArrayList<Petition>();
  
    final Sector tileSector = tile._sector;
    if (!_sector.touchesWith(tileSector))
    {
      return petitions;
    }
  
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString(_mapServerURL.getPath());
    isb.addString(_mapLayer);
    isb.addString("/");
    isb.addInt(tile._level);
    isb.addString("/");
    isb.addInt(tile._column);
    isb.addString("/");
    isb.addInt(tile._row);
    isb.addString(".");
    isb.addString(IStringUtils.instance().replaceSubstring(_format, "image/", ""));
  
    ILogger.instance().logInfo(isb.getString());
  
    Petition petition = new Petition(tileSector, new URL(isb.getString(), false), getTimeToCache(), getReadExpired(), _isTransparent);
    petitions.add(petition);
  
     return petitions;
  
  }

  public final URL getFeatureInfoURL(Geodetic2D g, Sector sector)
  {
    return URL.nullURL();
  
  }

  public final String description()
  {
    return "[TMSLayer]";
  }

  public final TMSLayer copy()
  {
    return new TMSLayer(_mapLayer, _mapServerURL, _sector, _format, _isTransparent, (_condition == null) ? null : _condition.copy(), TimeInterval.fromMilliseconds(_timeToCacheMS), _readExpired, (_parameters == null) ? null : _parameters.copy());
  }

}