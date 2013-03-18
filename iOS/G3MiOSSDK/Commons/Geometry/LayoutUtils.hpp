//
//  LayoutUtils.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo on 18/03/13.
//
//

#ifndef G3MiOSSDK_LayoutUtils_h
#define G3MiOSSDK_LayoutUtils_h

#include <vector>

#include "Angle.hpp"

class Geodetic3D;
class Ellipsoid;


class LayoutUtils {
private:
  LayoutUtils() {}

public:
  static std::vector<Geodetic3D*> splitOverCircle(const Ellipsoid* ellipsoid,
                                                  const Geodetic3D& center,
                                                  double radiusInMeters,
                                                  int splits,
                                                  const Angle& startAngle = Angle::zero());

};


#endif
