//
//  XPCHeightPointColorizer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 2/10/21.
//

#include "XPCHeightPointColorizer.hpp"

#include "XPCMetadata.hpp"
#include "IMathUtils.hpp"
#include "RampColorizer.hpp"
#include "MutableColor.hpp"


XPCHeightPointColorizer::XPCHeightPointColorizer(const RampColorizer* ramp,
                                                 const bool deleteRamp,
                                                 const float alpha) :
_ramp(ramp),
_deleteRamp(deleteRamp),
_alpha(alpha),
_minHeight(NAND),
_maxHeight(NAND),
_deltaHeight(NAND)
{

}

XPCHeightPointColorizer::~XPCHeightPointColorizer() {
  if (_deleteRamp) {
    delete _ramp;
  }

#if JAVA_CODE
  super.dispose();
#endif
}

IIntBuffer* XPCHeightPointColorizer::initialize(const XPCMetadata* metadata) {
  _minHeight   = metadata->_minHeight;
  _maxHeight   = metadata->_maxHeight;
  _deltaHeight = _maxHeight - _minHeight;

  return NULL; // no dimension needed
}

void XPCHeightPointColorizer::colorize(const XPCMetadata* metadata,
                                       const double heights[],
                                       const std::vector<const IByteBuffer*>* dimensionsValues,
                                       const size_t i,
                                       MutableColor& color) {
  if (ISNAN(_minHeight) || (_ramp == NULL)) {
    color.set(1, 0, 0, 1);
    return;
  }

  const double height = heights[i];

  const double a = (height - _minHeight) / _deltaHeight;

  _ramp->getColor((float) a, color);
  color._alpha = _alpha;
}
