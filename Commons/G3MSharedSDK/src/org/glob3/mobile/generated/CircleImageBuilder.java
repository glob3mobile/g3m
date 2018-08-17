package org.glob3.mobile.generated;//
//  CircleImageBuilder.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/2/15.
//
//

//
//  CircleImageBuilder.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/2/15.
//
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Color;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class G3MContext;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class ICanvas;


public class CircleImageBuilder extends CanvasImageBuilder
{

  private final Color _color = new Color();
  private final int _radius;

  protected final void buildOnCanvas(G3MContext context, ICanvas canvas)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: canvas->setFillColor(_color);
	canvas.setFillColor(new Color(_color));
	canvas.fillEllipse(1, 1, _radius *2, _radius *2);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: String getImageName(const G3MContext* context) const
  protected final String getImageName(G3MContext context)
  {
	final IStringUtils su = context.getStringUtils();
  
	return "_CircleImage_" + _color.toID() + "_" + su.toString(_radius);
  }

  public CircleImageBuilder(Color color, int radius)
  {
	  super(radius *2 + 2, radius *2 + 2, true);
	  _color = new Color(color);
	  _radius = radius;
  
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isMutable() const
  public final boolean isMutable()
  {
	return false;
  }
}
