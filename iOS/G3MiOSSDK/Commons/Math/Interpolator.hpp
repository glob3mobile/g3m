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

#include <string>


class Interpolator {
protected:
  Interpolator() {

  }

public:

  virtual ~Interpolator() {
  }

  virtual double interpolation(const Geodetic2D& sw,
                               const Geodetic2D& ne,
                               double valueSW,
                               double valueSE,
                               double valueNE,
                               double valueNW,
                               const Geodetic2D& position) const;

  virtual double interpolation(const Geodetic2D& sw,
                               const Geodetic2D& ne,
                               double valueSW,
                               double valueSE,
                               double valueNE,
                               double valueNW,
                               const Angle& latitude,
                               const Angle& longitude) const;

  virtual double interpolation(double valueSW,
                               double valueSE,
                               double valueNE,
                               double valueNW,
                               double u,
                               double v) const = 0;

  virtual const std::string description() const = 0;

#ifdef JAVA_CODE
  @Override
  public String toString() {
    return description();
  }
#endif
  
};

#endif
