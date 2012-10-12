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
//#define ISBETWEEN_THRESHOLD 1e-2



public class Angle
{
  private final double _degrees;
  private final double _radians;

  private Angle(double degrees) //GMath.pi()
  {
	  _degrees = degrees;
	  _radians = degrees / 180.0 * 3.14159265358979323846264338327950288;
  }

  public static Angle lerp(Angle start, Angle end, float percent)
  {
	return start.add(end.sub(start).times(percent));
  }

  public static Angle fromDegrees(double degrees)
  {
	return new Angle(degrees);
  }

  public static Angle fromRadians(double radians)
  {
	return Angle.fromDegrees(radians / IMathUtils.instance().pi() * 180.0);
  }

  public static Angle getMin(Angle a1, Angle a2)
  {
	if (a1._degrees < a2._degrees)
		return a1;
	else
		return a2;
  }

  public static Angle getMax(Angle a1, Angle a2)
  {
	if (a1._degrees > a2._degrees)
		return a1;
	else
		return a2;
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
	return Angle.fromDegrees((angle1.degrees() + angle2.degrees()) / 2);
  }


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isNan() const
  public final boolean isNan()
  {
	return IMathUtils.instance().isNan(_degrees);
  }

  public Angle(Angle angle)
  {
	  _degrees = angle._degrees;
	  _radians = angle._radians;

  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double sinus() const
  public final double sinus()
  {
	return IMathUtils.instance().sin(_radians);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double cosinus() const
  public final double cosinus()
  {
	return IMathUtils.instance().cos(_radians);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double degrees() const
  public final double degrees()
  {
	return _degrees;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double radians() const
  public final double radians()
  {
	//return _degrees / 180.0 * GMath.pi();
	return _radians;
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
	return new Angle(_degrees + a._degrees);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Angle sub(const Angle& a) const
  public final Angle sub(Angle a)
  {
	return new Angle(_degrees - a._degrees);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Angle times(double k) const
  public final Angle times(double k)
  {
	return new Angle(k * _degrees);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Angle div(double k) const
  public final Angle div(double k)
  {
	return new Angle(_degrees / k);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double div(const Angle& k) const
  public final double div(Angle k)
  {
	return _degrees / k._degrees;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean greaterThan(const Angle& a) const
  public final boolean greaterThan(Angle a)
  {
	return (_degrees > a._degrees);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean lowerThan(const Angle& a) const
  public final boolean lowerThan(Angle a)
  {
	return (_degrees < a._degrees);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Angle clampedTo(const Angle& min, const Angle& max) const
  public final Angle clampedTo(Angle min, Angle max)
  {
	if (_degrees < min._degrees)
	{
	  return min;
	}
  
	if (_degrees > max._degrees)
	{
	  return max;
	}
  
	return this;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isBetween(const Angle& min, const Angle& max) const
  public final boolean isBetween(Angle min, Angle max)
  {
	return (_degrees + DefineConstants.ISBETWEEN_THRESHOLD >= min._degrees) && (_degrees - DefineConstants.ISBETWEEN_THRESHOLD <= max._degrees);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Angle nearestAngleInInterval(const Angle& min, const Angle& max) const
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
//ORIGINAL LINE: boolean isZero() const
  public final boolean isZero()
  {
	return (_degrees == 0);
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String description() const
  public final String description()
  {
	IStringBuilder isb = IStringBuilder.newStringBuilder();
	isb.addDouble(_degrees);
	isb.addString("°");
	final String s = isb.getString();
	if (isb != null)
		isb.dispose();
	return s;
  }
}