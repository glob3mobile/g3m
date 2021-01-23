//
//  XPCIntensityPointColorizer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/21/21.
//

#include "XPCIntensityPointColorizer.hpp"

#include "ILogger.hpp"

#include "XPCMetadata.hpp"
#include "XPCDimension.hpp"
#include "IFactory.hpp"
#include "IIntBuffer.hpp"


XPCIntensityPointColorizer::XPCIntensityPointColorizer() :
_intensityDimensionName("Intensity"),
_intensityDimensionIndex(-1),
_ok(false)
{

}

XPCIntensityPointColorizer::XPCIntensityPointColorizer(const std::string& intensityDimensionName) :
_intensityDimensionName(intensityDimensionName),
_intensityDimensionIndex(-1),
_ok(false)
{

}

XPCIntensityPointColorizer::~XPCIntensityPointColorizer() {

}

IIntBuffer* XPCIntensityPointColorizer::initialize(const XPCMetadata* metadata) {
  const size_t dimensionsCount = metadata->getDimensionsCount();

  for (int i = 0; i < dimensionsCount; i++) {
    const std::string dimensionName = metadata->getDimension(i)->_name;

    if (dimensionName == _intensityDimensionName) {
      _intensityDimensionIndex = i;
    }
  }

  _ok = (_intensityDimensionIndex >= 0);

  if (!_ok) {
    ILogger::instance()->logError("Can't find Intensity dimensions");
    return NULL;
  }

  IIntBuffer* requiredDimensionIndices = IFactory::instance()->createIntBuffer(1);
  requiredDimensionIndices->put(0, _intensityDimensionIndex);
  return requiredDimensionIndices;
}

Color XPCIntensityPointColorizer::colorize(const XPCMetadata* metadata,
                                           const std::vector<const IByteBuffer*>* dimensionsValues,
                                           const size_t i) {
  if (!_ok) {
    return Color::RED;
  }

  const float intensity = metadata->getDimension(_intensityDimensionIndex  )->getNormalizedValue( dimensionsValues->at(0), i );

  return Color::fromRGBA(intensity, intensity, intensity, 1);
}
