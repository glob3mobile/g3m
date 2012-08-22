package org.glob3.mobile.generated; 
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

public abstract class Layer
{

  private java.util.ArrayList<TerrainTouchEventListener> _listeners = new java.util.ArrayList<TerrainTouchEventListener>();


  public Layer()
  {

  }

  public void dispose()
  {
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean fullContains(const Sector& s) const = 0;
  public abstract boolean fullContains(Sector s);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual java.util.ArrayList<Petition*> getTilePetitions(const RenderContext* rc, const Tile* tile, int width, int height) const = 0;
  public abstract java.util.ArrayList<Petition> getTilePetitions(RenderContext rc, Tile tile, int width, int height);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean isAvailable(const RenderContext* rc, const Tile* tile) const = 0;
  public abstract boolean isAvailable(RenderContext rc, Tile tile);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean isTransparent() const = 0;
  public abstract boolean isTransparent();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual URL getFeatureURL(const Geodetic2D& g, const IFactory* factory, const Sector& sector, int width, int height) const = 0;
  public abstract URL getFeatureURL(Geodetic2D g, IFactory factory, Sector sector, int width, int height);

  public final void addTerrainTouchEventListener(TerrainTouchEventListener listener)
  {
	_listeners.add(listener);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void onTerrainTouchEventListener(TerrainTouchEvent& tte) const
  public final void onTerrainTouchEventListener(TerrainTouchEvent tte)
  {
	for (int i = 0; i < _listeners.size(); i++)
	{
	  TerrainTouchEventListener listener = _listeners.get(i);
	  if (listener != null)
	  {
		listener.onTerrainTouchEvent(tte);
	  }
	}
  }

}