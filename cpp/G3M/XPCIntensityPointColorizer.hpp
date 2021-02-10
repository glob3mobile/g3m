//
//  XPCIntensityPointColorizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/21/21.
//

#ifndef XPCIntensityPointColorizer_hpp
#define XPCIntensityPointColorizer_hpp

#include "XPCPointColorizer.hpp"

#include <string>


class XPCIntensityPointColorizer : public XPCPointColorizer {
private:
  const std::string _intensityDimensionName;
  const float       _alpha;

  int _intensityDimensionIndex;

  bool _ok;

public:

  XPCIntensityPointColorizer(const float alpha);

  XPCIntensityPointColorizer(const std::string& intensityDimensionName,
                             const float alpha);

  ~XPCIntensityPointColorizer();

  IIntBuffer* initialize(const XPCMetadata* metadata);

  void colorize(const XPCMetadata* metadata,
                const double heights[],
                const std::vector<const IByteBuffer*>* dimensionsValues,
                const size_t i,
                MutableColor& color);

};

#endif
