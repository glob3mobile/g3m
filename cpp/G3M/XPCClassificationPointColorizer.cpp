//
//  XPCClassificationPointColorizer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/21/21.
//

#include "XPCClassificationPointColorizer.hpp"

#include "ILogger.hpp"

#include "XPCMetadata.hpp"
#include "XPCDimension.hpp"
#include "IFactory.hpp"
#include "IIntBuffer.hpp"
#include "IByteBuffer.hpp"
#include "IMathUtils.hpp"
#include "MutableColor.hpp"


void XPCClassificationPointColorizer::initializeColors(std::vector<const Color>& colors,
                                                       const float alpha) {
  const int alpha255 = IMathUtils::instance()->round( alpha * 255.0f );

  colors.push_back( Color::fromRGBA255(215,  30,  30, alpha255) );   //  0 - Never Classified
  colors.push_back( Color::fromRGBA255(219,  61,  61, alpha255) );   //  1 - Unclassified
  colors.push_back( Color::fromRGBA255(255,   0, 255, alpha255) );   //  2 -  Ground
  colors.push_back( Color::fromRGBA255(  0, 143,   0, alpha255) );   //  3 - Low Vegetation
  colors.push_back( Color::fromRGBA255(  0, 191,   0, alpha255) );   //  4 - Medium Vegetation
  colors.push_back( Color::fromRGBA255(  0, 255,   0, alpha255) );   //  5 - High Vegetation
  colors.push_back( Color::fromRGBA255(255,   0,   0, alpha255) );   //  6 - Building
  colors.push_back( Color::fromRGBA255(207,  44,  44, alpha255) );   //  7 - Low Point / Noise
  colors.push_back( Color::fromRGBA255(255, 255,   0, alpha255) );   //  8 - Key Point / Reserved
  colors.push_back( Color::fromRGBA255(  0,   0, 255, alpha255) );   //  9 - Water
  colors.push_back( Color::fromRGBA255(  0, 224, 224, alpha255) );   // 10 - Rail
  colors.push_back( Color::fromRGBA255(224,   0, 224, alpha255) );   // 11 - Road Surface
  colors.push_back( Color::fromRGBA255( 66, 249,  63, alpha255) );   // 12 - Reserved
  colors.push_back( Color::fromRGBA255(189, 158,   3, alpha255) );   // 13 - Wire Guard / Shield
  colors.push_back( Color::fromRGBA255( 89, 214, 125, alpha255) );   // 14 - Wire Conductor / Phase
  colors.push_back( Color::fromRGBA255(147, 163, 120, alpha255) );   // 15 - Transmission Tower
  colors.push_back( Color::fromRGBA255(123,  50,  42, alpha255) );   // 16 - Wire Structure Connection
  colors.push_back( Color::fromRGBA255( 36, 120,  60, alpha255) );   // 17 - Bridge Deck
  colors.push_back( Color::fromRGBA255( 87,  38,  65, alpha255) );   // 18 - High Noise
}

XPCClassificationPointColorizer::XPCClassificationPointColorizer(const float alpha) :
XPCFixedAlphaPointColorizer(alpha),
_classificationDimensionName("Classification"),
_classificationDimensionIndex(-1),
_ok(false)
{
  initializeColors(_colors, _alpha);
}

XPCClassificationPointColorizer::XPCClassificationPointColorizer(const std::string& classificationDimensionName,
                                                                 const float alpha) :
XPCFixedAlphaPointColorizer(alpha),
_classificationDimensionName(classificationDimensionName),
_classificationDimensionIndex(-1),
_ok(false)
{
  initializeColors(_colors, _alpha);
}

XPCClassificationPointColorizer::~XPCClassificationPointColorizer() {
  
}

IIntBuffer* XPCClassificationPointColorizer::initialize(const XPCMetadata* metadata) {
  const size_t dimensionsCount = metadata->getDimensionsCount();
  
  for (int i = 0; i < dimensionsCount; i++) {
    const std::string dimensionName = metadata->getDimension(i)->_name;
    
    if (dimensionName == _classificationDimensionName) {
      _classificationDimensionIndex = i;
    }
  }
  
  _ok = (_classificationDimensionIndex >= 0);
  
  if (!_ok) {
    ILogger::instance()->logError("Can't find Classification dimensions");
    return NULL;
  }
  
  IIntBuffer* requiredDimensionIndices = IFactory::instance()->createIntBuffer(1);
  requiredDimensionIndices->put(0, _classificationDimensionIndex);
  return requiredDimensionIndices;
}

void XPCClassificationPointColorizer::colorize(const XPCMetadata* metadata,
                                               const double heights[],
                                               const std::vector<const IByteBuffer*>* dimensionsValues,
                                               const size_t i,
                                               MutableColor& color) {
  if (!_ok) {
    color.set(1, 0, 0, 1);
    return;
  }

  const unsigned char classification = dimensionsValues->at(0)->get(i);

  if (classification >= _colors.size()) {
    color.set(1, 0, 0, 1);
    return;
  }

  color.set( _colors[classification] );
}
