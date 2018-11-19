package org.glob3.mobile.generated;
//
//  HUDRelativePosition.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/20/13.
//
//

//
//  HUDRelativePosition.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/20/13.
//
//



public class HUDRelativePosition extends HUDPosition
{
  public enum Align
  {
    LEFT,
    RIGHT,
    CENTER,
    TOP,
    BOTTOM,
    MIDDLE;

     public int getValue()
     {
        return this.ordinal();
     }

     public static Align forValue(int value)
     {
        return values()[value];
     }
  }

  public enum Anchor
  {
    VIEWPORT_WIDTH,
    VIEWPORT_HEIGHT;

     public int getValue()
     {
        return this.ordinal();
     }

     public static Anchor forValue(int value)
     {
        return values()[value];
     }
  }

  private final float _factor;
  private final float _margin;
  private final Anchor _relativeTo;
  private final Align _align;

  public HUDRelativePosition(float factor, Anchor relativeTo, Align align)
  {
     this(factor, relativeTo, align, 0);
  }
  public HUDRelativePosition(float factor, Anchor relativeTo, Align align, float margin)
  {
     _factor = factor;
     _relativeTo = relativeTo;
     _align = align;
     _margin = margin;
  }

  public void dispose()
  {
    super.dispose();
  }

  public final float getPosition(int viewPortWidth, int viewPortHeight, float widgetWidth, float widgetHeight)
  {
  
    final float position = _factor * ((_relativeTo == Anchor.VIEWPORT_WIDTH) ? viewPortWidth : viewPortHeight);
  
    switch (_align)
    {
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

}
