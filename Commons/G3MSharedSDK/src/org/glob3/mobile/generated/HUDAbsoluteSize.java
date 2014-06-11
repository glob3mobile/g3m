package org.glob3.mobile.generated; 
//
//  HUDAbsoluteSize.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/26/13.
//
//



public class HUDAbsoluteSize extends HUDSize
{
  private final float _size;

  public HUDAbsoluteSize(float size)
  {
     _size = size;
  }

  public final float getSize(int viewPortWidth, int viewPortHeight, int bitmapWidth, int bitmapHeight)
  {
    return _size;
  }

}