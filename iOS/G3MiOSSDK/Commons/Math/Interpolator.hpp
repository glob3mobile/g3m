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
private:
  Interpolator();

public:
  static double interpolateHeight(const Geodetic2D& lower,
                                  const Geodetic2D& upper,
                                  double valueSW,
                                  double valueSE,
                                  double valueNE,
                                  double valueNW,
                                  const Angle& latitude,
                                  const Angle& longitude);


  static double interpolateHeight(const Geodetic2D& lower,
                                  const Geodetic2D& upper,
                                  double valueSW,
                                  double valueSE,
                                  double valueNE,
                                  double valueNW,
                                  const Geodetic2D& position);
  
};

#endif
