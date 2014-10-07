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



public class OSMLayer extends MercatorTiledLayer
{

  private static java.util.ArrayList<String> getSubdomains()
  {
    java.util.ArrayList<String> result = new java.util.ArrayList<String>();
    result.add("a.");
    result.add("b.");
    result.add("c.");
    return result;
  }


  protected final String getLayerType()
  {
    return "OSM";
  }

  protected final boolean rawIsEquals(Layer that)
  {
    return true;
  }

  public OSMLayer(TimeInterval timeToCache, boolean readExpired, int initialLevel, float transparency, LayerCondition condition)
  {
     this(timeToCache, readExpired, initialLevel, transparency, condition, new java.util.ArrayList<Info>());
  }
  public OSMLayer(TimeInterval timeToCache, boolean readExpired, int initialLevel, float transparency)
  {
     this(timeToCache, readExpired, initialLevel, transparency, null, new java.util.ArrayList<Info>());
  }
  public OSMLayer(TimeInterval timeToCache, boolean readExpired, int initialLevel)
  {
     this(timeToCache, readExpired, initialLevel, 1, null, new java.util.ArrayList<Info>());
  }
  public OSMLayer(TimeInterval timeToCache, boolean readExpired)
  {
     this(timeToCache, readExpired, 2, 1, null, new java.util.ArrayList<Info>());
  }
  public OSMLayer(TimeInterval timeToCache)
  {
     this(timeToCache, true, 2, 1, null, new java.util.ArrayList<Info>());
  }
  public OSMLayer(TimeInterval timeToCache, boolean readExpired, int initialLevel, float transparency, LayerCondition condition, java.util.ArrayList<Info> layerInfo) // isTransparent
  {
     super("http://", "tile.openstreetmap.org", getSubdomains(), "png", timeToCache, readExpired, initialLevel, 18, false, transparency, condition, layerInfo);
  }

  public final String description()
  {
    return "[OSMLayer]";
  }

  public final OSMLayer copy()
  {
    return new OSMLayer(_timeToCache, _readExpired, _initialLevel, _transparency, (_condition == null) ? null : _condition.copy(), _layerInfo);
  }

  public final RenderState getRenderState()
  {
    return RenderState.ready();
  }

}