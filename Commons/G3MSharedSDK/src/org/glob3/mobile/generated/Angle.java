package org.glob3.mobile.generated;//
//  Angle.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//

//
//  Angle.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//


//#define THRESHOLD 1e-5



//C++ TO JAVA CONVERTER NOTE: The following #define macro was replaced in-line:
//#define TO_RADIANS(degrees) ((degrees) / 180.0 * 3.14159265358979323846264338327950288)
//C++ TO JAVA CONVERTER NOTE: The following #define macro was replaced in-line:
//#define TO_DEGREES(radians) ((radians) * (180.0 / 3.14159265358979323846264338327950288))


public class Angle
{
//  mutable double _sin;
//  mutable double _cos;

  private Angle(double degrees, double radians)
//  _sin(2),
//  _cos(2)
  {
	  _degrees = degrees;
	  _radians = radians;
  }


  public final double _degrees;
  public final double _radians;


  public Angle(Angle angle)
//  _sin(angle._sin),
//  _cos(angle._cos)
  {
	  _degrees = angle._degrees;
	  _radians = angle._radians;

  }

  public static Angle fromDegrees(double degrees)
  {
	return new Angle(degrees, ((degrees) / 180.0 * 3.14159265358979323846264338327950288));
  }

  public static Angle fromDegreesMinutes(double degrees, double minutes)
  {
	final IMathUtils mu = IMathUtils.instance();
	final double sign = (degrees * minutes) < 0 ? -1.0 : 1.0;
	final double d = sign * (mu.abs(degrees) + (mu.abs(minutes) / 60.0));
	return new Angle(d, ((d) / 180.0 * 3.14159265358979323846264338327950288));
  }

  public static Angle fromDegreesMinutesSeconds(double degrees, double minutes, double seconds)
  {
	final IMathUtils mu = IMathUtils.instance();
	final double sign = (degrees * minutes * seconds) < 0 ? -1.0 : 1.0;
	final double d = sign * (mu.abs(degrees) + (mu.abs(minutes) / 60.0) + (mu.abs(seconds) / 3600.0));
	return new Angle(d, ((d) / 180.0 * 3.14159265358979323846264338327950288));
  }

  public static Angle fromRadians(double radians)
  {
	return new Angle(((radians) * (180.0 / 3.14159265358979323846264338327950288)), radians);
  }

  public static Angle min(Angle a1, Angle a2)
  {
	return (a1._degrees < a2._degrees) ? a1 : a2;
  }

  public static Angle max(Angle a1, Angle a2)
  {
	return (a1._degrees > a2._degrees) ? a1 : a2;
  }

  public static Angle zero()
  {
	return Angle.fromDegrees(0);
  }

  public static Angle pi()
  {
	return Angle.fromDegrees(180);
  }

  public static Angle halfPi()
  {
	return Angle.fromDegrees(90);
  }

  public static Angle nan()
  {
	return Angle.fromDegrees(NAND);
  }

  public static Angle midAngle(Angle angle1, Angle angle2)
  {
	return Angle.fromRadians((angle1._radians + angle2._radians) / 2);
  }

  public static Angle linearInterpolation(Angle from, Angle to, double alpha)
  {
	return Angle.fromRadians((1.0-alpha) * from._radians + alpha * to._radians);
  }

  public static Angle cosineInterpolation(Angle from, Angle to, double alpha)
  {
	return Angle.fromRadians(IMathUtils.instance().cosineInterpolation(from._radians, to._radians, alpha));
  }

  public static Angle linearInterpolationFromRadians(double fromRadians, double toRadians, double alpha)
  {
	return Angle.fromRadians((1.0-alpha) * fromRadians + alpha * toRadians);
  }

