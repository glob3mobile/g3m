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

const Color XPCClassificationPointColorizer::COLORS[] = {
  Color::fromRGBA255(215,  30,  30),    //  0 - Never Classified
  Color::fromRGBA255(219,  61,  61),    //  1 - Unclassified
  Color::fromRGBA255(255,   0, 255),    //  2 -  Ground
  Color::fromRGBA255(  0, 143,   0),    //  3 - Low Vegetation
  Color::fromRGBA255(  0, 191,   0),    //  4 - Medium Vegetation
  Color::fromRGBA255(  0, 255,   0),    //  5 - High Vegetation
  Color::fromRGBA255(255,   0,   0),    //  6 - Building
  Color::fromRGBA255(207,  44,  44),    //  7 - Low Point / Noise
  Color::fromRGBA255(255, 255,   0),    //  8 - Key Point / Reserved
  Color::fromRGBA255(  0,   0, 255),    //  9 - Water
  Color::fromRGBA255(  0, 224, 224),    // 10 - Rail
  Color::fromRGBA255(224,   0, 224),    // 11 - Road Surface
  Color::fromRGBA255( 66, 249,  63),    // 12 - Reserved
  Color::fromRGBA255(189, 158,   3),    // 13 - Wire Guard / Shield
  Color::fromRGBA255( 89, 214, 125),    // 14 - Wire Conductor / Phase
  Color::fromRGBA255(147, 163, 120),    // 15 - Transmission Tower
  Color::fromRGBA255(123,  50,  42),    // 16 - Wire Structure Connection
  Color::fromRGBA255( 36, 120,  60),    // 17 - Bridge Deck
  Color::fromRGBA255( 87,  38,  65)     // 18 - High Noise
};


XPCClassificationPointColorizer::XPCClassificationPointColorizer() :
_classificationDimensionName("Classification"),
_classificationDimensionIndex(-1),
_ok(false)
{
  
}

XPCClassificationPointColorizer::XPCClassificationPointColorizer(const std::string& classificationDimensionName) :
_classificationDimensionName(classificationDimensionName),
_classificationDimensionIndex(-1),
_ok(false)
{
  
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

Color XPCClassificationPointColorizer::colorize(const XPCMetadata* metadata,
                                                const std::vector<const IByteBuffer*>* dimensionsValues,
                                                const size_t i) {
  if (!_ok) {
    return Color::RED;
  }

  unsigned char classification = dimensionsValues->at(0)->get(i);
  return (classification > 18) ? Color::RED : COLORS[classification];
}
