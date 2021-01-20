//
//  XPCRGBPointColorizer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/20/21.
//

#include "XPCRGBPointColorizer.hpp"

#include "XPCMetadata.hpp"
#include "XPCDimension.hpp"


XPCRGBPointColorizer::XPCRGBPointColorizer() :
_redDimensionName("Red"),
_greenDimensionName("Green"),
_blueDimensionName("Blue"),
_redDimensionIndex(-1),
_greenDimensionIndex(-1),
_blueDimensionIndex(-1),
_ok(false)
{

}

XPCRGBPointColorizer::XPCRGBPointColorizer(const std::string& redDimensionName,
                                           const std::string& greenDimensionName,
                                           const std::string& blueDimensionName) :
_redDimensionName(redDimensionName),
_greenDimensionName(greenDimensionName),
_blueDimensionName(blueDimensionName),
_redDimensionIndex(-1),
_greenDimensionIndex(-1),
_blueDimensionIndex(-1),
_ok(false)
{

}

XPCRGBPointColorizer::~XPCRGBPointColorizer() {

}

void XPCRGBPointColorizer::initialize(const XPCMetadata* metadata) {
  const size_t dimensionsCount = metadata->getDimensionsCount();

  for (int i = 0; i < dimensionsCount; i++) {
    const std::string dimensionName = metadata->getDimension(i)->_name;

    if (dimensionName == _redDimensionName) {
      _redDimensionIndex = i;
    }
    else if (dimensionName == _greenDimensionName) {
      _greenDimensionIndex = i;
    }
    else if (dimensionName == _blueDimensionName) {
      _blueDimensionIndex = i;
    }
  }

  _ok = (_redDimensionIndex >= 0) && (_greenDimensionIndex >= 0) && (_blueDimensionIndex >= 0);
}

Color XPCRGBPointColorizer::colorize(const XPCMetadata* metadata,
                                     const XPCPoint* point) {
  if (!_ok) {
    return Color::RED;
  }

#warning DIEGO AT WORK
  return Color::YELLOW;
}
