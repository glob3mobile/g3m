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
    final double pi4 = mu.pi() * 4;

    final double latSin = latitude.sinus();
    return 1.0 - ((mu.log((1.0 + latSin) / (1.0 - latSin)) / pi4) + 0.5);
  }

  public static Angle toLatitude(double v)
  {
    final IMathUtils mu = IMathUtils.instance();
    final double pi = mu.pi();

    final double exp = mu.exp(-2 * pi * (1.0 - v - 0.5));
    final double atan = mu.atan(exp);
    return Angle.fromRadians((pi / 2) - 2 * atan);
  }

  public static Angle calculateSplitLatitude(Angle lowerLatitude, Angle upperLatitude)
  {
    final double middleV = (getMercatorV(lowerLatitude) + getMercatorV(upperLatitude)) / 2;

    return toLatitude(middleV);
  }

}