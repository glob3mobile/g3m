//
//  HUDAbsolutePosition.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/20/13.
//
//

#ifndef __G3MiOSSDK__HUDAbsolutePosition__
#define __G3MiOSSDK__HUDAbsolutePosition__

#include "HUDPosition.hpp"

class HUDAbsolutePosition : public HUDPosition {
private:
  const float _position;

public:
  HUDAbsolutePosition(float position) :
  _position(position)
  {
  }

  virtual ~HUDAbsolutePosition() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

  float getPosition(int viewPortWidth,
                    int viewPortHeight,
                    float widgetWidth,
                    float widgetHeight) const;
  
};


#endif
