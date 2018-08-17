//
//  HUDRelativePosition.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/20/13.
//
//

#include "HUDRelativePosition.hpp"


float HUDRelativePosition::getPosition(int viewPortWidth,
                                       int viewPortHeight,
                                       float widgetWidth,
                                       float widgetHeight) const {

  const float position = _factor * ( (_relativeTo == VIEWPORT_WIDTH) ? viewPortWidth : viewPortHeight );

  switch (_align) {
    case LEFT:
      return position - widgetWidth - _margin;
    case RIGHT:
      return position + _margin;
    case CENTER:
      return position - (widgetWidth / 2) - _margin;

    case ABOVE:
      return position + _margin;
    case BELOW:
      return position - widgetHeight - _margin;
    case MIDDLE:
      return position - (widgetHeight / 2) - _margin;
  }

  return position;
}