  public static Angle linearInterpolationFromDegrees(double fromDegrees, double toDegrees, double alpha)
  {
	return Angle.fromDegrees((1.0-alpha) * fromDegrees + alpha * toDegrees);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isNan() const
  public final boolean isNan()
  {
	return (_degrees != _degrees);
  }

//  double sinus() const {
////    if (_sin > 1) {
////      _sin = SIN(_radians);
////    }
////    return _sin;
//    return SIN(_radians);
//  }
//
//  double cosinus() const {
////    if (_cos > 1) {
////      _cos = COS(_radians);
////    }
////    return _cos;
//    return COS(_radians);
//  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double tangent() const
  public final double tangent()
  {
	return TAN(_radians);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean closeTo(const Angle& other) const
  public final boolean closeTo(Angle other)
  {
	return (IMathUtils.instance().abs(_degrees - other._degrees) < DefineConstants.THRESHOLD);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Angle add(const Angle& a) const
  public final Angle add(Angle a)
  {
	final double r = _radians + a._radians;
	return new Angle(((r) * (180.0 / 3.14159265358979323846264338327950288)), r);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Angle sub(const Angle& a) const
  public final Angle sub(Angle a)
  {
	final double r = _radians - a._radians;
	return new Angle(((r) * (180.0 / 3.14159265358979323846264338327950288)), r);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Angle times(double k) const
  public final Angle times(double k)
  {
	final double r = k * _radians;
	return new Angle(((r) * (180.0 / 3.14159265358979323846264338327950288)), r);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Angle div(double k) const
  public final Angle div(double k)
  {
	final double r = _radians / k;
	return new Angle(((r) * (180.0 / 3.14159265358979323846264338327950288)), r);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double div(const Angle& k) const
  public final double div(Angle k)
  {
	return _radians / k._radians;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean greaterThan(const Angle& a) const
  public final boolean greaterThan(Angle a)
  {
	return (_radians > a._radians);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean lowerThan(const Angle& a) const
  public final boolean lowerThan(Angle a)
  {
	return (_radians < a._radians);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Angle clampedTo(const Angle& min, const Angle& max) const
  public final Angle clampedTo(Angle min, Angle max)
  {
	if (_radians < min._radians)
	{
	  return min;
	}
  
	if (_radians > max._radians)
	{
	  return max;
	}
  
	return this;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isBetween(const Angle& min, const Angle& max) const
  public final boolean isBetween(Angle min, Angle max)
  {
	return (_radians >= min._radians) && (_radians <= max._radians);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Angle nearestAngleInInterval(const Angle& min, const Angle& max) const
  public final Angle nearestAngleInInterval(Angle min, Angle max)
  {
	// it the interval contains the angle, return this value
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: if (greaterThan(min) && lowerThan(max))
	if (greaterThan(new Angle(min)) && lowerThan(new Angle(max)))
	{
	  return (this);
	}
  
	// look for the extreme closest to the angle
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Angle dif0 = distanceTo(min);
	final Angle dif0 = distanceTo(new Angle(min));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Angle dif1 = distanceTo(max);
	final Angle dif1 = distanceTo(new Angle(max));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return (dif0.lowerThan(dif1))? min : max;
	return (dif0.lowerThan(new Angle(dif1)))? min : max;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Angle distanceTo(const Angle& other) const
  public final Angle distanceTo(Angle other)
  {
	double dif = IMathUtils.instance().abs(_degrees - other._degrees);
	if (dif > 180)
		dif = 360 - dif;
	return Angle.fromDegrees(dif);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Angle normalized() const
  public final Angle normalized()
  {
	double degrees = _degrees;
	while (degrees < 0)
	{
	  degrees += 360;
	}
	while (degrees >= 360)
	{
	  degrees -= 360;
	}
	return new Angle(degrees, ((degrees) / 180.0 * 3.14159265358979323846264338327950288));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isZero() const
  public final boolean isZero()
  {
	return (_degrees == 0);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isEquals(const Angle& that) const
  public final boolean isEquals(Angle that)
  {
	final IMathUtils mu = IMathUtils.instance();
	return mu.isEquals(_degrees, that._degrees) || mu.isEquals(_radians, that._radians);
  }

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public final Override public int hashCode()
  {
	final int prime = 31;
	int result = 1;
	int temp;
	temp = Double.doubleToLongBits(_radians);
	result = (prime * result) + (int)(temp ^ (temp >>> 32));
	return result;
  }


  public Override public boolean equals(final Object obj)
  {
	if (this == obj)
	{
	  return true;
	}
	if (obj == null)
	{
	  return false;
	}
	if (getClass() != obj.getClass())
	{
	  return false;
	}
	final Angle other = (Angle) obj;
	if (Double.doubleToLongBits(_radians) != Double.doubleToLongBits(other._radians))
	{
	  return false;
	}
	return true;
  }
//#endif

  public void dispose()
  {

  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String description() const
  public final String description()
  {
	IStringBuilder isb = IStringBuilder.newStringBuilder();
	isb.addDouble(_degrees);
  //  isb->addString("°");
	isb.addString("d");
	final String s = isb.getString();
	if (isb != null)
		isb.dispose();
	return s;
  }

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public final Override public String toString()
  {
	return description();
  }
//#endif

}
