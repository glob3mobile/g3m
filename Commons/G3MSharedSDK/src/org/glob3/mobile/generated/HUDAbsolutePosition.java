package org.glob3.mobile.generated;import java.util.*;

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
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: float getPosition(int viewPortWidth, int viewPortHeight, float widgetWidth, float widgetHeight) const
  public final float getPosition(int viewPortWidth, int viewPortHeight, float widgetWidth, float widgetHeight)
  {
	return _position;
  }

}
