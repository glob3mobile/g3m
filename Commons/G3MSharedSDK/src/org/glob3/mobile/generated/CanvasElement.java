package org.glob3.mobile.generated; 
//
//  CanvasElement.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/12/13.
//
//

//
//  CanvasElement.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/12/13.
//
//



//class ICanvas;

public abstract class CanvasElement
{
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  CanvasElement(CanvasElement that);

  public CanvasElement()
  {

  }

  public void dispose()
  {
  }

  public abstract Vector2F getExtent(ICanvas canvas);

  public abstract void drawAt(float left, float top, ICanvas canvas);

  public void drawCentered(ICanvas canvas)
  {
    final Vector2F extent = getExtent(canvas);
    drawAt((canvas.getWidth() - extent._x) / 2, (canvas.getHeight() - extent._y) / 2, canvas);
  }

}