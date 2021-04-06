//
//  XPCRGBPointColorizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/20/21.
//

#ifndef XPCRGBPointColorizer_hpp
#define XPCRGBPointColorizer_hpp

#include "XPCFixedAlphaPointColorizer.hpp"

#include <string>


class XPCRGBPointColorizer : public XPCFixedAlphaPointColorizer {
private:
  const std::string _redDimensionName;
  const std::string _greenDimensionName;
  const std::string _blueDimensionName;

  int _redDimensionIndex;
  int _greenDimensionIndex;
  int _blueDimensionIndex;
  bool _ok;

public:

  XPCRGBPointColorizer(const float alpha);

  XPCRGBPointColorizer(const std::string& redDimensionName,
                       const std::string& greenDimensionName,
                       const std::string& blueDimensionName,
                       const float alpha);

  ~XPCRGBPointColorizer();

  IIntBuffer* initialize(const XPCMetadata* metadata);

  void colorize(const XPCMetadata* metadata,
                const double heights[],
                const std::vector<const IByteBuffer*>* dimensionsValues,
                const size_t i,
                MutableColor& color);

};

#endif
