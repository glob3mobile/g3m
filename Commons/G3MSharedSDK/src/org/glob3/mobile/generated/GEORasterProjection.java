package org.glob3.mobile.generated; 
//
//  GEORasterProjection.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/11/13.
//
//

//
//  GEORasterProjection.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/11/13.
//
//


//class Geodetic2D;

public class GEORasterProjection
{
  private final Sector _sector ;
  private final boolean _mercator;
  private final int _imageWidth;
  private final int _imageHeight;


  public GEORasterProjection(Sector sector, boolean mercator, int imageWidth, int imageHeight)
  {
     _sector = new Sector(sector);
     _mercator = mercator;
     _imageWidth = imageWidth;
     _imageHeight = imageHeight;
  }

  public void dispose()
  {
  }

  public final Vector2F project(Geodetic2D position)
  {
  
    int _TODO_mercator;
  
    final Vector2D uvCoordinates = _sector.getUVCoordinates(position);
  
    return new Vector2F((float)(uvCoordinates._x * _imageWidth), (float)((1.0 - uvCoordinates._y) * _imageHeight));
  }

}