package org.glob3.mobile.generated;import java.util.*;

//
//  GEOLine2DStyle.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/26/13.
//
//

//
//  GEOLine2DStyle.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/26/13.
//
//





public class GEOLine2DStyle extends GEOStyle
{
  private final Color _color = new Color();
  private final float _width;

  public GEOLine2DStyle(Color color, float width)
  {
	  _color = new Color(color);
	  _width = width;

  }

  public void dispose()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  super.dispose();
//#endif

  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Color getColor() const
  public final Color getColor()
  {
	return _color;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const float getWidth() const
  public final float getWidth()
  {
	return _width;
  }

}
