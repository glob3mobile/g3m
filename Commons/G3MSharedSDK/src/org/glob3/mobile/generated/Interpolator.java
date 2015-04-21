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



public abstract class Interpolator
{
  protected Interpolator()
  {

  }


  public void dispose()
  {
  }

  public double interpolation(Geodetic2D sw, Geodetic2D ne, double valueSW, double valueSE, double valueNE, double valueNW, Geodetic2D position)
  {
    return interpolation(sw, ne, valueSW, valueSE, valueNE, valueNW, position._latitude, position._longitude);
  }

  public double interpolation(Geodetic2D sw, Geodetic2D ne, double valueSW, double valueSE, double valueNE, double valueNW, Angle latitude, Angle longitude)
  {
  
    final double swLatRadians = sw._latitude._radians;
    final double swLonRadians = sw._longitude._radians;
    final double neLatRadians = ne._latitude._radians;
    final double neLonRadians = ne._longitude._radians;
  
    final double deltaLonRadians = neLonRadians - swLonRadians;
    final double deltaLatRadians = neLatRadians - swLatRadians;
  
    final double u = (longitude._radians - swLonRadians) / deltaLonRadians;
    final double v = (neLatRadians - latitude._radians) / deltaLatRadians;
  
    return interpolation(valueSW, valueSE, valueNE, valueNW, u, v);
  }

  public abstract double interpolation(double valueSW, double valueSE, double valueNE, double valueNW, double u, double v);

  public abstract String description();

  @Override
  public String toString() {
    return description();
  }

}