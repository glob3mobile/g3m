//
//  TaitBryanAngles.hpp
//  G3M
//
//  Created by Jose Miguel SN on 06/02/14.
//
//

#ifndef __G3M__TaitBryanAngles__
#define __G3M__TaitBryanAngles__

#include "Angle.hpp"

class TaitBryanAngles {
public:
  const Angle _heading;
  const Angle _pitch;
  const Angle _roll;

  TaitBryanAngles(const Angle& heading,
                  const Angle& pitch,
                  const Angle& roll);

  static TaitBryanAngles fromRadians(double heading,
                                     double pitch,
                                     double roll);

  static TaitBryanAngles fromDegrees(double heading,
                                     double pitch,
                                     double roll);

  const std::string description() const;

};

#endif
