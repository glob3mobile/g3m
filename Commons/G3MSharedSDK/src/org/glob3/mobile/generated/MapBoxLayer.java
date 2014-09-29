package org.glob3.mobile.generated; 
//
//  MapBoxLayer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/8/13.
//
//

//
//  MapBoxLayer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/8/13.
//
//



public class MapBoxLayer extends MercatorTiledLayer
{
  private final String _mapKey;

  private static java.util.ArrayList<String> getSubdomains()
  {
    java.util.ArrayList<String> result = new java.util.ArrayList<String>();
    result.add("a.");
    result.add("b.");
    result.add("c.");
    result.add("d.");
    return result;
  }

  protected final String getLayerType()
  {
    return "MapBox";
  }

  protected final boolean rawIsEquals(Layer that)
  {
    MapBoxLayer t = (MapBoxLayer) that;
    return (_domain.equals(t._domain));
  }

  // https://tiles.mapbox.com/v3/dgd.map-v93trj8v/3/3/3.png
  // https://tiles.mapbox.com/v3/dgd.map-v93trj8v/7/62/48.png?updated=f0e992c

  // TODO: parse json of layer metadata
  // http://a.tiles.mapbox.com/v3/examples.map-qfyrx5r8.json

  public MapBoxLayer(String mapKey, TimeInterval timeToCache, boolean readExpired, int initialLevel, int maxLevel, float transparency, LayerCondition condition)
  {
     this(mapKey, timeToCache, readExpired, initialLevel, maxLevel, transparency, condition, new java.util.ArrayList<Info>());
  }
  public MapBoxLayer(String mapKey, TimeInterval timeToCache, boolean readExpired, int initialLevel, int maxLevel, float transparency)
  {
     this(mapKey, timeToCache, readExpired, initialLevel, maxLevel, transparency, null, new java.util.ArrayList<Info>());
  }
  public MapBoxLayer(String mapKey, TimeInterval timeToCache, boolean readExpired, int initialLevel, int maxLevel)
  {
     this(mapKey, timeToCache, readExpired, initialLevel, maxLevel, 1, null, new java.util.ArrayList<Info>());
  }
  public MapBoxLayer(String mapKey, TimeInterval timeToCache, boolean readExpired, int initialLevel)
  {
     this(mapKey, timeToCache, readExpired, initialLevel, 19, 1, null, new java.util.ArrayList<Info>());
  }
  public MapBoxLayer(String mapKey, TimeInterval timeToCache, boolean readExpired)
  {
     this(mapKey, timeToCache, readExpired, 1, 19, 1, null, new java.util.ArrayList<Info>());
  }
  public MapBoxLayer(String mapKey, TimeInterval timeToCache)
  {
     this(mapKey, timeToCache, true, 1, 19, 1, null, new java.util.ArrayList<Info>());
  }
  public MapBoxLayer(String mapKey, TimeInterval timeToCache, boolean readExpired, int initialLevel, int maxLevel, float transparency, LayerCondition condition, java.util.ArrayList<Info> layerInfo) // isTransparent
  {
     super("http://", "tiles.mapbox.com/v3/" + mapKey, getSubdomains(), "png", timeToCache, readExpired, initialLevel, maxLevel, false, transparency, condition, layerInfo);
     _mapKey = mapKey;
  }

  public final String description()
  {
    return "[MapBoxLayer]";
  }

  public final MapBoxLayer copy()
  {
    return new MapBoxLayer(_mapKey, _timeToCache, _readExpired, _initialLevel, _maxLevel, _transparency, (_condition == null) ? null : _condition.copy(), _layerInfo);
  }

  public final RenderState getRenderState()
  {
    _errors.clear();
    if (_mapKey.compareTo("") == 0)
    {
      _errors.add("Missing layer parameter: mapKey");
    }
  
    if (_errors.size() > 0)
    {
      return RenderState.error(_errors);
    }
    return RenderState.ready();
  }

}