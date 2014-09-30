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




public abstract class TMSLayer extends RasterLayer
{

  private final URL _mapServerURL;

  private final String _mapLayer;
  private final Sector _dataSector ;
  private final String _format;
  private final boolean _isTransparent;


  public TMSLayer(String mapLayer, URL mapServerURL, Sector dataSector, String format, boolean isTransparent, LayerCondition condition, TimeInterval timeToCache, boolean readExpired, LayerTilesRenderParameters parameters, float transparency)
  {
     this(mapLayer, mapServerURL, dataSector, format, isTransparent, condition, timeToCache, readExpired, parameters, transparency, "");
  }
  public TMSLayer(String mapLayer, URL mapServerURL, Sector dataSector, String format, boolean isTransparent, LayerCondition condition, TimeInterval timeToCache, boolean readExpired, LayerTilesRenderParameters parameters)
  {
     this(mapLayer, mapServerURL, dataSector, format, isTransparent, condition, timeToCache, readExpired, parameters, 1, "");
  }
  public TMSLayer(String mapLayer, URL mapServerURL, Sector dataSector, String format, boolean isTransparent, LayerCondition condition, TimeInterval timeToCache, boolean readExpired)
  {
     this(mapLayer, mapServerURL, dataSector, format, isTransparent, condition, timeToCache, readExpired, null, 1, "");
  }
  public TMSLayer(String mapLayer, URL mapServerURL, Sector dataSector, String format, boolean isTransparent, LayerCondition condition, TimeInterval timeToCache, boolean readExpired, LayerTilesRenderParameters parameters, float transparency, String disclaimerInfo)
  {
     super(timeToCache, readExpired, (parameters == null) ? LayerTilesRenderParameters.createDefaultWGS84(dataSector, 0, 17) : parameters, transparency, condition, disclaimerInfo);
     _mapServerURL = mapServerURL;
     _mapLayer = mapLayer;
     _dataSector = new Sector(dataSector);
     _format = format;
     _isTransparent = isTransparent;
  }

  public final java.util.ArrayList<Petition> createTileMapPetitions(G3MRenderContext rc, LayerTilesRenderParameters layerTilesRenderParameters, Tile tile)
  {
  
    java.util.ArrayList<Petition> petitions = new java.util.ArrayList<Petition>();
  
    final Sector tileSector = tile._sector;
    if (!_dataSector.touchesWith(tileSector))
    {
      return petitions;
    }
  
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString(_mapServerURL._path);
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
  
    Petition petition = new Petition(tileSector, new URL(isb.getString(), false), _timeToCache, _readExpired, _isTransparent, _transparency);
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

  public final RenderState getRenderState()
  {
    _errors.clear();
    if (_mapLayer.compareTo("") == 0)
    {
      _errors.add("Missing layer parameter: mapLayer");
    }
    final String mapServerUrl = _mapServerURL._path;
    if (mapServerUrl.compareTo("") == 0)
    {
      _errors.add("Missing layer parameter: mapServerURL");
    }
    if (_format.compareTo("") == 0)
    {
      _errors.add("Missing layer parameter: format");
    }
  
    if (_errors.size() > 0)
    {
      return RenderState.error(_errors);
    }
    return RenderState.ready();
  }
}