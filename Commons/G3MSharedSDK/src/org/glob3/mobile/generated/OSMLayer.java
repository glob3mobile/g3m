package org.glob3.mobile.generated;import java.util.*;

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


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: String getLayerType() const
  protected final String getLayerType()
  {
	return "OSM";
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean rawIsEquals(const Layer* that) const
  protected final boolean rawIsEquals(Layer that)
  {
	return true;
  }

  public OSMLayer(TimeInterval timeToCache, boolean readExpired, int initialLevel, float transparency, LayerCondition condition)
  {
	  this(timeToCache, readExpired, initialLevel, transparency, condition, new java.util.ArrayList<const Info*>());
  }
  public OSMLayer(TimeInterval timeToCache, boolean readExpired, int initialLevel, float transparency)
  {
	  this(timeToCache, readExpired, initialLevel, transparency, null, new java.util.ArrayList<const Info*>());
  }
  public OSMLayer(TimeInterval timeToCache, boolean readExpired, int initialLevel)
  {
	  this(timeToCache, readExpired, initialLevel, 1, null, new java.util.ArrayList<const Info*>());
  }
  public OSMLayer(TimeInterval timeToCache, boolean readExpired)
  {
	  this(timeToCache, readExpired, 2, 1, null, new java.util.ArrayList<const Info*>());
  }
  public OSMLayer(TimeInterval timeToCache)
  {
	  this(timeToCache, true, 2, 1, null, new java.util.ArrayList<const Info*>());
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: OSMLayer(const TimeInterval& timeToCache, const boolean readExpired = true, const int initialLevel = 2, const float transparency = 1, LayerCondition* condition = null, java.util.ArrayList<const Info*>* layerInfo = new java.util.ArrayList<const Info*>()) : MercatorTiledLayer("http://", "tile.openstreetmap.org", getSubdomains(), "png", timeToCache, readExpired, initialLevel, 18, false, transparency, condition, layerInfo)
  public OSMLayer(TimeInterval timeToCache, boolean readExpired, int initialLevel, float transparency, LayerCondition condition, java.util.ArrayList<Info> layerInfo) // isTransparent
  {
	  super("http://", "tile.openstreetmap.org", getSubdomains(), "png", timeToCache, readExpired, initialLevel, 18, false, transparency, condition, layerInfo);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String description() const
  public final String description()
  {
	return "[OSMLayer]";
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: OSMLayer* copy() const
  public final OSMLayer copy()
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return new OSMLayer(_timeToCache, _readExpired, _initialLevel, _transparency, (_condition == null) ? null : _condition->copy(), _layerInfo);
	return new OSMLayer(new TimeInterval(_timeToCache), _readExpired, _initialLevel, _transparency, (_condition == null) ? null : _condition.copy(), _layerInfo);
  }

  public final RenderState getRenderState()
  {
	return RenderState.ready();
  }

}
