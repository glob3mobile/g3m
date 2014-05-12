package org.glob3.mobile.generated; 
//
//  HereLayer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/7/13.
//
//

//
//  HereLayer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/7/13.
//
//





public class HereLayer extends Layer
{
  private final String _appId;
  private final String _appCode;
  private final int _initialLevel;

  protected final String getLayerType()
  {
    return "Here";
  }

  protected final boolean rawIsEquals(Layer that)
  {
    HereLayer t = (HereLayer) that;
  
    if (!_appId.equals(t._appId))
    {
      return false;
    }
  
    if (!_appCode.equals(t._appCode))
    {
      return false;
    }
  
    if (_initialLevel != t._initialLevel)
    {
      return false;
    }
  
    return true;
  }


  public HereLayer(String appId, String appCode, TimeInterval timeToCache, boolean readExpired, int initialLevel, LayerCondition condition, float transparency)
  {
     this(appId, appCode, timeToCache, readExpired, initialLevel, condition, transparency, "");
  }
  public HereLayer(String appId, String appCode, TimeInterval timeToCache, boolean readExpired, int initialLevel, LayerCondition condition)
  {
     this(appId, appCode, timeToCache, readExpired, initialLevel, condition, (float)1.0, "");
  }
  public HereLayer(String appId, String appCode, TimeInterval timeToCache, boolean readExpired, int initialLevel)
  {
     this(appId, appCode, timeToCache, readExpired, initialLevel, null, (float)1.0, "");
  }
  public HereLayer(String appId, String appCode, TimeInterval timeToCache, boolean readExpired)
  {
     this(appId, appCode, timeToCache, readExpired, 2, null, (float)1.0, "");
  }
  public HereLayer(String appId, String appCode, TimeInterval timeToCache)
  {
     this(appId, appCode, timeToCache, true, 2, null, (float)1.0, "");
  }
  public HereLayer(String appId, String appCode, TimeInterval timeToCache, boolean readExpired, int initialLevel, LayerCondition condition, float transparency, String disclaimerInfo)
  {
     super(condition, "here", timeToCache, readExpired, new LayerTilesRenderParameters(Sector.fullSphere(), 1, 1, initialLevel, 20, new Vector2I(256, 256), LayerTilesRenderParameters.defaultTileMeshResolution(), true), Sector.fullSphere(), transparency, disclaimerInfo);
     _appId = appId;
     _appCode = appCode;
     _initialLevel = initialLevel;
  
  }

  public final java.util.ArrayList<Petition> createTileMapPetitions(G3MRenderContext rc, LayerTilesRenderParameters layerTilesRenderParameters, Tile tile)
  {
    java.util.ArrayList<Petition> petitions = new java.util.ArrayList<Petition>();
  
    final Sector tileSector = tile._sector;
  
    IStringBuilder isb = IStringBuilder.newStringBuilder();
  
    isb.addString("http://m.nok.it/");
  
    isb.addString("?app_id=");
    isb.addString(_appId);
  
    isb.addString("&app_code=");
    isb.addString(_appCode);
  
    isb.addString("&nord");
    isb.addString("&nodot");
  
    isb.addString("&w=");
    isb.addInt(_parameters._tileTextureResolution._x);
  
    isb.addString("&h=");
    isb.addInt(_parameters._tileTextureResolution._y);
  
    isb.addString("&ctr=");
    isb.addDouble(tileSector._center._latitude._degrees);
    isb.addString(",");
    isb.addDouble(tileSector._center._longitude._degrees);
  
  //  isb->addString("&poi=");
  //  isb->addDouble(tileSector._lower._latitude._degrees);
  //  isb->addString(",");
  //  isb->addDouble(tileSector._lower._longitude._degrees);
  //  isb->addString(",");
  //  isb->addDouble(tileSector._upper._latitude._degrees);
  //  isb->addString(",");
  //  isb->addDouble(tileSector._upper._longitude._degrees);
  //  isb->addString("&nomrk");
  
    isb.addString("&z=");
    final int level = tile._level;
    isb.addInt(level);
  
  //  isb->addString("&t=3");
  
    /*
     0 (normal.day)
     Normal map view in day light mode.
  
     1 (satellite.day)
     Satellite map view in day light mode.
  
     2 (terrain.day)
     Terrain map view in day light mode.
  
     3 (hybrid.day)
     Satellite map view with streets in day light mode.
  
     4 (normal.day.transit)
     Normal grey map view with public transit in day light mode.
  
     5 (normal.day.grey)
     Normal grey map view in day light mode (used for background maps).
  
     6 (normal.day.mobile)
     Normal map view for small screen devices in day light mode.
  
     7 (normal.night.mobile)
     Normal map view for small screen devices in night mode.
  
     8 (terrain.day.mobile)
     Terrain map view for small screen devices in day light mode.
  
     9 (hybrid.day.mobile)
     Satellite map view with streets for small screen devices in day light mode.
  
     10 (normal.day.transit.mobile)
     Normal grey map view with public transit for small screen devices in day light mode.
  
     11 (normal.day.grey.mobile)
     12 (carnav.day.grey) Map view designed for navigation devices.
     13 (pedestrian.day) Map view designed for pedestrians walking by day.
     14 (pedestrian.night) Map view designed for pedestrians walking by night.
     Normal grey map view for small screen devices in day light mode (used for background maps).
  
     By default normal map view in day light mode (0) is used for non-mobile clients. For mobile clients the default is normal map view for small screen devices in day light mode (6).
    
  
     */
  
    final String path = isb.getString();
  
    if (isb != null)
       isb.dispose();
  
    petitions.add(new Petition(tileSector, new URL(path, false), getTimeToCache(), getReadExpired(), true, _transparency));
  
    return petitions;
  }

  public final URL getFeatureInfoURL(Geodetic2D position, Sector sector)
  {
    return new URL();
  }

  public final String description()
  {
    return "[HereLayer]";
  }

  public final HereLayer copy()
  {
    return new HereLayer(_appId, _appCode, TimeInterval.fromMilliseconds(_timeToCacheMS), _readExpired, _initialLevel, (_condition == null) ? null : _condition.copy(), _transparency, _disclaimerInfo);
  }

  public final RenderState getRenderState()
  {
    _errors.clear();
    if (_appId.compareTo("") == 0)
    {
      _errors.add("Missing layer parameter: appId");
    }
    if (_appCode.compareTo("") == 0)
    {
      _errors.add("Missing layer parameter: appCode");
    }
  
    if (_errors.size() > 0)
    {
      return RenderState.error(_errors);
    }
    return RenderState.ready();
  }
}