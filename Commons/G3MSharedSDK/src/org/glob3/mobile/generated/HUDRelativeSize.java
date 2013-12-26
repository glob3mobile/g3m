package org.glob3.mobile.generated; 
//
//  HUDRelativeSize.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/26/13.
//
//

//
//  HUDRelativeSize.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/26/13.
//
//



public class HUDRelativeSize extends HUDSize
{
  public enum Anchor
  {
    VIEWPORT_WIDTH,
    VIEWPORT_HEIGTH,
    VIEWPORT_MIN_AXIS,
    VIEWPORT_MAX_AXIS,
    BITMAP_WIDTH,
    BITMAP_HEIGTH,
    BITMAP_MIN_AXIS,
    BITMAP_MAX_AXIS;

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
  private final HUDRelativeSize.Anchor _relativeTo;

  public HUDRelativeSize(float factor, HUDRelativeSize.Anchor relativeTo)
  {
     _factor = factor;
     _relativeTo = relativeTo;
  }


  public final float getSize(int viewPortWidth, int viewPortHeight, int bitmapWidth, int bitmapHeight)
  {
    switch (_relativeTo)
    {
      case VIEWPORT_WIDTH:
        return _factor * viewPortWidth;
      case VIEWPORT_HEIGTH:
        return _factor * viewPortHeight;
      case VIEWPORT_MIN_AXIS:
        return _factor * ((viewPortWidth < viewPortHeight) ? viewPortWidth : viewPortHeight);
      case VIEWPORT_MAX_AXIS:
        return _factor * ((viewPortWidth > viewPortHeight) ? viewPortWidth : viewPortHeight);
  
      case BITMAP_WIDTH:
        return _factor * bitmapWidth;
      case BITMAP_HEIGTH:
        return _factor * bitmapHeight;
      case BITMAP_MIN_AXIS:
        return _factor * ((bitmapWidth < bitmapHeight) ? bitmapWidth : bitmapHeight);
      case BITMAP_MAX_AXIS:
        return _factor * ((bitmapWidth > bitmapHeight) ? bitmapWidth : bitmapHeight);
    }
  
    return 0;
  }

}