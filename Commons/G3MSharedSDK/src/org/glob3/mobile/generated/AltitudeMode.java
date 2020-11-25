package org.glob3.mobile.generated;
//
//  AltitudeMode.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/7/16.
//
//

//
//  AltitudeMode.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/7/16.
//
//


//Altitude modes taken from KML standard (with the exception of relative to sea floor)
public enum AltitudeMode
{
  RELATIVE_TO_GROUND, // Relative to elevation provided by any SurfaceElevationProvider (typically PlanetRenderer)
  ABSOLUTE; // Relative to surface of geometrical planet definition (Ellipsoid, Sphere, Flat, ...)

   public int getValue()
   {
      return this.ordinal();
   }

   public static AltitudeMode forValue(int value)
   {
      return values()[value];
   }
}