package org.glob3.mobile.generated; 
//
//  Geodetic3D.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

//
//  Geodetic2D.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//



/**
 * Class to represent a position in the globe by latitude, longitud and altitude.
 */
public class Geodetic2D
{
  private final Angle _latitude ;
  private final Angle _longitude ;


  public static Geodetic2D zero()
  {
	return new Geodetic2D(Angle.zero(), Angle.zero());
  }
  public static Geodetic2D fromDegrees(double lat, double lon)
  {
	return new Geodetic2D(Angle.fromDegrees(lat), Angle.fromDegrees(lon));
  }

  public Geodetic2D(Angle latitude, Angle longitude)
  {
	  _latitude = new Angle(latitude);
	  _longitude = new Angle(longitude);
  }

  public Geodetic2D(Geodetic2D g)
  {
	  _latitude = new Angle(g._latitude);
	  _longitude = new Angle(g._longitude);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Angle latitude() const
  public final Angle latitude()
  {
	return _latitude;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Angle longitude() const
  public final Angle longitude()
  {
	return _longitude;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean closeTo(const Geodetic2D &other) const
  public final boolean closeTo(Geodetic2D other)
  {
	if (!_latitude.closeTo(other._latitude))
	{
	  return false;
	}
  
	return _longitude.closeTo(other._longitude);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isBetween(const Geodetic2D& min, const Geodetic2D& max) const
  public final boolean isBetween(Geodetic2D min, Geodetic2D max)
  {
	return _latitude.isBetween(min.latitude(), max.latitude()) && _longitude.isBetween(min.longitude(), max.longitude());
  }

}