//
//  HUDRelativeSize.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/26/13.
//
//

#include "HUDRelativeSize.hpp"

float HUDRelativeSize::getSize(int viewPortWidth,
                               int viewPortHeight,
                               int bitmapWidth,
                               int bitmapHeight) const {
  switch (_relativeTo) {
    case VIEWPORT_WIDTH:
      return _factor * viewPortWidth;

    case VIEWPORT_HEIGTH:
      return _factor * viewPortHeight;

    case VIEWPORT_MIN_AXIS:
      return _factor * ((viewPortWidth < viewPortHeight) ? viewPortWidth : viewPortHeight);

    case VIEWPORT_MAX_AXIS:
      return _factor * ((viewPortWidth > viewPortHeight) ? viewPortWidth : viewPortHeight);


    case BITMAP_WIDTH:
      return _factor * bitmapWidth;

    case BITMAP_HEIGTH:
      return _factor * bitmapHeight;

    case BITMAP_MIN_AXIS:
      return _factor * ((bitmapWidth < bitmapHeight) ? bitmapWidth : bitmapHeight);

    case BITMAP_MAX_AXIS:
      return _factor * ((bitmapWidth > bitmapHeight) ? bitmapWidth : bitmapHeight);
  }
  
  return 0;
}
