package org.glob3.mobile.generated;import java.util.*;

//
//  TextCanvasElement.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/12/13.
//
//

//
//  TextCanvasElement.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/12/13.
//
//




public class TextCanvasElement extends CanvasElement
{
  private final String _text;
  private final GFont _font;
  private final Color _color = new Color();

  public TextCanvasElement(String text, GFont font, Color color)
  {
	  _text = text;
	  _font = new GFont(font);
	  _color = new Color(color);

  }

  public void dispose()
  {
	if (_font != null)
		_font.dispose();
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  super.dispose();
//#endif

  }

  public final Vector2F getExtent(ICanvas canvas)
  {
	canvas.setFont(_font);
	return canvas.textExtent(_text);
  }

  public final void drawAt(float left, float top, ICanvas canvas)
  {
	canvas.setFont(_font);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: canvas->setFillColor(_color);
	canvas.setFillColor(new Color(_color));
	canvas.fillText(_text, left, top);
  }

}
