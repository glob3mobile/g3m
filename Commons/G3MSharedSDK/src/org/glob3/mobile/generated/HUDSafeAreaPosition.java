package org.glob3.mobile.generated;
//
//  HUDSafeAreaPosition.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/13/26.
//
//

//
//  HUDSafeAreaPosition.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/13/26.
//
//




// measured from an edge of the screen's safe area (notch, island, rounded corners), with the margin
// in points; where the platform reports no safe area it is the screen edge
public class HUDSafeAreaPosition extends HUDPosition
{
  public enum Edge
  {
    LEFT,
    RIGHT,
    TOP,
    BOTTOM;

     public int getValue()
     {
        return this.ordinal();
     }

     public static Edge forValue(int value)
     {
        return values()[value];
     }
  }

  private final HUDSafeAreaPosition.Edge _edge;
  private final float _marginPoints;

  public HUDSafeAreaPosition(HUDSafeAreaPosition.Edge edge, float marginPoints)
  {
     _edge = edge;
     _marginPoints = marginPoints;
  }

  public void dispose()
  {
    super.dispose();
  }


  // the insets are asked every time: they change with the rotation, which rebuilds the widgets
  public final float getPosition(int viewPortWidth, int viewPortHeight, float widgetWidth, float widgetHeight)
  {
    final IDeviceInfo deviceInfo = IFactory.instance().getDeviceInfo();
    final float margin = _marginPoints * deviceInfo.getDevicePixelRatio();
  
    // x from the left edge, y from the bottom edge, of the widget's lower-left corner
    switch (_edge)
    {
      case LEFT:
        return deviceInfo.getSafeAreaInsetLeft() + margin;
      case RIGHT:
        return viewPortWidth - deviceInfo.getSafeAreaInsetRight() - margin - widgetWidth;
      case TOP:
        return viewPortHeight - deviceInfo.getSafeAreaInsetTop() - margin - widgetHeight;
      case BOTTOM:
        return deviceInfo.getSafeAreaInsetBottom() + margin;
    }
  
    return 0;
  }

}