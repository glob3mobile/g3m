//
//  HUDAbsoluteSize.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/26/13.
//
//

#ifndef __G3MiOSSDK__HUDAbsoluteSize__
#define __G3MiOSSDK__HUDAbsoluteSize__

#include "HUDSize.hpp"

class HUDAbsoluteSize : public HUDSize {
private:
  const float _size;

public:
  HUDAbsoluteSize(float size) :
  _size(size)
  {
  }

  float getSize(int viewPortWidth,
                int viewPortHeight,
                int bitmapWidth,
                int bitmapHeight) const {
    return _size;
  }

};

#endif
