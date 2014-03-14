//
//  LayoutUtils.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo on 18/03/13.
//
//

#ifndef G3MiOSSDK_LayoutUtils
#define G3MiOSSDK_LayoutUtils

#include <vector>

#include "Angle.hpp"

class Geodetic3D;
class Geodetic2D;
class EllipsoidalPlanet;


class LayoutUtils {
private:
  LayoutUtils() {}

public:
  static std::vector<Geodetic3D*> splitOverCircle(const EllipsoidalPlanet* EllipsoidalPlanet,
                                                  const Geodetic3D& center,
                                                  double radiusInMeters,
                                                  int splits,
                                                  const Angle& startAngle = Angle::zero());
  static std::vector<Geodetic2D*> splitOverCircle(const EllipsoidalPlanet* EllipsoidalPlanet,
                                                  const Geodetic2D& center,
                                                  double radiusInMeters,
                                                  int splits,
                                                  const Angle& startAngle = Angle::zero());
};


#endif
