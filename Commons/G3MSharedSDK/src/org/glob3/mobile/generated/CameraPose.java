package org.glob3.mobile.generated;
//
//  CameraPose.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 9/29/26.
//

//
//  CameraPose.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 9/29/26.
//




// Where a camera stands and where it looks, in the terms G3MWidget::setAnimatedCameraPosition takes
public class CameraPose
{
  public final Geodetic3D _position ;
  public final Angle _heading ;
  public final Angle _pitch ;

  public static CameraPose nan()
  {
    return new CameraPose(Geodetic3D.nan(), Angle.nan(), Angle.nan());
  }

  public CameraPose(Geodetic3D position, Angle heading, Angle pitch)
  {
     _position = new Geodetic3D(position);
     _heading = new Angle(heading);
     _pitch = new Angle(pitch);
  }

  public CameraPose(CameraPose that)
  {
     _position = new Geodetic3D(that._position);
     _heading = new Angle(that._heading);
     _pitch = new Angle(that._pitch);
  }

  public void dispose()
  {
  }

  public final boolean isNan()
  {
    return _position.isNan() || _heading.isNan() || _pitch.isNan();
  }

}