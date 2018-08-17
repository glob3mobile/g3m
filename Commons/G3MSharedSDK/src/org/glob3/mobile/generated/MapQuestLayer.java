package org.glob3.mobile.generated;import java.util.*;

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

  private MapQuestLayer(String domain, java.util.ArrayList<String> subdomains, int initialLevel, int maxLevel, TimeInterval timeToCache, boolean readExpired, float transparency, LayerCondition condition)
  {
	  this(domain, subdomains, initialLevel, maxLevel, timeToCache, readExpired, transparency, condition, new java.util.ArrayList<const Info*>());
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: MapQuestLayer(const String& domain, const java.util.ArrayList<String>& subdomains, const int initialLevel, const int maxLevel, const TimeInterval& timeToCache, const boolean readExpired, const float transparency, const LayerCondition* condition, java.util.ArrayList<const Info*>* layerInfo = new java.util.ArrayList<const Info*>()) : MercatorTiledLayer("http://", domain, subdomains, "jpg", timeToCache, readExpired, initialLevel, maxLevel, false, transparency, condition, layerInfo)
  private MapQuestLayer(String domain, java.util.ArrayList<String> subdomains, int initialLevel, int maxLevel, TimeInterval timeToCache, boolean readExpired, float transparency, LayerCondition condition, java.util.ArrayList<Info> layerInfo) // isTransparent
  {
	  super("http://", domain, subdomains, "jpg", timeToCache, readExpired, initialLevel, maxLevel, false, transparency, condition, layerInfo);
  }


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: String getLayerType() const
  protected final String getLayerType()
  {
	return "MapQuest";
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean rawIsEquals(const Layer* that) const
  protected final boolean rawIsEquals(Layer that)
  {
	MapQuestLayer t = (MapQuestLayer) that;
	return (_domain.equals(t._domain));
  }



  public static MapQuestLayer newOSM(TimeInterval timeToCache, boolean readExpired, int initialLevel, float transparency)
  {
	  return newOSM(timeToCache, readExpired, initialLevel, transparency, null);
  }
  public static MapQuestLayer newOSM(TimeInterval timeToCache, boolean readExpired, int initialLevel)
  {
	  return newOSM(timeToCache, readExpired, initialLevel, 1, null);
  }
  public static MapQuestLayer newOSM(TimeInterval timeToCache, boolean readExpired)
  {
	  return newOSM(timeToCache, readExpired, 2, 1, null);
  }
  public static MapQuestLayer newOSM(TimeInterval timeToCache)
  {
	  return newOSM(timeToCache, true, 2, 1, null);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: static MapQuestLayer* newOSM(const TimeInterval& timeToCache, const boolean readExpired = true, const int initialLevel = 2, const float transparency = 1, const LayerCondition* condition = null)
  public static MapQuestLayer newOSM(TimeInterval timeToCache, boolean readExpired, int initialLevel, float transparency, LayerCondition condition)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return new MapQuestLayer("mqcdn.com/tiles/1.0.0/map", getSubdomains(), initialLevel, 19, timeToCache, readExpired, transparency, condition);
	return new MapQuestLayer("mqcdn.com/tiles/1.0.0/map", getSubdomains(), initialLevel, 19, new TimeInterval(timeToCache), readExpired, transparency, condition);
  }


  public static MapQuestLayer newOpenAerial(TimeInterval timeToCache, boolean readExpired, int initialLevel, float transparency)
  {
	  return newOpenAerial(timeToCache, readExpired, initialLevel, transparency, null);
  }
  public static MapQuestLayer newOpenAerial(TimeInterval timeToCache, boolean readExpired, int initialLevel)
  {
	  return newOpenAerial(timeToCache, readExpired, initialLevel, 1, null);
  }
  public static MapQuestLayer newOpenAerial(TimeInterval timeToCache, boolean readExpired)
  {
	  return newOpenAerial(timeToCache, readExpired, 2, 1, null);
  }
  public static MapQuestLayer newOpenAerial(TimeInterval timeToCache)
  {
	  return newOpenAerial(timeToCache, true, 2, 1, null);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: static MapQuestLayer* newOpenAerial(const TimeInterval& timeToCache, const boolean readExpired = true, const int initialLevel = 2, const float transparency = 1, const LayerCondition* condition = null)
  public static MapQuestLayer newOpenAerial(TimeInterval timeToCache, boolean readExpired, int initialLevel, float transparency, LayerCondition condition)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return new MapQuestLayer("mqcdn.com/tiles/1.0.0/sat", getSubdomains(), initialLevel, 11, timeToCache, readExpired, transparency, condition);
	return new MapQuestLayer("mqcdn.com/tiles/1.0.0/sat", getSubdomains(), initialLevel, 11, new TimeInterval(timeToCache), readExpired, transparency, condition);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String description() const
  public final String description()
  {
	return "[MapQuestLayer]";
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: MapQuestLayer* copy() const
  public final MapQuestLayer copy()
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return new MapQuestLayer(_domain, _subdomains, _initialLevel, _maxLevel, _timeToCache, _readExpired, _transparency, (_condition == null) ? null : _condition->copy(), _layerInfo);
	return new MapQuestLayer(_domain, _subdomains, _initialLevel, _maxLevel, new TimeInterval(_timeToCache), _readExpired, _transparency, (_condition == null) ? null : _condition.copy(), _layerInfo);
  }

  public final RenderState getRenderState()
  {
	return RenderState.ready();
  }
}
