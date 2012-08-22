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
	  _layers.get(i).dispose();
	}
  }

  public final void addLayer(Layer layer)
  {
	_layers.add(layer);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: java.util.ArrayList<Petition*> createTilePetitions(const RenderContext* rc, const Tile* tile, int width, int height) const
  public final java.util.ArrayList<Petition> createTilePetitions(RenderContext rc, Tile tile, int width, int height)
  {
	java.util.ArrayList<Petition> petitions = new java.util.ArrayList<Petition>();
  
	for (int i = 0; i < _layers.size(); i++)
	{
	  Layer layer = _layers.get(i);
	  if (layer.isAvailable(rc, tile))
	  {
		java.util.ArrayList<Petition> pet = layer.getTilePetitions(rc, tile, width, height);
  
		//Storing petitions
		for (int j = 0; j < pet.size(); j++)
		{
		  petitions.add(pet.get(j));
		}
	  }
	}
  
	return petitions;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void onTerrainTouchEvent(const Geodetic3D& g3d, const Tile* tile) const
  public final void onTerrainTouchEvent(Geodetic3D g3d, Tile tile)
  {
  
	for (int i = 0; i < _layers.size(); i++)
	{
	  Layer layer = _layers.get(i);
  
	  TerrainTouchEvent tte = new TerrainTouchEvent(g3d.asGeodetic2D(), tile.getSector(), layer);
  
	  layer.onTerrainTouchEventListener(tte);
	}
  
  
  
  }

}