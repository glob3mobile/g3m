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
#include "MutableColor.hpp"
#include "Color.hpp"


XPCIntensityPointColorizer::XPCIntensityPointColorizer(const float alpha) :
_intensityDimensionName("Intensity"),
_alpha(alpha),
_intensityDimensionIndex(-1),
_ok(false)
{

}

XPCIntensityPointColorizer::XPCIntensityPointColorizer(const std::string& intensityDimensionName,
                                                       const float alpha) :
_intensityDimensionName(intensityDimensionName),
_alpha(alpha),
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

void XPCIntensityPointColorizer::colorize(const XPCMetadata* metadata,
                                          const double heights[],
                                          const std::vector<const IByteBuffer*>* dimensionsValues,
                                          const size_t i,
                                          MutableColor& color) {
  if (!_ok) {
    color.set(1, 0, 0, 1);
    return;
  }

  const float intensity = metadata->getDimension(_intensityDimensionIndex  )->getNormalizedValue( dimensionsValues->at(0), i );

  color.set(intensity, intensity, intensity, _alpha);
}
