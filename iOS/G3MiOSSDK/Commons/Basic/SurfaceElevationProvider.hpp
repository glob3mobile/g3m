//
//  SurfaceElevationProvider.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 8/2/13.
//
//

#ifndef __G3MiOSSDK__SurfaceElevationProvider__
#define __G3MiOSSDK__SurfaceElevationProvider__

class Angle;
class Geodetic2D;


class SurfaceElevationListener {
public:
  virtual ~SurfaceElevationListener() {
#ifdef JAVA_CODE
  super.dispose();
#endif

  }
};


class SurfaceElevationProvider {
public:
#ifdef C_CODE
  virtual ~SurfaceElevationProvider() { }
#endif
#ifdef JAVA_CODE
  public void dispose();
#endif

  virtual void addListener(const Angle& latitude,
                           const Angle& longitude,
                           SurfaceElevationListener* observer) = 0;

  virtual void addListener(const Geodetic2D& position,
                           SurfaceElevationListener* observer) = 0;

  virtual void removeListener(SurfaceElevationListener* observer) = 0;

};

#endif
