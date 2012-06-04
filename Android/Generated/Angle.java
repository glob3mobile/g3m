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



//C++ TO JAVA CONVERTER NOTE: The following #define macro was replaced in-line:
//#define PI_TIMES_180 (M_PI * 180.0)
//#define THRESHOLD 1e-5



public class Angle
{
  private final double _degrees;

  private Angle(double degrees)
  {
	  _degrees = degrees;

  }


  public static Angle fromDegrees(double degrees)
  {
	return new Angle(degrees);
  }

  public static Angle fromRadians(double radians)
  {
	return Angle.fromDegrees(radians * (180.0 / Math.PI));
  }

  public static Angle zero()
  {
	return Angle.fromDegrees(0);
  }

  public Angle(Angle angle)
  {
	  _degrees = angle._degrees;

  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double sinus() const
  public final double sinus()
  {
	return Math.sin(_degrees / Math.PI * 180.0);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double cosinus() const
  public final double cosinus()
  {
	return Math.cos(_degrees / Math.PI * 180.0);
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
	return _degrees / Math.PI * 180.0;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean closeTo(const Angle& other) const
  public final boolean closeTo(Angle other)
  {
	return (Math.abs(_degrees - other._degrees) < DefineConstants.THRESHOLD);
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

}