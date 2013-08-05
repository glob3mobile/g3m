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

  private static java.util.ArrayList<String> getSubdomains()
  {
    java.util.ArrayList<String> result = new java.util.ArrayList<String>();
    result.add("a.");
    result.add("b.");
    result.add("c.");
    result.add("d.");
    return result;
  }

  // https://tiles.mapbox.com/v3/dgd.map-v93trj8v/3/3/3.png
  // https://tiles.mapbox.com/v3/dgd.map-v93trj8v/7/62/48.png?updated=f0e992c

  public MapBoxLayer(String mapKey, TimeInterval timeToCache, boolean readExpired)
  {
     this(mapKey, timeToCache, readExpired, null);
  }
  public MapBoxLayer(String mapKey, TimeInterval timeToCache)
  {
     this(mapKey, timeToCache, true, null);
  }
  public MapBoxLayer(String mapKey, TimeInterval timeToCache, boolean readExpired, LayerCondition condition) //initialMapBoxLevel,
              //int initialMapBoxLevel = 1,
  {
     super("MapBoxLayer", "http://", "tiles.mapbox.com/v3/" + mapKey, getSubdomains(), "png", timeToCache, readExpired, Sector.fullSphere(), 1, 17, condition);

  }

}