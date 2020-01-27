//
//  IDeviceInfo.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/8/13.
//
//

#include "IDeviceInfo.hpp"

float IDeviceInfo::getPixelsInMM(float millimeters) const {
  return getDPI() / 25.4f * millimeters;
}
