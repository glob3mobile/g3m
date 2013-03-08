package org.glob3.mobile.generated; 
//
//  MapQuestLayer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/8/13.
//
//

//
//  MapQuestLayer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/8/13.
//
//



/*
 http: //developer.mapquest.com/web/products/open/map
 */

public class MapQuestLayer extends MercatorTiledLayer
{

  private static java.util.ArrayList<String> getSubdomains()
  {
    java.util.ArrayList<String> result = new java.util.ArrayList<String>();
    result.add("otile1.");
    result.add("otile2.");
    result.add("otile3.");
    result.add("otile4.");
    return result;
  }

  private MapQuestLayer(String name, String domain, java.util.ArrayList<String> subdomains, int maxMercatorLevel, TimeInterval timeToCache, LayerCondition condition) //initialMapQuestLevel,
                //int initialMapQuestLevel,
  {
     super(name, "http://", domain, subdomains, "jpg", timeToCache, Sector.fullSphere(), 1, maxMercatorLevel, condition);

  }



  public static MapQuestLayer newOSM(TimeInterval timeToCache)
  {
     return newOSM(timeToCache, null);
  }
  public static MapQuestLayer newOSM(TimeInterval timeToCache, LayerCondition condition)
                               //int initialMapQuestLevel = 1,
  {
    return new MapQuestLayer("MapQuest-OSM", "mqcdn.com/tiles/1.0.0/map", getSubdomains(), 19, timeToCache, condition);
                             //initialMapQuestLevel,
  }


  public static MapQuestLayer newOpenAerial(TimeInterval timeToCache)
  {
     return newOpenAerial(timeToCache, null);
  }
  public static MapQuestLayer newOpenAerial(TimeInterval timeToCache, LayerCondition condition)
                                      //int initialMapQuestLevel = 1,
  {
    return new MapQuestLayer("MapQuest-OpenAerial", "mqcdn.com/tiles/1.0.0/sat", getSubdomains(), 11, timeToCache, condition);
                             //initialMapQuestLevel,
  }

}