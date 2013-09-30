package org.glob3.mobile.generated; 
//
//  HUDErrorRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/28/13.
//
//

//
//  HUDErrorRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/28/13.
//
//




public class HUDErrorRenderer_ImageFactory extends HUDImageRenderer.CanvasImageFactory
{
  private java.util.ArrayList<String> _errors = new java.util.ArrayList<String>();


  protected final void drawOn(ICanvas canvas, int width, int height)
  {
    canvas.setFillColor(Color.black());
    canvas.fillRectangle(0, 0, width, height);
  
    ColumnCanvasElement column = new ColumnCanvasElement(Color.fromRGBA(0.9f, 0.4f, 0.4f, 1.0f), 0, 16, 8); // cornerRadius -  padding -  margin
    final GFont labelFont = GFont.sansSerif(22);
    final Color labelColor = Color.white();
  //  column.add( new TextCanvasElement("Error message #1", labelFont, labelColor) );
  //  column.add( new TextCanvasElement("Another error message", labelFont, labelColor) );
  //  column.add( new TextCanvasElement("And another error message", labelFont, labelColor) );
  //  column.add( new TextCanvasElement("And just another error message", labelFont, labelColor) );
  
    final int errorsSize = _errors.size();
    for (int i = 0; i < errorsSize; i++)
    {
      column.add(new TextCanvasElement(_errors.get(i), labelFont, labelColor));
    }
  
    column.drawCentered(canvas);
  }

  public void dispose()
  {
  }

  public final void setErrors(java.util.ArrayList<String> errors)
  {
    _errors.clear();
    _errors.addAll(errors);
  }
}