package org.glob3.mobile.generated; 
//
//  Sector.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 22/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

//
//  Sector.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//




public class Sector
{

  private final Geodetic2D _lower ;
  private final Geodetic2D _upper ;

  private final Angle _deltaLatitude ;
  private final Angle _deltaLongitude ;



  public Sector(Geodetic2D lower, Geodetic2D upper)
  {
	  _lower = new Geodetic2D(lower);
	  _upper = new Geodetic2D(upper);
	  _deltaLatitude = new Angle(upper.latitude().sub(lower.latitude()));
	  _deltaLongitude = new Angle(upper.longitude().sub(lower.longitude()));
  }

  public Sector(Sector s)
  {
	  _lower = new Geodetic2D(s._lower);
	  _upper = new Geodetic2D(s._upper);
	  _deltaLatitude = new Angle(s._deltaLatitude);
	  _deltaLongitude = new Angle(s._deltaLongitude);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Geodetic2D lower() const
  public final Geodetic2D lower()
  {
	return _lower;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Geodetic2D upper() const
  public final Geodetic2D upper()
  {
	return _upper;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean contains(const Geodetic2D &position) const
  public final boolean contains(Geodetic2D position)
  {
	return position.isBetween(_lower, _upper);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean touchesWith(const Sector &that) const
  public final boolean touchesWith(Sector that)
  {
	// from Real-Time Collision Detection - Christer Ericson
	//   page 79
  
	// Exit with no intersection if separated along an axis
	if (_upper.latitude().lowerThan(that._lower.latitude()) || _lower.latitude().greaterThan(that._upper.latitude()))
	{
	  return false;
	}
	if (_upper.longitude().lowerThan(that._lower.longitude()) || _lower.longitude().greaterThan(that._upper.longitude()))
	{
	  return false;
	}
  
	// Overlapping on all axes means Sectors are intersecting
	return true;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Angle getDeltaLatitude() const
  public final Angle getDeltaLatitude()
  {
	return _deltaLatitude;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Angle getDeltaLongitude() const
  public final Angle getDeltaLongitude()
  {
	return _deltaLongitude;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Geodetic2D getSW() const
  public final Geodetic2D getSW()
  {
	return _lower;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Geodetic2D getNE() const
  public final Geodetic2D getNE()
  {
	return _upper;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Geodetic2D getNW() const
  public final Geodetic2D getNW()
  {
	return new Geodetic2D(_upper.latitude(), _lower.longitude());
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Geodetic2D getSE() const
  public final Geodetic2D getSE()
  {
	return new Geodetic2D(_lower.latitude(), _upper.longitude());
  }


}