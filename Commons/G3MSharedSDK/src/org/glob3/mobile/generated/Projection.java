package org.glob3.mobile.generated;
//
//  Projection.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/14/16.
//
//

//
//  Projection.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/14/16.
//
//




//class Angle;
//class Geodetic2D;
//class Vector2D;
//class MutableVector2D;
//class Sector;


public abstract class Projection extends RCObject
{


  protected Projection()
  {
  
  }

  public void dispose()
  {
    super.dispose();
  }


  public abstract String getEPSG();


  public abstract double getU(Angle longitude);
  public abstract double getV(Angle latitude);

  public Vector2D getUV(Angle latitude, Angle longitude)
  {
    return new Vector2D(getU(longitude), getV(latitude));
  }
  public Vector2D getUV(Geodetic2D position)
  {
    return new Vector2D(getU(position._longitude), getV(position._latitude));
  }

  public void getUV(Angle latitude, Angle longitude, MutableVector2D result)
  {
    result.set(getU(longitude), getV(latitude));
  }
  public void getUV(Geodetic2D position, MutableVector2D result)
  {
    result.set(getU(position._longitude), getV(position._latitude));
  }

  public abstract Angle getInnerPointLongitude(double u);
  public abstract Angle getInnerPointLatitude(double v);

  public abstract Angle getInnerPointLongitude(Sector sector, double u);
  public abstract Angle getInnerPointLatitude(Sector sector, double v);

}
