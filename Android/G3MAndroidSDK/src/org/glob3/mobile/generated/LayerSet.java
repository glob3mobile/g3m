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

  public final void add(Layer l)
  {
	_layers.add(l);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: java.util.ArrayList<Petition*> createTilePetitions(const Tile& tile, int width, int height) const
  public final java.util.ArrayList<Petition> createTilePetitions(Tile tile, int width, int height)
  {
	java.util.ArrayList<Petition> petitions = new java.util.ArrayList<Petition>();
  
	final Sector tileSector = tile.getSector();
  
	if (tileSector.getDeltaLatitude().degrees() < 45)
	{
	  int a = 0;
	  a++;
	}
  
	for (int i = 0; i < _layers.size(); i++)
	{
	  Layer layer = _layers.get(i);
	  if (layer.fullContains(tileSector))
	  {
		java.util.ArrayList<Petition> pet = layer.getTilePetitions(tile, width, height);
		for (int i = 0; i < pet.size(); i++)
		{
		  petitions.add(pet.get(i));
		}
	  }
	}
  
	return petitions;
  }


}