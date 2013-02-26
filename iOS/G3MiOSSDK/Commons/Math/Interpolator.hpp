//
//  Interpolator.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/20/13.
//
//

#ifndef __G3MiOSSDK__Interpolator__
#define __G3MiOSSDK__Interpolator__

class Angle;
class Geodetic2D;

class Interpolator {
protected:
  Interpolator() {

  }

public:

  virtual ~Interpolator() {

  }

  virtual double interpolate(const Geodetic2D& sw,
                             const Geodetic2D& ne,
                             double valueSW,
                             double valueSE,
                             double valueNE,
                             double valueNW,
                             const Geodetic2D& position);

  virtual double interpolate(const Geodetic2D& sw,
                             const Geodetic2D& ne,
                             double valueSW,
                             double valueSE,
                             double valueNE,
                             double valueNW,
                             const Angle& latitude,
                             const Angle& longitude);

  virtual double interpolate(double valueSW,
                             double valueSE,
                             double valueNE,
                             double valueNW,
                             double u,
                             double v) = 0;
  
};

#endif
