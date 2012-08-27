package org.glob3.mobile.generated; 
//
//  Layer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 23/08/12.
//
//

//
//  Layer.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 23/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//




//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Petition;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Tile;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class LayerCondition;

public abstract class Layer
{
  private LayerCondition _condition;
  private java.util.ArrayList<TerrainTouchEventListener> _listeners = new java.util.ArrayList<TerrainTouchEventListener>();


  public Layer(LayerCondition condition)
  {
	  _condition = condition;

  }

  public void dispose()
  {
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual java.util.ArrayList<Petition*> getMapPetitions(const RenderContext* rc, const Tile* tile, int width, int height) const = 0;
  public abstract java.util.ArrayList<Petition> getMapPetitions(RenderContext rc, Tile tile, int width, int height);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean isAvailable(const RenderContext* rc, const Tile* tile) const
  public boolean isAvailable(RenderContext rc, Tile tile)
  {
	if (_condition == null)
	{
	  return true;
	}
	return _condition.isAvailable(rc, tile);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean isAvailable(const EventContext* ec, const Tile* tile) const
  public boolean isAvailable(EventContext ec, Tile tile)
  {
	if (_condition == null)
	{
	  return true;
	}
	return _condition.isAvailable(ec, tile);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean isTransparent() const = 0;
  public abstract boolean isTransparent();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual URL getFeatureInfoURL(const Geodetic2D& g, const IFactory* factory, const Sector& sector, int width, int height) const = 0;
  public abstract URL getFeatureInfoURL(Geodetic2D g, IFactory factory, Sector sector, int width, int height);

  public final void addTerrainTouchEventListener(TerrainTouchEventListener listener)
  {
	_listeners.add(listener);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void onTerrainTouchEventListener(const EventContext* ec, TerrainTouchEvent& tte) const
  public final void onTerrainTouchEventListener(EventContext ec, TerrainTouchEvent tte)
  {
	for (int i = 0; i < _listeners.size(); i++)
	{
	  TerrainTouchEventListener listener = _listeners.get(i);
	  if (listener != null)
	  {
		listener.onTerrainTouchEvent(ec, tte);
	  }
	}
  }

}