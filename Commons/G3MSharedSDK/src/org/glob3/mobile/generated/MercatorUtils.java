package org.glob3.mobile.generated; 
//
//  MercatorUtils.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/7/13.
//
//

//
//  MercatorUtils.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/7/13.
//
//




public class MercatorUtils
{
  private MercatorUtils()
  {
  }

  private static final Angle _upperLimit = Angle.fromDegrees(85.0511287798);
  private static final Angle _lowerLimit = Angle.fromDegrees(-85.0511287798);

  private static double _upperLimitInRadians = Angle.fromDegrees(85.0511287798)._radians;
  private static double _lowerLimitInRadians = Angle.fromDegrees(-85.0511287798)._radians;

  private static double _upperLimitInDegrees = 85.0511287798;
  private static double _lowerLimitInDegrees = -85.0511287798;

  private static double _originShift = 2.0 * 3.14159265358979323846264338327950288 * 6378137.0 / 2.0;


  public static Angle upperLimit()
  {
    return _upperLimit;
  }

  public static Angle lowerLimit()
  {
    return _lowerLimit;
  }

  public static double upperLimitInDegrees()
  {
    return _upperLimitInDegrees;
  }

  public static double lowerLimitInDegrees()
  {
    return _lowerLimitInDegrees;
  }

  public static double upperLimitInRadians()
  {
    return _upperLimitInRadians;
  }

  public static double lowerLimitInRadians()
  {
    return _lowerLimitInRadians;
  }

  public static double clampIntoLimitsInRadians(Angle angle)
  {
    final double radians = angle._radians;
    if (radians < _lowerLimitInRadians)
    {
      return _lowerLimitInRadians;
    }
    if (radians > _upperLimitInRadians)
    {
      return _upperLimitInRadians;
    }
    return radians;
  }

  public static double getMercatorV(Angle latitude)
  {
    if (latitude._degrees >= _upperLimitInDegrees)
    {
      return 0;
    }
    if (latitude._degrees <= _lowerLimitInDegrees)
    {
      return 1;
    }
  
    final IMathUtils mu = IMathUtils.instance();
    final double pi4 = DefineConstants.PI * 4;
  
    final double latSin = java.lang.Math.sin(latitude._radians);
    return 1.0 - ((mu.log((1.0 + latSin) / (1.0 - latSin)) / pi4) + 0.5);
  }

  public static Angle toLatitude(double v)
  {
    final IMathUtils mu = IMathUtils.instance();
  
    final double exp = mu.exp(-2 * DefineConstants.PI * (1.0 - v - 0.5));
    final double atan = mu.atan(exp);
    return Angle.fromRadians((DefineConstants.PI / 2) - 2 * atan);
  }

  public static Angle calculateSplitLatitude(Angle lowerLatitude, Angle upperLatitude)
  {
    final double middleV = (getMercatorV(lowerLatitude) + getMercatorV(upperLatitude)) / 2;

    return toLatitude(middleV);
  }

  public static double longitudeToMeters(Angle longitude)
  {
      return longitude._degrees * _originShift / 180.0;
  }

  public static double latitudeToMeters(Angle latitude)
  {
    if (latitude._degrees >= _upperLimitInDegrees)
    {
      return 20037508.342789244;
    }
    if (latitude._degrees <= _lowerLimitInDegrees)
    {
      return -20037508.342789244;
    }
  
    final IMathUtils mu = IMathUtils.instance();
  
    double my = mu.log(mu.tan((90 + latitude._degrees) * DefineConstants.PI / 360.0)) / (DefineConstants.PI / 180.0);
    my = my * _originShift / 180.0;
  
    return my;
  }

  public static Sector getSector(int z, int x, int y)
  {
    return new Sector(new Geodetic2D(yToLatitude(y, z), xToLongitude(x, z)), new Geodetic2D(yToLatitude(y+1, z), xToLongitude(x+1, z)));
  }

  public static Angle xToLongitude(int x, int z)
  {
    return Angle.fromDegrees(x / IMathUtils.instance().pow(2.0, z) * 360.0 - 180);
  }

  public static Angle yToLatitude(int y, int z)
  {
    final IMathUtils mu = IMathUtils.instance();
    double n = DefineConstants.PI - (2.0 * DefineConstants.PI * y) / mu.pow(2.0, z);
    return Angle.fromRadians(mu.atan(mu.sinh(n)));
  }

}