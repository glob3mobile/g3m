//
//  HUDPosition.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 12/20/13.
//
//

#ifndef __G3M__HUDPosition__
#define __G3M__HUDPosition__


class HUDPosition {
public:
  virtual ~HUDPosition() {
  }

  virtual float getPosition(int viewPortWidth,
                            int viewPortHeight,
                            float widgetWidth,
                            float widgetHeight) const = 0;

};

#endif
