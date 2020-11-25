package org.glob3.mobile.generated;
//
//  WGS84Projetion.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/14/16.
//
//

//
//  WGS84Projetion.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/14/16.
//
//



public class WGS84Projetion extends Projection
{
  private static WGS84Projetion INSTANCE = null;

  private WGS84Projetion()
  {
  }

  public void dispose()
  {
    super.dispose();
  }


  public static WGS84Projetion instance()
  {
    if (INSTANCE == null)
    {
      INSTANCE = new WGS84Projetion();
    }
    return INSTANCE;
  }

  public final String getEPSG()
  {
    return "EPSG:4326";
  }

  public final double getU(Angle longitude)
  {
    return (longitude._radians + DefineConstants.PI) / (DefineConstants.PI *2);
  }
  public final double getV(Angle latitude)
  {
    return (DefineConstants.HALF_PI - latitude._radians) / DefineConstants.PI;
  }

  public final Angle getInnerPointLongitude(double u)
  {
    return Angle.fromRadians(IMathUtils.instance().linearInterpolation(-DefineConstants.PI, DefineConstants.PI, u));
  }
  public final Angle getInnerPointLatitude(double v)
  {
    return Angle.fromRadians(IMathUtils.instance().linearInterpolation(-DefineConstants.HALF_PI, DefineConstants.HALF_PI, 1.0 - v));
  }

  public final Angle getInnerPointLongitude(Sector sector, double u)
  {
    return Angle.linearInterpolation(sector._lower._longitude, sector._upper._longitude, u);
  }
  public final Angle getInnerPointLatitude(Sector sector, double v)
  {
    return Angle.linearInterpolation(sector._lower._latitude, sector._upper._latitude, 1.0 - v);
  }

}