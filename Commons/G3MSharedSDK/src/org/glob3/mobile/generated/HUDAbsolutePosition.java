package org.glob3.mobile.generated; 
//
//  HUDAbsolutePosition.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/20/13.
//
//

//
//  HUDAbsolutePosition.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/20/13.
//
//



public class HUDAbsolutePosition extends HUDPosition
{
  private final float _position;

  public HUDAbsolutePosition(float position)
  {
     _position = position;
  }

  public void dispose()
  {
    super.dispose();
  }

  public final float getPosition(int viewPortWidth, int viewPortHeight, float widgetWidth, float widgetHeight)
  {
    return _position;
  }

}