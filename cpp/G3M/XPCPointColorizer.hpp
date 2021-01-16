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
class PCPoint;


class XPCPointColorizer {
public:
  virtual ~XPCPointColorizer() {
  }

  virtual Color colorize(const XPCMetadata* metadata,
                         const PCPoint* point) = 0;
};


#endif
