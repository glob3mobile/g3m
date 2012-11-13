package org.glob3.mobile.generated; 
//
//  TerrainTouchEventListener.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 14/08/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Layer;

public class TerrainTouchEvent
{
  private final Geodetic3D _position ;
  private final Sector _sector ;
  private final Layer _layer;

  public TerrainTouchEvent(Geodetic3D position, Sector sector, Layer layer)
  {
	  _position = new Geodetic3D(position);
	  _sector = new Sector(sector);
	  _layer = layer;

  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Geodetic3D getPosition() const
  public final Geodetic3D getPosition()
  {
	return _position;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Sector getSector() const
  public final Sector getSector()
  {
	return _sector;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Layer* getLayer() const
  public final Layer getLayer()
  {
	return _layer;
  }

}