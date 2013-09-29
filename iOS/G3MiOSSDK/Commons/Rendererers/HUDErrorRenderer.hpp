//
//  HUDErrorRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/28/13.
//
//

#ifndef __G3MiOSSDK__HUDErrorRenderer__
#define __G3MiOSSDK__HUDErrorRenderer__

#include "ErrorRenderer.hpp"
#include "HUDImageRenderer.hpp"
#include "ImageFactory.hpp"


class HUDErrorRenderer : public HUDImageRenderer, ErrorRenderer, ImageFactory {
public:
  HUDErrorRenderer() :
  HUDImageRenderer(this)
  {

  }
};

#endif
