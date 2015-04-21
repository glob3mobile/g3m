package org.glob3.mobile.generated; 
/**
 * Class to represent a position in the globe by latitude, longitud and altitude.
 */
public class Geodetic3D
{

  public final Angle _latitude ;
  public final Angle _longitude ;
  public final double _height;


  public static Geodetic3D nan()
  {
    return new Geodetic3D(Angle.nan(), Angle.nan(), 0);
  }

  public final boolean isNan()
  {
    return _latitude.isNan() || _longitude.isNan();
  }

  public static Geodetic3D zero()
  {
    return new Geodetic3D(Angle.zero(), Angle.zero(), 0);
  }

  public static Geodetic3D fromDegrees(double lat, double lon, double height)
  {
    return new Geodetic3D(Angle.fromDegrees(lat), Angle.fromDegrees(lon), height);
  }

  public static Geodetic3D linearInterpolation(Geodetic3D from, Geodetic3D to, double alpha)
  {
    return new Geodetic3D(Angle.linearInterpolation(from._latitude, to._latitude, alpha), Angle.linearInterpolation(from._longitude, to._longitude, alpha), IMathUtils.instance().linearInterpolation(from._height, to._height, alpha));
                      //((1.0 - alpha) * from._height) + (alpha * to._height)
  }

  public Geodetic3D(Angle latitude, Angle longitude, double height)
  {
     _latitude = new Angle(latitude);
     _longitude = new Angle(longitude);
     _height = height;
  }

  public Geodetic3D(Geodetic2D g2, double height)
  {
     _latitude = new Angle(g2._latitude);
     _longitude = new Angle(g2._longitude);
     _height = height;
  }

  public Geodetic3D(Geodetic3D g)
  {
     _latitude = new Angle(g._latitude);
     _longitude = new Angle(g._longitude);
     _height = g._height;
  }

  public void dispose()
  {
  }

  public final Geodetic2D asGeodetic2D()
  {
    return new Geodetic2D(_latitude, _longitude);
  }

  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("(lat=");
    isb.addString(_latitude.description());
    isb.addString(", lon=");
    isb.addString(_longitude.description());
    isb.addString(", height=");
    isb.addDouble(_height);
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


  public final Geodetic3D add(Geodetic3D that)
  {
    return new Geodetic3D(_latitude.add(that._latitude), _longitude.add(that._longitude), _height + that._height);
  }

  public final Geodetic3D sub(Geodetic3D that)
  {
    return new Geodetic3D(_latitude.sub(that._latitude), _longitude.sub(that._longitude), _height - that._height);
  }

  public final Geodetic3D times(double magnitude)
  {
    return new Geodetic3D(_latitude.times(magnitude), _longitude.times(magnitude), _height * magnitude);
  }

  public final Geodetic3D div(double magnitude)
  {
    return new Geodetic3D(_latitude.div(magnitude), _longitude.div(magnitude), _height / magnitude);
  }

  public final boolean isEquals(Geodetic3D that)
  {
    return (_latitude.isEquals(that._latitude) && _longitude.isEquals(that._longitude) && (_height == that._height));
  }

}