//
//  XPCPointColorizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/15/21.
//


#ifndef XPCPointColorizer_hpp
#define XPCPointColorizer_hpp

//#include "Color.hpp"

#include <vector>

class XPCMetadata;
class IIntBuffer;
class IByteBuffer;
class MutableColor;


class XPCPointColorizer {
public:
  virtual ~XPCPointColorizer() {
  }

  virtual IIntBuffer* initialize(const XPCMetadata* metadata) = 0;

  virtual void colorize(const XPCMetadata* metadata,
                        const double heights[],
                        const std::vector<const IByteBuffer*>* dimensionsValues,
                        const size_t i,
                        MutableColor& color) = 0;

};

#endif
