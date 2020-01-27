//
//  TaitBryanAngles.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 06/02/14.
//
//

#include "TaitBryanAngles.hpp"
#include "IStringBuilder.hpp"

TaitBryanAngles::TaitBryanAngles(const Angle& heading,
                                 const Angle& pitch,
                                 const Angle& roll) :
_heading(heading),
_pitch(pitch),
_roll(roll)
{
}

TaitBryanAngles TaitBryanAngles::fromRadians(double heading,
                                             double pitch,
                                             double roll) {
  return TaitBryanAngles(Angle::fromRadians(heading),
                         Angle::fromRadians(pitch),
                         Angle::fromRadians(roll));
}

TaitBryanAngles TaitBryanAngles::fromDegrees(double heading,
                                             double pitch,
                                             double roll) {
  return TaitBryanAngles(Angle::fromDegrees(heading),
                         Angle::fromDegrees(pitch),
                         Angle::fromDegrees(roll));
}

const std::string TaitBryanAngles::description() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("(heading= ");
  isb->addString(_heading.description());
  isb->addString(", pitch= ");
  isb->addString(_pitch.description());
  isb->addString(", roll= ");
  isb->addString(_roll.description());
  isb->addString(")");
  const std::string s = isb->getString();
  delete isb;
  return s;
}
