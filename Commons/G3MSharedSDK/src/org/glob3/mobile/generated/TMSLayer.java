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
     this(mapLayer, mapServerURL, dataSector, format, isTransparent, condition, timeToCache, readExpired, parameters, transparency, new java.util.ArrayList<Info>());
  }
  public TMSLayer(String mapLayer, URL mapServerURL, Sector dataSector, String format, boolean isTransparent, LayerCondition condition, TimeInterval timeToCache, boolean readExpired, LayerTilesRenderParameters parameters)
  {
     this(mapLayer, mapServerURL, dataSector, format, isTransparent, condition, timeToCache, readExpired, parameters, 1, new java.util.ArrayList<Info>());
  }
  public TMSLayer(String mapLayer, URL mapServerURL, Sector dataSector, String format, boolean isTransparent, LayerCondition condition, TimeInterval timeToCache, boolean readExpired)
  {
     this(mapLayer, mapServerURL, dataSector, format, isTransparent, condition, timeToCache, readExpired, null, 1, new java.util.ArrayList<Info>());
  }
  public TMSLayer(String mapLayer, URL mapServerURL, Sector dataSector, String format, boolean isTransparent, LayerCondition condition, TimeInterval timeToCache, boolean readExpired, LayerTilesRenderParameters parameters, float transparency, java.util.ArrayList<Info> layerInfo)
  {
     super(timeToCache, readExpired, (parameters == null) ? LayerTilesRenderParameters.createDefaultWGS84(dataSector, 0, 17) : parameters, transparency, condition, layerInfo);
     _mapServerURL = mapServerURL;
     _mapLayer = mapLayer;
     _dataSector = new Sector(dataSector);
     _format = format;
     _isTransparent = isTransparent;
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