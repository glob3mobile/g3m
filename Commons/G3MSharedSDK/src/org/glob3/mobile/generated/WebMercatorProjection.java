package org.glob3.mobile.generated;
//
//  WebMercatorProjection.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/14/16.
//
//

//
//  WebMercatorProjection.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/14/16.
//
//



public class WebMercatorProjection extends Projection
{
  private static WebMercatorProjection INSTANCE = null;

  private static final double _upperLimitRadians = Angle.fromDegrees(85.0511287798)._radians;
  private static final double _lowerLimitRadians = Angle.fromDegrees(-85.0511287798)._radians;

  private WebMercatorProjection()
  {
  }

  public void dispose()
  {
    super.dispose();
  }


  public static WebMercatorProjection instance()
  {
    if (INSTANCE == null)
    {
      INSTANCE = new WebMercatorProjection();
    }
    return INSTANCE;
  }

  public final String getEPSG()
  {
    return "EPSG:3857";
  }

  public final double getU(Angle longitude)
  {
    return (longitude._radians + DefineConstants.PI) / (DefineConstants.PI *2);
  }
  public final double getV(Angle latitude)
  {
    final double latitudeRadians = latitude._radians;
  
    if (latitudeRadians >= _upperLimitRadians)
    {
      return 0;
    }
    if (latitudeRadians <= _lowerLimitRadians)
    {
      return 1;
    }
  
    final IMathUtils mu = IMathUtils.instance();
    final double pi4 = DefineConstants.PI * 4;
    final double latSin = Math.sin(latitudeRadians);
    return 0.5 - (mu.log((1.0 + latSin) / (1.0 - latSin)) / pi4);
  }

  public final Angle getInnerPointLongitude(double u)
  {
    return Angle.fromRadians(IMathUtils.instance().linearInterpolation(-DefineConstants.PI, DefineConstants.PI, u));
  }
  public final Angle getInnerPointLatitude(double v)
  {
    final IMathUtils mu = IMathUtils.instance();
  
    final double exp = mu.exp(-2 * DefineConstants.PI * (1.0 - v - 0.5));
    final double atan = mu.atan(exp);
    return Angle.fromRadians((DefineConstants.PI / 2) - 2 * atan);
  }

  public final Angle getInnerPointLongitude(Sector sector, double u)
  {
    return Angle.linearInterpolation(sector._lower._longitude, sector._upper._longitude, u);
  }
  public final Angle getInnerPointLatitude(Sector sector, double v)
  {
    final double lowerV = getU(sector._lower._latitude);
    final double upperV = getU(sector._upper._latitude);
    final double sV = lowerV + ((upperV - lowerV) * v);
  
    return getInnerPointLatitude(sV);
  }

}
