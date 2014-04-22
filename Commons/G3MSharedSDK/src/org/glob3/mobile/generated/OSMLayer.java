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

  public OSMLayer(TimeInterval timeToCache, boolean readExpired, int initialLevel)
  {
     this(timeToCache, readExpired, initialLevel, null);
  }
  public OSMLayer(TimeInterval timeToCache, boolean readExpired)
  {
     this(timeToCache, readExpired, 2, null);
  }
  public OSMLayer(TimeInterval timeToCache)
  {
     this(timeToCache, true, 2, null);
  }
  public OSMLayer(TimeInterval timeToCache, boolean readExpired, int initialLevel, LayerCondition condition)
  {
     super("http://", "tile.openstreetmap.org", getSubdomains(), "png", timeToCache, readExpired, Sector.fullSphere(), initialLevel, 18, condition);

  }

  public final String description()
  {
    return "[OSMLayer]";
  }

  public final OSMLayer copy()
  {
    return new OSMLayer(TimeInterval.fromMilliseconds(_timeToCacheMS), _readExpired, _initialLevel, (_condition == null) ? null : _condition.copy());
  }

  public final RenderState getRenderState()
  {
    return RenderState.ready();
  }
}