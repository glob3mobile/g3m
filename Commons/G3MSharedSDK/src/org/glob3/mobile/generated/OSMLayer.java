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

  public OSMLayer(TimeInterval timeToCache, int initialLevel)
  {
     this(timeToCache, initialLevel, null);
  }
  public OSMLayer(TimeInterval timeToCache)
  {
     this(timeToCache, 2, null);
  }
  public OSMLayer(TimeInterval timeToCache, int initialLevel, LayerCondition condition)
  {
     super("OpenStreetMap", "http://", "tile.openstreetmap.org", getSubdomains(), "png", timeToCache, Sector.fullSphere(), initialLevel, 18, condition);

  }

}