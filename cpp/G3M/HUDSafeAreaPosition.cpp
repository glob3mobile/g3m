//
//  HUDSafeAreaPosition.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/13/26.
//
//

#include "HUDSafeAreaPosition.hpp"

#include "IFactory.hpp"
#include "IDeviceInfo.hpp"


// the insets are asked every time: they change with the rotation, which rebuilds the widgets
float HUDSafeAreaPosition::getPosition(int viewPortWidth,
                                       int viewPortHeight,
                                       float widgetWidth,
                                       float widgetHeight) const {
  const IDeviceInfo* deviceInfo = IFactory::instance()->getDeviceInfo();
  const float margin = _marginPoints * deviceInfo->getDevicePixelRatio();

  // x from the left edge, y from the bottom edge, of the widget's lower-left corner
  switch (_edge) {
    case LEFT:
      return deviceInfo->getSafeAreaInsetLeft() + margin;
    case RIGHT:
      return viewPortWidth - deviceInfo->getSafeAreaInsetRight() - margin - widgetWidth;
    case TOP:
      return viewPortHeight - deviceInfo->getSafeAreaInsetTop() - margin - widgetHeight;
    case BOTTOM:
      return deviceInfo->getSafeAreaInsetBottom() + margin;
  }

  return 0;
}
