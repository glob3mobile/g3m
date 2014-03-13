package org.glob3.mobile.generated; 
//
//  GEORasterProjection.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/11/13.
//
//

//
//  GEORasterProjection.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/11/13.
//
//


//class Geodetic2D;

public class GEORasterProjection
{
  private final Sector _sector ;
  private final boolean _mercator;
  private final int _imageWidth;
  private final int _imageHeight;

  private double _mercatorUpperGlobalV;
  private double _mercatorDeltaGlobalV;


  public GEORasterProjection(Sector sector, boolean mercator, int imageWidth, int imageHeight)
  {
     _sector = new Sector(sector);
     _mercator = mercator;
     _imageWidth = imageWidth;
     _imageHeight = imageHeight;
    if (_mercator)
    {
      final double mercatorLowerGlobalV = MercatorUtils.getMercatorV(sector._lower._latitude);
      _mercatorUpperGlobalV = MercatorUtils.getMercatorV(sector._upper._latitude);
      _mercatorDeltaGlobalV = mercatorLowerGlobalV - _mercatorUpperGlobalV;
    }
  }

  public void dispose()
  {

  }

  public final Vector2F project(Geodetic2D position)
  {
    final Vector2D uvCoordinates = _sector.getUVCoordinates(position);
  
    double v;
    if (_mercator)
    {
      final double linearV = uvCoordinates._y;
      final Angle latitude = _sector.getInnerPointLatitude(linearV);
      final double mercatorGlobalV = MercatorUtils.getMercatorV(latitude);
      final double mercatorLocalV = (mercatorGlobalV - _mercatorUpperGlobalV) / _mercatorDeltaGlobalV;
      v = mercatorLocalV;
    }
    else
    {
      v = uvCoordinates._y;
    }
  
    return new Vector2F((float)(uvCoordinates._x * _imageWidth), (float)(v * _imageHeight));
  
  }

}