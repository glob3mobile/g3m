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

  public static double interpolate(Geodetic2D sw, Geodetic2D ne, double valueSW, double valueSE, double valueNE, double valueNW, Geodetic2D position)
  {
    return interpolate(sw, ne, valueSW, valueSE, valueNE, valueNW, position.latitude(), position.longitude());
  }

  public static double interpolate(Geodetic2D sw, Geodetic2D ne, double valueSW, double valueSE, double valueNE, double valueNW, Angle latitude, Angle longitude)
  {
  
    final double swLatRadians = sw.latitude().radians();
    final double swLonRadians = sw.longitude().radians();
    final double neLatRadians = ne.latitude().radians();
    final double neLonRadians = ne.longitude().radians();
  
    final double deltaLonRadians = neLonRadians - swLonRadians;
    final double deltaLatRadians = neLatRadians - swLatRadians;
  
    final double u = (longitude.radians() - swLonRadians) / deltaLonRadians;
    final double v = (neLatRadians - latitude.radians()) / deltaLatRadians;
  
    return interpolate(sw, ne, valueSW, valueSE, valueNE, valueNW, u, v);
  }

  public static double interpolate(Geodetic2D sw, Geodetic2D ne, double valueSW, double valueSE, double valueNE, double valueNW, double u, double v)
  {
    final double alphaSW = (1.0 - u) * v;
    final double alphaSE = u * v;
    final double alphaNE = u * (1.0 - v);
    final double alphaNW = (1.0 - u) * (1.0 - v);
  
    return (alphaSW * valueSW) + (alphaSE * valueSE) + (alphaNE * valueNE) + (alphaNW * valueNW);
  }

}