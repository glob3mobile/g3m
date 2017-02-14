//
//  AltitudeMode.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/7/16.
//
//

#ifndef AltitudeMode_hpp
#define AltitudeMode_hpp

//Altitude modes taken from KML standard (with the exception of relative to sea floor)
enum AltitudeMode {
  RELATIVE_TO_GROUND, // Relative to elevation provided by any SurfaceElevationProvider (typically PlanetRenderer)
  ABSOLUTE            // Relative to surface of geometrical planet definition (Ellipsoid, Sphere, Flat, ...)
};

#endif
