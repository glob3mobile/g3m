package org.glob3.mobile.generated; 
//
//  Planet.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//





public class Planet extends Ellipsoid
{
  private String _name;


  public Planet(String name, Vector3D radii)
  {
	  super(radii);
	  _name = name;
  }

  public static Planet createEarth()
  {
	return new Planet("Earth", new Vector3D(6378137.0, 6378137.0, 6356752.314245));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: String getName() const
  public final String getName()
  {
	return _name;
  }

}