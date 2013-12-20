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
    ABOVE,
    BELOW,
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
    VIEWPORT_HEIGTH;

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
  private final HUDRelativePosition.Anchor _relativeTo;
  private final HUDRelativePosition.Align _align;

  public HUDRelativePosition(float factor, HUDRelativePosition.Anchor relativeTo, HUDRelativePosition.Align align)
  {
     this(factor, relativeTo, align, 0);
  }
  public HUDRelativePosition(float factor, HUDRelativePosition.Anchor relativeTo, HUDRelativePosition.Align align, float margin)
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
  
    float position;
    switch (_relativeTo)
    {
      case VIEWPORT_WIDTH:
        position = viewPortWidth * _factor;
        break;
      case VIEWPORT_HEIGTH:
        position = viewPortHeight * _factor;
        break;
    }
  
    switch (_align)
    {
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

}