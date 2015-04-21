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




public class TMSLayer extends RasterLayer
{

  private final URL _mapServerURL;

  private final String _mapLayer;
  private final Sector _dataSector ;
  private final String _format;
  private final boolean _isTransparent;

  protected final String getLayerType()
  {
    return "TMSLayer";
  }

  protected final TileImageContribution rawContribution(Tile tile)
  {
    final Tile tileP = getParentTileOfSuitableLevel(tile);
    if (tileP == null)
    {
      return null;
    }
  
    final Sector requestedImageSector = tileP._sector;
  
    if (!_dataSector.touchesWith(requestedImageSector))
    {
      return null;
    }
  
    if (tile == tileP && (_dataSector.fullContains(requestedImageSector)))
    {
      return ((_isTransparent || (_transparency < 1)) ? TileImageContribution.fullCoverageTransparent(_transparency) : TileImageContribution.fullCoverageOpaque());
    }
  
    return ((_isTransparent || (_transparency < 1)) ? TileImageContribution.partialCoverageTransparent(requestedImageSector, _transparency) : TileImageContribution.partialCoverageOpaque(requestedImageSector));
  }

  protected final URL createURL(Tile tile)
  {
  
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
  
  
    URL url = new URL(isb.getString(), false);
    if (isb != null)
       isb.dispose();
  
    return url;
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
  
    if (!(_dataSector.isEquals(t._dataSector)))
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


  public final TMSLayer copy()
  {
    return new TMSLayer(_mapLayer, _mapServerURL, _dataSector, _format, _isTransparent, (_condition == null) ? null : _condition.copy(), _timeToCache, _readExpired, (_parameters == null) ? null : _parameters.copy());
  }

  public final Sector getDataSector()
  {
    return _dataSector;
  }

}