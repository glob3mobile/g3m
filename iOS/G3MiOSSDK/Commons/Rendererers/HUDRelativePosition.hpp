//
//  HUDRelativePosition.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/20/13.
//
//

#ifndef __G3MiOSSDK__HUDRelativePosition__
#define __G3MiOSSDK__HUDRelativePosition__

#include "HUDPosition.hpp"

class HUDRelativePosition : public HUDPosition {
public:
  enum Align {
    LEFT,
    RIGHT,
    CENTER,
    ABOVE,
    BELOW,
    MIDDLE
  };

  enum Anchor {
    VIEWPORT_WIDTH,
    VIEWPORT_HEIGTH
  };

private:
  const float _factor;
  const float _margin;
  const HUDRelativePosition::Anchor _relativeTo;
  const HUDRelativePosition::Align  _align;

public:
  HUDRelativePosition(float factor,
                      HUDRelativePosition::Anchor relativeTo,
                      HUDRelativePosition::Align  align,
                      float margin = 0) :
  _factor(factor),
  _relativeTo(relativeTo),
  _align(align),
  _margin(margin)
  {
  }

  virtual ~HUDRelativePosition() {
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
