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
  public final Geodetic2D _g2d ;
  public final Sector _sector ;
  public final Layer _layer;

  public TerrainTouchEvent(Geodetic2D g2d, Sector s, Layer layer)
  {
	  _g2d = new Geodetic2D(g2d);
	  _sector = new Sector(s);
	  _layer = layer;

  }

}