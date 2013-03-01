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



public class Angle
{
  private Angle(double degrees)
  {
     _degrees = degrees;
     _radians = degrees / 180.0 * 3.14159265358979323846264338327950288;
  }

  public final double _degrees;
  public final double _radians;

  public static Angle fromDegrees(double degrees)
  {
    return new Angle(degrees);
  }

  public static Angle fromDegreesMinutes(double degrees, double minutes)
  {
    return new Angle(degrees + (minutes / 60.0));
  }

  public static Angle fromDegreesMinutesSeconds(double degrees, double minutes, double seconds)
  {
    return new Angle(degrees + (minutes / 60.0) + (seconds / 3600.0));
  }

  public static Angle fromRadians(double radians)
  {
    return Angle.fromDegrees(radians / IMathUtils.instance().pi() * 180.0);
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

  public static Angle nan()
  {
    return Angle.fromDegrees(IMathUtils.instance().NanD());
  }

  public static Angle midAngle(Angle angle1, Angle angle2)
  {
    return Angle.fromDegrees((angle1._degrees + angle2._degrees) / 2);
  }

  public static Angle linearInterpolation(Angle from, Angle to, double alpha)
  {
    return Angle.fromDegrees((1.0-alpha) * from._degrees + alpha * to._degrees);
  }

  public final boolean isNan()
  {
    return IMathUtils.instance().isNan(_degrees);
  }

  public Angle(Angle angle)
  {
     _degrees = angle._degrees;
     _radians = angle._radians;

  }

  public final double sinus()
  {
    return IMathUtils.instance().sin(_radians);
  }

  public final double cosinus()
  {
    return IMathUtils.instance().cos(_radians);
  }

  public final double degrees()
  {
    return _degrees;
  }

  public final double radians()
  {
    return _radians;
  }

  public final boolean closeTo(Angle other)
  {
    return (IMathUtils.instance().abs(_degrees - other._degrees) < DefineConstants.THRESHOLD);
  }

  public final Angle add(Angle a)
  {
    return new Angle(_degrees + a._degrees);
  }

  public final Angle sub(Angle a)
  {
    return new Angle(_degrees - a._degrees);
  }

  public final Angle times(double k)
  {
    return new Angle(k * _degrees);
  }

  public final Angle div(double k)
  {
    return new Angle(_degrees / k);
  }

  public final double div(Angle k)
  {
    return _degrees / k._degrees;
  }

  public final boolean greaterThan(Angle a)
  {
    return (_degrees > a._degrees);
  }

  public final boolean lowerThan(Angle a)
  {
    return (_degrees < a._degrees);
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
    return new Angle(degrees);
  }

  public final boolean isZero()
  {
    return (_degrees == 0);
  }

  public final boolean isEqualsTo(Angle that)
  {
    return (_degrees == that._degrees) || (_radians == that._radians);
  }

  @Override
	public int hashCode() {
		return Double.toString(_degrees).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
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
    if (_degrees != other._degrees) {
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

}