//
//  XPCHeightPointColorizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 2/10/21.
//

#ifndef XPCHeightPointColorizer_hpp
#define XPCHeightPointColorizer_hpp

#include "XPCPointColorizer.hpp"

class RampColorizer;


class XPCHeightPointColorizer : public XPCPointColorizer {
private:
  const RampColorizer* _ramp;
  const bool           _deleteRamp;
  const float          _alpha;

  double _minHeight;
  double _maxHeight;
  double _deltaHeight;


public:
  XPCHeightPointColorizer(const RampColorizer* ramp,
                          const bool deleteRamp,
                          const float alpha);

  ~XPCHeightPointColorizer();

  IIntBuffer* initialize(const XPCMetadata* metadata);

  void colorize(const XPCMetadata* metadata,
                const double heights[],
                const std::vector<const IByteBuffer*>* dimensionsValues,
                const size_t i,
                MutableColor& color);

};

#endif
