package org.glob3.mobile.generated; 
//
//  LayerSet.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 23/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  LayerSet.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 23/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Petition;

public class LayerSet
{
  private java.util.ArrayList<Layer> _layers = new java.util.ArrayList<Layer>();


  public void dispose()
  {
	for (int i = 0; i < _layers.size(); i++)
	{
	  if (_layers.get(i) != null)
		  _layers.get(i).dispose();
	}
  }

  public final void addLayer(Layer layer)
  {
	_layers.add(layer);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: java.util.ArrayList<Petition*> createTileMapPetitions(const RenderContext* rc, const Tile* tile, int width, int height) const
  public final java.util.ArrayList<Petition> createTileMapPetitions(RenderContext rc, Tile tile, int width, int height)
  {
	java.util.ArrayList<Petition> petitions = new java.util.ArrayList<Petition>();
  
	for (int i = 0; i < _layers.size(); i++)
	{
	  Layer layer = _layers.get(i);
	  if (layer.isAvailable(rc, tile))
	  {
		java.util.ArrayList<Petition> pet = layer.getMapPetitions(rc, tile, width, height);
  
		//Storing petitions
		for (int j = 0; j < pet.size(); j++)
		{
		  petitions.add(pet.get(j));
		}
	  }
	}
  
	if (petitions.isEmpty())
	{
	  rc.getLogger().logWarning("Can't create map petitions for tile %s", tile.getKey().description());
	}
  
	return petitions;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void onTerrainTouchEvent(const EventContext* ec, const Geodetic3D& position, const Tile* tile) const
  public final void onTerrainTouchEvent(EventContext ec, Geodetic3D position, Tile tile)
  {
  
	for (int i = 0; i < _layers.size(); i++)
	{
	  Layer layer = _layers.get(i);
	  if (layer.isAvailable(ec, tile))
	  {
		TerrainTouchEvent tte = new TerrainTouchEvent(position, tile.getSector(), layer);
  
		layer.onTerrainTouchEventListener(ec, tte);
	  }
	}
  
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isReady()const
  public final boolean isReady()
  {
	for (int i = 0; i<_layers.size(); i++)
	{
	  if (!(_layers.get(i).isReady()))
	  {
		return false;
	  }
	}
	return true;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void initialize(const InitializationContext* ic)const
  public final void initialize(InitializationContext ic)
  {
	for (int i = 0; i<_layers.size(); i++)
	{
	  _layers.get(i).initialize(ic);
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int size() const
  public final int size()
  {
	return _layers.size();
  }

}