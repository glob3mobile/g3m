//
//  XPCClassificationPointColorizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/21/21.
//

#ifndef XPCClassificationPointColorizer_hpp
#define XPCClassificationPointColorizer_hpp

#include "XPCFixedAlphaPointColorizer.hpp"

#include <string>
#include <vector>

#include "Color.hpp"


class XPCClassificationPointColorizer : public XPCFixedAlphaPointColorizer {
private:

  const std::string _classificationDimensionName;

  int _classificationDimensionIndex;
  
  bool _ok;

  std::vector<const Color> _colors;

  static void initializeColors(std::vector<const Color>& colors,
                               const float alpha);

public:
  
  XPCClassificationPointColorizer(const float alpha);
  
  XPCClassificationPointColorizer(const std::string& classificationDimensionName,
                                  const float alpha);
  
  ~XPCClassificationPointColorizer();

  IIntBuffer* initialize(const XPCMetadata* metadata);
  
  void colorize(const XPCMetadata* metadata,
                const double heights[],
                const std::vector<const IByteBuffer*>* dimensionsValues,
                const size_t i,
                MutableColor& color);
  
};

#endif
