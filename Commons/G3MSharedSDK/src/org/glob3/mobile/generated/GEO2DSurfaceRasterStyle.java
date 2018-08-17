package org.glob3.mobile.generated;import java.util.*;

//
//  GEO2DSurfaceRasterStyle.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/23/13.
//
//

//
//  GEO2DSurfaceRasterStyle.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/23/13.
//
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class ICanvas;

public class GEO2DSurfaceRasterStyle
{
  private final Color _color = new Color();

  public GEO2DSurfaceRasterStyle(Color color)
  {
	  _color = new Color(color);
  }

  public GEO2DSurfaceRasterStyle(GEO2DSurfaceRasterStyle that)
  {
	  _color = new Color(that._color);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean apply(ICanvas* canvas) const
  public final boolean apply(ICanvas canvas)
  {
	final boolean applied = !_color.isFullTransparent();
	if (applied)
	{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: canvas->setFillColor(_color);
	  canvas.setFillColor(new Color(_color));
	}
	return applied;
  }


}
