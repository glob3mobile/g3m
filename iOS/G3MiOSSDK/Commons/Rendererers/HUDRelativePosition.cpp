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

  float position;
  switch (_relativeTo) {
    case VIEWPORT_WIDTH:
      position = viewPortWidth * _factor;
      break;
    case VIEWPORT_HEIGTH:
      position = viewPortHeight * _factor;
      break;
  }

  switch (_align) {
    case LEFT:
      position = position - widgetWidth - _margin;
      break;
    case RIGHT:
      position = position + _margin;
      break;
    case CENTER:
      position = position - (widgetWidth / 2) - _margin;
      break;

    case ABOVE:
      position = position + _margin;
      break;
    case BELOW:
      position = position - widgetHeight - _margin;
      break;
    case MIDDLE:
      position = position - (widgetHeight / 2) - _margin;
      break;
  }

  return position;
}
