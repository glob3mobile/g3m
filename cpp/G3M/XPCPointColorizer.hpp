//
//  XPCPointColorizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/15/21.
//


#ifndef XPCPointColorizer_hpp
#define XPCPointColorizer_hpp

#include "Color.hpp"

class XPCMetadata;
class IIntBuffer;
class IByteBuffer;


class XPCPointColorizer {
public:
  virtual ~XPCPointColorizer() {
  }

  virtual IIntBuffer* initialize(const XPCMetadata* metadata) = 0;

  virtual Color colorize(const XPCMetadata* metadata,
                         const std::vector<const IByteBuffer*>* dimensionsValues,
                         const size_t i) = 0;

};

#endif
