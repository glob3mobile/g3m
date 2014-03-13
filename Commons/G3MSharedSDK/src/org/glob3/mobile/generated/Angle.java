package org.glob3.mobile.generated; 
//
//  Angle.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

//
//  Angle.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//


//#define THRESHOLD 1e-5



//#define TO_RADIANS(degrees) ((degrees) / 180.0 * 3.14159265358979323846264338327950288)
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

  public static Angle nan()
  {
    return Angle.fromDegrees(java.lang.Double.NaN);
  }

  public static Angle midAngle(Angle angle1, Angle angle2)
  {
    return Angle.fromRadians((angle1._radians + angle2._radians) / 2);
  }

  public static Angle linearInterpolation(Angle from, Angle to, double alpha)
  {
    return Angle.fromRadians((1.0-alpha) * from._radians + alpha * to._radians);
  }

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

  public final double tangent()
  {
    return java.lang.Math.tan(_radians);
  }

  public final boolean closeTo(Angle other)
  {
    return (IMathUtils.instance().abs(_degrees - other._degrees) < DefineConstants.THRESHOLD);
  }

  public final Angle add(Angle a)
  {
    final double r = _radians + a._radians;
    return new Angle(((r) * (180.0 / 3.14159265358979323846264338327950288)), r);
  }

  public final Angle sub(Angle a)
  {
    final double r = _radians - a._radians;
    return new Angle(((r) * (180.0 / 3.14159265358979323846264338327950288)), r);
  }

  public final Angle times(double k)
  {
    final double r = k * _radians;
    return new Angle(((r) * (180.0 / 3.14159265358979323846264338327950288)), r);
  }

  public final Angle div(double k)
  {
    final double r = _radians / k;
    return new Angle(((r) * (180.0 / 3.14159265358979323846264338327950288)), r);
  }

  public final double div(Angle k)
  {
    return _radians / k._radians;
  }

  public final boolean greaterThan(Angle a)
  {
    return (_radians > a._radians);
  }

  public final boolean lowerThan(Angle a)
  {
    return (_radians < a._radians);
  }

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

  public final boolean isBetween(Angle min, Angle max)
  {
    return (_radians >= min._radians) && (_radians <= max._radians);
  }

  public final Angle nearestAngleInInterval(Angle min, Angle max)
  {
    // it the interval contains the angle, return this value
    if (greaterThan(min) && lowerThan(max))
    {
      return (this);
    }
  
    // look for the extreme closest to the angle
    final Angle dif0 = distanceTo(min);
    final Angle dif1 = distanceTo(max);
    return (dif0.lowerThan(dif1))? min : max;
  }

  public final Angle distanceTo(Angle other)
  {
    double dif = IMathUtils.instance().abs(_degrees - other._degrees);
    if (dif > 180)
       dif = 360 - dif;
    return Angle.fromDegrees(dif);
  }

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

  public final boolean isZero()
  {
    return (_degrees == 0);
  }

  public final boolean isEquals(Angle that)
  {
    final IMathUtils mu = IMathUtils.instance();
    return mu.isEquals(_degrees, that._degrees) || mu.isEquals(_radians, that._radians);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    long temp;
    temp = Double.doubleToLongBits(_radians);
    result = (prime * result) + (int) (temp ^ (temp >>> 32));
    return result;
  }


  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Angle other = (Angle) obj;
    if (Double.doubleToLongBits(_radians) != Double.doubleToLongBits(other._radians)) {
      return false;
    }
    return true;
  }

  public void dispose()
  {

  }

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

  @Override
  public String toString() {
    return description();
  }

}