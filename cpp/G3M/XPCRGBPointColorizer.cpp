//
//  XPCRGBPointColorizer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/20/21.
//

#include "XPCRGBPointColorizer.hpp"

#include "ILogger.hpp"

#include "XPCMetadata.hpp"
#include "XPCDimension.hpp"
#include "IFactory.hpp"
#include "IIntBuffer.hpp"


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

IIntBuffer* XPCRGBPointColorizer::initialize(const XPCMetadata* metadata) {
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

  if (!_ok) {
    ILogger::instance()->logError("Can't find Red, Green and Blue dimensions");
    return NULL;
  }

  IIntBuffer* requiredDimensionIndices = IFactory::instance()->createIntBuffer(3);
  requiredDimensionIndices->put(0, _redDimensionIndex);
  requiredDimensionIndices->put(1, _greenDimensionIndex);
  requiredDimensionIndices->put(2, _blueDimensionIndex);

  return requiredDimensionIndices;
}

Color XPCRGBPointColorizer::colorize(const XPCMetadata* metadata,
                                     const std::vector<const IByteBuffer*>* dimensionsValues,
                                     const size_t i) {
  if (!_ok) {
    return Color::RED;
  }

//  redValue = dimensionsValues->at(0)->;

  const float red   = metadata->getDimension(_redDimensionIndex  )->getNormalizedValue( dimensionsValues->at(0), i );
  const float green = metadata->getDimension(_greenDimensionIndex)->getNormalizedValue( dimensionsValues->at(1), i );
  const float blue  = metadata->getDimension(_blueDimensionIndex )->getNormalizedValue( dimensionsValues->at(2), i );

#warning ______DIEGO AT WORK
  return Color::fromRGBA(red, green, blue, 1);
}