//
//  HUDPosition.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/20/13.
//
//

#ifndef __G3MiOSSDK__HUDPosition__
#define __G3MiOSSDK__HUDPosition__


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
