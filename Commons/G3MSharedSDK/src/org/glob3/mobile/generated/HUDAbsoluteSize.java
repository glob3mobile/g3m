package org.glob3.mobile.generated;//
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: float getSize(int viewPortWidth, int viewPortHeight, int bitmapWidth, int bitmapHeight) const
  public final float getSize(int viewPortWidth, int viewPortHeight, int bitmapWidth, int bitmapHeight)
  {
	return _size;
  }

}
