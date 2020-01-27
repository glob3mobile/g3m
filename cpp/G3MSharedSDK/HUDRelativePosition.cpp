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
    case RIGHT:
      return position - widgetWidth - _margin;
    case LEFT:
      return position + _margin;
    case CENTER:
      return position - (widgetWidth / 2);

    case TOP:
      return viewPortHeight - (position + widgetHeight + _margin);
    case BOTTOM:
      return viewPortHeight - (position + _margin);
    case MIDDLE:
      return position - (widgetHeight / 2);
  }

  return position;
}
