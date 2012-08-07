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
//ORIGINAL LINE: java.util.ArrayList<Petition*> createTilePetitions(const RenderContext* rc, const IFactory& factory, const Tile& tile, int width, int height) const
  public final java.util.ArrayList<Petition> createTilePetitions(RenderContext rc, IFactory factory, Tile tile, int width, int height)
  {
	java.util.ArrayList<Petition> petitions = new java.util.ArrayList<Petition>();
  
	for (int i = 0; i < _layers.size(); i++)
	{
	  Layer layer = _layers.get(i);
	  if (layer.isAvailable(rc, tile))
	  {
		java.util.ArrayList<Petition> pet = layer.getTilePetitions(factory, tile, width, height);
  
		//Storing petitions
		for (int j = 0; j < pet.size(); j++)
		{
		  petitions.add(pet.get(j));
		}
	  }
	}
  
	return petitions;
  }


}