//
//  XPCFixedAlphaPointColorizer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 2/10/21.
//

#include "XPCFixedAlphaPointColorizer.hpp"


XPCFixedAlphaPointColorizer::XPCFixedAlphaPointColorizer(const float alpha) :
_alpha(alpha)
{

}

XPCFixedAlphaPointColorizer::~XPCFixedAlphaPointColorizer() {
#ifdef JAVA_CODE
  super.dispose();
#endif
}

float XPCFixedAlphaPointColorizer::getAlpha() const {
  return _alpha;
}
