//
//  HUDSafeAreaPosition.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/13/26.
//
//

#ifndef __G3M__HUDSafeAreaPosition__
#define __G3M__HUDSafeAreaPosition__

#include "HUDPosition.hpp"


// measured from an edge of the screen's safe area (notch, island, rounded corners), with the margin
// in points; where the platform reports no safe area it is the screen edge
class HUDSafeAreaPosition : public HUDPosition {
public:
  enum Edge {
    LEFT,
    RIGHT,
    TOP,
    BOTTOM
  };

private:
  const HUDSafeAreaPosition::Edge _edge;
  const float                     _marginPoints;

public:
  HUDSafeAreaPosition(HUDSafeAreaPosition::Edge edge,
                      float                     marginPoints) :
  _edge(edge),
  _marginPoints(marginPoints)
  {
  }

  virtual ~HUDSafeAreaPosition() {
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
