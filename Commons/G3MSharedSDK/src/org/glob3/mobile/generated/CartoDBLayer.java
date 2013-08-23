package org.glob3.mobile.generated; 
//
//  CartoDBLayer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 5/27/13.
//
//

//
//  CartoDBLayer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 5/27/13.
//
//



public class CartoDBLayer extends MercatorTiledLayer
{

  private static java.util.ArrayList<String> getSubdomains()
  {
    java.util.ArrayList<String> result = new java.util.ArrayList<String>();
    result.add("0.");
    result.add("1.");
    result.add("2.");
    result.add("3.");
    return result;
  }


  // http://0.tiles.cartocdn.com/mdelacalle/tiles/tm_world_borders_simpl_0_3/2/0/1.png

  public CartoDBLayer(String userName, String table, TimeInterval timeToCache, boolean readExpired)
  {
     this(userName, table, timeToCache, readExpired, null);
  }
  public CartoDBLayer(String userName, String table, TimeInterval timeToCache)
  {
     this(userName, table, timeToCache, true, null);
  }
  public CartoDBLayer(String userName, String table, TimeInterval timeToCache, boolean readExpired, LayerCondition condition)
               //int initialCartoDBLevel = 1,
  {
     super("CartoDBLayer", "http://", "tiles.cartocdn.com/" + userName + "/tiles/" + table, getSubdomains(), "png", timeToCache, readExpired, Sector.fullSphere(), 2, 17, condition);

  }

  public final String description()
  {
    return "[CartoDBLayer]";
  }

}