package org.glob3.mobile.generated; 
//
//  SurfaceElevationProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 8/2/13.
//
//

//
//  SurfaceElevationProvider.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 8/2/13.
//
//




//class Angle;
//class Geodetic2D;



public interface SurfaceElevationListener
{

  void dispose();

  void elevationChanged(Geodetic2D position, double rawElevation, double verticalExaggeration); //Without considering vertical exaggeration

  void elevationChanged(Sector position, ElevationData rawElevationData, double verticalExaggeration); //Without considering vertical exaggeration
}