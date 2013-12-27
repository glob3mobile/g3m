//
//  HUDRelativeSize.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/26/13.
//
//

#ifndef __G3MiOSSDK__HUDRelativeSize__
#define __G3MiOSSDK__HUDRelativeSize__

#include "HUDSize.hpp"

class HUDRelativeSize : public HUDSize {
public:
  enum Reference {
    VIEWPORT_WIDTH,
    VIEWPORT_HEIGTH,
    VIEWPORT_MIN_AXIS,
    VIEWPORT_MAX_AXIS,
    BITMAP_WIDTH,
    BITMAP_HEIGTH,
    BITMAP_MIN_AXIS,
    BITMAP_MAX_AXIS
  };

private:
  const float                      _factor;
  const HUDRelativeSize::Reference _relativeTo;

public:
  HUDRelativeSize(float                            factor,
                  const HUDRelativeSize::Reference relativeTo) :
  _factor(factor),
  _relativeTo(relativeTo)
  {
  }


  float getSize(int viewPortWidth,
                int viewPortHeight,
                int bitmapWidth,
                int bitmapHeight) const;

};

#endif
