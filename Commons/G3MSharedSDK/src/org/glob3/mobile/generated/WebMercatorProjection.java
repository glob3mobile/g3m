package org.glob3.mobile.generated;
//
//  WebMercatorProjection.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/14/16.
//
//

//
//  WebMercatorProjection.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/14/16.
//
//



public class WebMercatorProjection extends Projection
{
  private static WebMercatorProjection INSTANCE = null;

  private static final double _upperLimitRadians = Angle.fromDegrees(85.0511287798)._radians;
  private static final double _lowerLimitRadians = Angle.fromDegrees(-85.0511287798)._radians;

  //  static const double _upperLimitDegrees;
  //  static const double _lowerLimitDegrees;


  //const double WebMercatorProjection::_upperLimitDegrees = 85.0511287798;
  //const double WebMercatorProjection::_lowerLimitDegrees = -85.0511287798;
  
  
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
    final double latSin = java.lang.Math.sin(latitudeRadians);
    return 0.5 - (mu.log((1.0 + latSin) / (1.0 - latSin)) / pi4);
  }

}
