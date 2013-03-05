package org.glob3.mobile.generated; 
//
//  ElevationData.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/17/13.
//
//

//
//  ElevationData.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/17/13.
//
//


//class Vector2I;


public abstract class ElevationData
{
  protected final Sector _sector ;
  protected final int _width;
  protected final int _height;
  protected final double _noDataValue;

  public ElevationData(Sector sector, Vector2I resolution, double noDataValue)
  {
     _sector = new Sector(sector);
     _width = resolution._x;
     _height = resolution._y;
     _noDataValue = noDataValue;
  }

  public void dispose()
  {
  }

  public Vector2I getExtent()
  {
    return new Vector2I(_width, _height);
  }

  public abstract double getElevationAt(int x, int y, int type);

  public abstract double getElevationAt(Angle latitude, Angle longitude, int type);

  public double getElevationAt(Geodetic2D position, int type)
  {
    return getElevationAt(position.latitude(), position.longitude(), type);
  }

  public abstract String description(boolean detailed);

}