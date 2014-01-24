package org.glob3.mobile.generated; 
//
//  BilinearInterpolator.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/21/13.
//
//

//
//  BilinearInterpolator.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/21/13.
//
//



public class BilinearInterpolator extends Interpolator
{

  public final double interpolation(double valueSW, double valueSE, double valueNE, double valueNW, double u, double v)
  {
    final double alphaSW = (1.0 - u) * v;
    final double alphaSE = u * v;
    final double alphaNE = u * (1.0 - v);
    final double alphaNW = (1.0 - u) * (1.0 - v);
  
    return (alphaSW * valueSW) + (alphaSE * valueSE) + (alphaNE * valueNE) + (alphaNW * valueNW);
  }


  public final String description()
  {
    return "BilinearInterpolator";
  }

  @Override
  public String toString() {
    return description();
  }

}