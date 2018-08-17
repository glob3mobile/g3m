package org.glob3.mobile.generated;//
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double interpolation(double valueSW, double valueSE, double valueNE, double valueNW, double u, double v) const
  public final double interpolation(double valueSW, double valueSE, double valueNE, double valueNW, double u, double v)
  {
	final double alphaSW = (1.0 - u) * v;
	final double alphaSE = u * v;
	final double alphaNE = u * (1.0 - v);
	final double alphaNW = (1.0 - u) * (1.0 - v);
  
	return (alphaSW * valueSW) + (alphaSE * valueSE) + (alphaNE * valueNE) + (alphaNW * valueNW);
  }


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String description() const
  public final String description()
  {
	return "BilinearInterpolator";
  }

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public final Override public String toString()
  {
	return description();
  }
//#endif

}
