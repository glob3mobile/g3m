package org.glob3.mobile.generated; 
//
//  ColumnCanvasElement.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/12/13.
//
//

//
//  ColumnCanvasElement.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/12/13.
//
//



public class ColumnCanvasElement extends GroupCanvasElement
{
  protected final Vector2F calculateExtent(ICanvas canvas)
  {
    float width = 0F;
    float height = 0F;
  
    final int childrenSize = _children.size();
    for (int i = 0; i < childrenSize; i++)
    {
      CanvasElement child = _children.get(i);
  
      final Vector2F childExtent = child.getExtent(canvas);
  
      if (childExtent._x > width)
      {
        width = childExtent._x;
      }
  
      height += childExtent._y;
    }
  
    return new Vector2F(width, height);
  }

  public ColumnCanvasElement()
  {
     this(Color.transparent());
  }
  public ColumnCanvasElement(Color color)
  {
     super(color);

  }

  public void dispose()
  {
  super.dispose();

  }

  public final void drawAt(float left, float top, ICanvas canvas)
  {
    final Vector2F extent = getExtent(canvas);
  
    canvas.setFillColor(_color);
    canvas.fillRectangle(left, top, extent._x, extent._y);
  
    final float halfWidth = extent._x / 2;
  
    float cursorTop = top;
  
    final int childrenSize = _children.size();
    for (int i = 0; i < childrenSize; i++)
    {
      CanvasElement child = _children.get(i);
  
      final Vector2F childExtent = child.getExtent(canvas);
  
      final float cursorLeft = left + halfWidth - (childExtent._x / 2);
  
      child.drawAt(cursorLeft, cursorTop, canvas);
  
      cursorTop += childExtent._y;
    }
  }

}