package org.glob3.mobile.generated; 
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
  private final Color _color ;

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
  super.dispose();

  }

  public final Vector2F getExtent(ICanvas canvas)
  {
    canvas.setFont(_font);
    return canvas.textExtent(_text);
  }

  public final void drawAt(float left, float top, ICanvas canvas)
  {
    canvas.setFont(_font);
    canvas.setFillColor(_color);
    canvas.fillText(_text, left, top);
  }

}