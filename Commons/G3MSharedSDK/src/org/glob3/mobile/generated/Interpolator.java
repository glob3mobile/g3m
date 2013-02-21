package org.glob3.mobile.generated; 
//
//  Interpolator.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/20/13.
//
//

//
//  Interpolator.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/20/13.
//
//


//class Angle;
//class Geodetic2D;

public class Interpolator
{
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  Interpolator();

  public static double interpolateHeight(Geodetic2D lower, Geodetic2D upper, double valueSW, double valueSE, double valueNE, double valueNW, Angle latitude, Angle longitude)
  {
  
    final Angle deltaLongitude = upper.longitude().sub(lower.longitude());
    final Angle deltaLatitude = upper.latitude().sub(lower.latitude());
  
    final double u = longitude.sub(lower.longitude()).div(deltaLongitude);
    final double v = upper.latitude().sub(latitude).div(deltaLatitude);
  
  
  //  const Vector2D uv = sector.getUVCoordinates(position);
  //  const double u = uv._x;
  //  //  const double v = 1.0 - uv._y;
  //  const double v = uv._y;
  
    //  const double pll = (1.0 - u) * (1.0 - v);
    //  const double plr = u         * (1.0 - v);
    //  const double pur = u         * v;
    //  const double pul = (1.0 - u) * v;
    final double pll = (1.0 - u) * v;
    final double plr = u * v;
    final double pur = u * (1.0 - v);
    final double pul = (1.0 - u) * (1.0 - v);
  
    final double ll = valueSW;
    final double lr = valueSE;
    final double ur = valueNE;
    final double ul = valueNW;
  
    final double interpolatedHeight = (pll * ll) + (plr * lr) + (pur * ur) + (pul * ul);
  
    return interpolatedHeight;
  }



  ///#include "Sector.hpp"
  
  public static double interpolateHeight(Geodetic2D lower, Geodetic2D upper, double valueSW, double valueSE, double valueNE, double valueNW, Geodetic2D position)
  {
    return interpolateHeight(lower, upper, valueSW, valueSE, valueNE, valueNW, position.latitude(), position.longitude());
  }

}