package org.glob3.mobile.generated;
//
//  WGS84Projetion.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/14/16.
//
//

//
//  WGS84Projetion.hpp
//  G3MiOSSDK
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

}
