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
  public final Angle _latitude ;
  public final Angle _longitude ;


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
    return Angle.fromRadians(bearingInRadians(fromLatitude, fromLongitude, toLatitude, toLongitude));
  }

  /**
   * Returns the (initial) bearing from this point to the supplied point
   *   see http: //williams.best.vwh.net/avform.htm#Crs
   */
  public static double bearingInRadians(Angle fromLatitude, Angle fromLongitude, Angle toLatitude, Angle toLongitude)
  {
    final Angle dLon = toLongitude.sub(fromLongitude);

//    const double toLatCos = toLatitude.cosinus();
    final double toLatCos = java.lang.Math.cos(toLatitude._radians);

//    const double y = dLon.sinus() * toLatCos;
    final double y = java.lang.Math.sin(dLon._radians) * toLatCos;
//    const double x = fromLatitude.cosinus() * toLatitude.sinus() - fromLatitude.sinus() * toLatCos * dLon.cosinus();
    final double x = java.lang.Math.cos(fromLatitude._radians) * java.lang.Math.sin(toLatitude._radians) - java.lang.Math.sin(fromLatitude._radians) * toLatCos * java.lang.Math.cos(dLon._radians);
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

  public final Geodetic2D add(Geodetic2D that)
  {
    return new Geodetic2D(_latitude.add(that._latitude), _longitude.add(that._longitude));
  }

  public final Geodetic2D sub(Geodetic2D that)
  {
    return new Geodetic2D(_latitude.sub(that._latitude), _longitude.sub(that._longitude));
  }

  public final Geodetic2D times(double magnitude)
  {
    return new Geodetic2D(_latitude.times(magnitude), _longitude.times(magnitude));
  }

  public final Geodetic2D div(double magnitude)
  {
    return new Geodetic2D(_latitude.div(magnitude), _longitude.div(magnitude));
  }

  public final boolean closeTo(Geodetic2D other)
  {
    if (!_latitude.closeTo(other._latitude))
    {
      return false;
    }
  
    return _longitude.closeTo(other._longitude);
  }

  public final boolean isBetween(Geodetic2D min, Geodetic2D max)
  {
    return _latitude.isBetween(min._latitude, max._latitude) && _longitude.isBetween(min._longitude, max._longitude);
  }

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
  
    final double cos1 = java.lang.Math.cos(_latitude._radians);
    final Vector3D normal1 = new Vector3D(cos1 * java.lang.Math.cos(_longitude._radians), cos1 * java.lang.Math.sin(_longitude._radians), java.lang.Math.sin(_latitude._radians));
    final double cos2 = java.lang.Math.cos(other._latitude._radians);
    final Vector3D normal2 = new Vector3D(cos2 * java.lang.Math.cos(other._longitude._radians), cos2 * java.lang.Math.sin(other._longitude._radians), java.lang.Math.sin(other._latitude._radians));
  
    return Angle.fromRadians(Math.asin(normal1.cross(normal2).squaredLength()));
  
  }


  /**
   * Returns the (initial) bearing from this point to the supplied point
   *   see http: //williams.best.vwh.net/avform.htm#Crs
   */
  public final Angle bearingTo(Geodetic2D that)
  {
    return bearing(_latitude, _longitude, that._latitude, that._longitude);

  }


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
  @Override
  public String toString() {
    return description();
  }

  public final boolean isEquals(Geodetic2D that)
  {
    return _latitude.isEquals(that._latitude) && _longitude.isEquals(that._longitude);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + ((_latitude == null) ? 0 : _latitude.hashCode());
    result = (prime * result) + ((_longitude == null) ? 0 : _longitude.hashCode());
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
    final Geodetic2D other = (Geodetic2D) obj;
    if (_latitude == null) {
      if (other._latitude != null) {
        return false;
      }
    }
    else if (!_latitude.equals(other._latitude)) {
      return false;
    }
    if (_longitude == null) {
      if (other._longitude != null) {
        return false;
      }
    }
    else if (!_longitude.equals(other._longitude)) {
      return false;
    }
    return true;
  }

}