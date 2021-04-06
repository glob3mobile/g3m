//
//  XPCFixedAlphaPointColorizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 2/10/21.
//

#ifndef XPCFixedAlphaPointColorizer_hpp
#define XPCFixedAlphaPointColorizer_hpp

#include "XPCPointColorizer.hpp"


class XPCFixedAlphaPointColorizer : public XPCPointColorizer {
protected:
  const float _alpha;


  XPCFixedAlphaPointColorizer(const float alpha);

  ~XPCFixedAlphaPointColorizer();

public:

  float getAlpha() const;

};

#endif
