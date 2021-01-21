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
class XPCPoint;
class IIntBuffer;


class XPCPointColorizer {
public:
  virtual ~XPCPointColorizer() {
  }

  virtual IIntBuffer* initialize(const XPCMetadata* metadata) = 0;

  virtual Color colorize(const XPCMetadata* metadata,
                         const XPCPoint* point) = 0;
};

#endif
