package org.glob3.mobile.generated;import java.util.*;

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


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Angle;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Geodetic2D;



public abstract class Interpolator
{
  protected Interpolator()
  {

  }


  public void dispose()
  {
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual double interpolation(const Geodetic2D& sw, const Geodetic2D& ne, double valueSW, double valueSE, double valueNE, double valueNW, const Geodetic2D& position) const
  public double interpolation(Geodetic2D sw, Geodetic2D ne, double valueSW, double valueSE, double valueNE, double valueNW, Geodetic2D position)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return interpolation(sw, ne, valueSW, valueSE, valueNE, valueNW, position._latitude, position._longitude);
	return interpolation(new Geodetic2D(sw), new Geodetic2D(ne), valueSW, valueSE, valueNE, valueNW, position._latitude, position._longitude);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual double interpolation(const Geodetic2D& sw, const Geodetic2D& ne, double valueSW, double valueSE, double valueNE, double valueNW, const Angle& latitude, const Angle& longitude) const
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual double interpolation(double valueSW, double valueSE, double valueNE, double valueNW, double u, double v) const = 0;
  public abstract double interpolation(double valueSW, double valueSE, double valueNE, double valueNW, double u, double v);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual const String description() const = 0;
  public abstract String description();

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public final Override public String toString()
  {
	return description();
  }
//#endif

}
