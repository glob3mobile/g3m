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
  private final int _initialLevel;
  private final int _maxLevel;

  private static java.util.ArrayList<String> getSubdomains()
  {
    java.util.ArrayList<String> result = new java.util.ArrayList<String>();
    result.add("otile1.");
    result.add("otile2.");
    result.add("otile3.");
    result.add("otile4.");
    return result;
  }

  private MapQuestLayer(String name, String domain, java.util.ArrayList<String> subdomains, int initialLevel, int maxLevel, TimeInterval timeToCache, boolean readExpired, LayerCondition condition)
  {
     super(name, "http://", domain, subdomains, "jpg", timeToCache, readExpired, Sector.fullSphere(), initialLevel, maxLevel, condition);
     _initialLevel = initialLevel;
     _maxLevel = maxLevel;

  }


  protected final String getLayerType()
  {
    return "MapQuest";
  }

  protected final boolean rawIsEquals(Layer that)
  {
    MapQuestLayer t = (MapQuestLayer) that;
    return (_domain.equals(t._domain));
  }



  public static MapQuestLayer newOSM(TimeInterval timeToCache, boolean readExpired, int initialLevel)
  {
     return newOSM(timeToCache, readExpired, initialLevel, null);
  }
  public static MapQuestLayer newOSM(TimeInterval timeToCache, boolean readExpired)
  {
     return newOSM(timeToCache, readExpired, 2, null);
  }
  public static MapQuestLayer newOSM(TimeInterval timeToCache)
  {
     return newOSM(timeToCache, true, 2, null);
  }
  public static MapQuestLayer newOSM(TimeInterval timeToCache, boolean readExpired, int initialLevel, LayerCondition condition)
  {
    return new MapQuestLayer("MapQuest-OSM", "mqcdn.com/tiles/1.0.0/map", getSubdomains(), initialLevel, 19, timeToCache, readExpired, condition);
  }


  public static MapQuestLayer newOpenAerial(TimeInterval timeToCache, boolean readExpired, int initialLevel)
  {
     return newOpenAerial(timeToCache, readExpired, initialLevel, null);
  }
  public static MapQuestLayer newOpenAerial(TimeInterval timeToCache, boolean readExpired)
  {
     return newOpenAerial(timeToCache, readExpired, 2, null);
  }
  public static MapQuestLayer newOpenAerial(TimeInterval timeToCache)
  {
     return newOpenAerial(timeToCache, true, 2, null);
  }
  public static MapQuestLayer newOpenAerial(TimeInterval timeToCache, boolean readExpired, int initialLevel, LayerCondition condition)
  {
    return new MapQuestLayer("MapQuest-OpenAerial", "mqcdn.com/tiles/1.0.0/sat", getSubdomains(), initialLevel, 11, timeToCache, readExpired, condition);
  }

  public final String description()
  {
    return "[MapQuestLayer]";
  }

  public final MapQuestLayer copy()
  {
    return new MapQuestLayer(_name, _domain, _subdomains, _initialLevel, _maxLevel, TimeInterval.fromMilliseconds(_timeToCacheMS), _readExpired, (_condition == null) ? null : _condition.copy());
  }

}