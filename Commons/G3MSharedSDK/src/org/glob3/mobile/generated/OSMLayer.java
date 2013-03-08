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

  public OSMLayer(TimeInterval timeToCache)
  {
     this(timeToCache, null);
  }
  public OSMLayer(TimeInterval timeToCache, LayerCondition condition) //initialOSMLevel,
           //int initialOSMLevel = 1,
  {
     super("OpenStreetMap", "tile.openstreetmap.org", getSubdomains(), timeToCache, Sector.fullSphere(), 1, 18, condition);

  }

}