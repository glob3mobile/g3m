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

  public ColumnCanvasElement(Color color, float margin, float padding)
  {
     this(color, margin, padding, 0);
  }
  public ColumnCanvasElement(Color color, float margin)
  {
     this(color, margin, 0, 0);
  }
  public ColumnCanvasElement(Color color)
  {
     this(color, 0, 0, 0);
  }
  public ColumnCanvasElement()
  {
     this(Color.transparent(), 0, 0, 0);
  }
  public ColumnCanvasElement(Color color, float margin, float padding, float cornerRadius)
  {
     super(color, margin, padding, cornerRadius);
  }

  public void dispose()
  {
  super.dispose();
  }

  public final void rawDrawAt(float left, float top, Vector2F extent, ICanvas canvas)
  {
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