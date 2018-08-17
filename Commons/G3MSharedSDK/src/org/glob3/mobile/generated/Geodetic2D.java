package org.glob3.mobile.generated;import java.util.*;

//
//  Geodetic3D.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//

//
//  Geodetic2D.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//




/**
 * Class to represent a position in the globe by latitude, longitud and altitude.
 */
public class Geodetic2D
{
  public final Angle _latitude = new Angle();
  public final Angle _longitude = new Angle();


  public static Geodetic2D zero()
  {
	return new Geodetic2D(Angle.zero(), Angle.zero());
  }

  public static Geodetic2D fromDegrees(double lat, double lon)
  {
	return new Geodetic2D(Angle.fromDegrees(lat), Angle.fromDegrees(lon));
  }

  public static Geodetic2D fromRadians(double lat, double lon)
  {
	return new Geodetic2D(Angle.fromRadians(lat), Angle.fromRadians(lon));
  }

  public static Geodetic2D linearInterpolation(Geodetic2D from, Geodetic2D to, double alpha)
  {
	return new Geodetic2D(Angle.linearInterpolation(from._latitude, to._latitude, alpha), Angle.linearInterpolation(from._longitude, to._longitude, alpha));
  }


  /**
   * Returns the (initial) bearing from this point to the supplied point
   *   see http: //williams.best.vwh.net/avform.htm#Crs
   */
  public static Angle bearing(Angle fromLatitude, Angle fromLongitude, Angle toLatitude, Angle toLongitude)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return Angle::fromRadians(bearingInRadians(fromLatitude, fromLongitude, toLatitude, toLongitude));
	return Angle.fromRadians(bearingInRadians(new Angle(fromLatitude), new Angle(fromLongitude), new Angle(toLatitude), new Angle(toLongitude)));
  }

  /**
   * Returns the (initial) bearing from this point to the supplied point
   *   see http: //williams.best.vwh.net/avform.htm#Crs
   */
  public static double bearingInRadians(Angle fromLatitude, Angle fromLongitude, Angle toLatitude, Angle toLongitude)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Angle dLon = toLongitude.sub(fromLongitude);
	final Angle dLon = toLongitude.sub(new Angle(fromLongitude));

//    const double toLatCos = toLatitude.cosinus();
	final double toLatCos = COS(toLatitude._radians);

//    const double y = dLon.sinus() * toLatCos;
	final double y = SIN(dLon._radians) * toLatCos;
//    const double x = fromLatitude.cosinus() * toLatitude.sinus() - fromLatitude.sinus() * toLatCos * dLon.cosinus();
	final double x = COS(fromLatitude._radians) * SIN(toLatitude._radians) - SIN(fromLatitude._radians) * toLatCos * COS(dLon._radians);
	final double radians = IMathUtils.instance().atan2(y, x);

	return radians;
  }

  /**
   * Returns the (initial) bearing from this point to the supplied point
   *   see http: //williams.best.vwh.net/avform.htm#Crs
   */
  public static Angle bearing(Geodetic2D from, Geodetic2D to)
  {
	return bearing(from._latitude, from._longitude, to._latitude, to._longitude);
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

  public void dispose()
  {

  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Geodetic2D add(const Geodetic2D& that) const
  public final Geodetic2D add(Geodetic2D that)
  {
	return new Geodetic2D(_latitude.add(that._latitude), _longitude.add(that._longitude));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Geodetic2D sub(const Geodetic2D& that) const
  public final Geodetic2D sub(Geodetic2D that)
  {
	return new Geodetic2D(_latitude.sub(that._latitude), _longitude.sub(that._longitude));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Geodetic2D times(const double magnitude) const
  public final Geodetic2D times(double magnitude)
  {
	return new Geodetic2D(_latitude.times(magnitude), _longitude.times(magnitude));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Geodetic2D div(const double magnitude) const
  public final Geodetic2D div(double magnitude)
  {
	return new Geodetic2D(_latitude.div(magnitude), _longitude.div(magnitude));
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
	return _latitude.isBetween(min._latitude, max._latitude) && _longitude.isBetween(min._longitude, max._longitude);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Angle angleTo(const Geodetic2D& other) const
  public final Angle angleTo(Geodetic2D other)
  {
  //  const double cos1 = _latitude.cosinus();
  //  const Vector3D normal1(cos1 * _longitude.cosinus(),
  //                         cos1 * _longitude.sinus(),
  //                         _latitude.sinus());
  //  const double cos2 = other._latitude.cosinus();
  //  const Vector3D normal2(cos2 * other._longitude.cosinus(),
  //                         cos2 * other._longitude.sinus(),
  //                         other._latitude.sinus());
  
	final double cos1 = COS(_latitude._radians);
	final Vector3D normal1 = new Vector3D(cos1 * COS(_longitude._radians), cos1 * SIN(_longitude._radians), SIN(_latitude._radians));
	final double cos2 = COS(other._latitude._radians);
	final Vector3D normal2 = new Vector3D(cos2 * COS(other._longitude._radians), cos2 * SIN(other._longitude._radians), SIN(other._latitude._radians));
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return Angle::fromRadians(asin(normal1.cross(normal2).squaredLength()));
	return Angle.fromRadians(Math.asin(normal1.cross(new Vector3D(normal2)).squaredLength()));
  
  }


  /**
   * Returns the (initial) bearing from this point to the supplied point
   *   see http: //williams.best.vwh.net/avform.htm#Crs
   */
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Angle bearingTo(const Geodetic2D& that) const
  public final Angle bearingTo(Geodetic2D that)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return bearing(_latitude, _longitude, that._latitude, that._longitude);
	return bearing(new Angle(_latitude), new Angle(_longitude), that._latitude, that._longitude);

  }

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean operator <(const Geodetic2D& that) const
//C++ TO JAVA CONVERTER TODO TASK: Operators cannot be overloaded in Java:
  boolean operator <(Geodetic2D that)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return lowerThan(that);
	return lowerThan(new Geodetic2D(that));
  }
//#endif

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean lowerThan(const Geodetic2D& that) const
  public final boolean lowerThan(Geodetic2D that)
  {
	if (_latitude.lowerThan(that._latitude))
	{
	  return true;
	}
	else if (_latitude.greaterThan(that._latitude))
	{
	  return false;
	}
	return _longitude.lowerThan(that._longitude);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String description() const
  public final String description()
  {
	IStringBuilder isb = IStringBuilder.newStringBuilder();
	isb.addString("(lat=");
	isb.addString(_latitude.description());
	isb.addString(", lon=");
	isb.addString(_longitude.description());
	isb.addString(")");
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isEquals(const Geodetic2D& that) const
  public final boolean isEquals(Geodetic2D that)
  {
	return _latitude.isEquals(that._latitude) && _longitude.isEquals(that._longitude);
  }

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public final Override public int hashCode()
  {
	final int prime = 31;
	int result = 1;
	result = (prime * result) + ((_latitude == null) ? 0 : _latitude.hashCode());
	result = (prime * result) + ((_longitude == null) ? 0 : _longitude.hashCode());
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
	final Geodetic2D other = (Geodetic2D) obj;
	if (_latitude == null)
	{
	  if (other._latitude != null)
	  {
		return false;
	  }
	}
	else if (!_latitude.equals(other._latitude))
	{
	  return false;
	}
	if (_longitude == null)
	{
	  if (other._longitude != null)
	  {
		return false;
	  }
	}
	else if (!_longitude.equals(other._longitude))
	{
	  return false;
	}
	return true;
  }
//#endif

}
