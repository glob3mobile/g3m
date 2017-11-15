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
  private final HorizontalAlignment _elementAlign;
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

  public ColumnCanvasElement(Color color, float margin, float padding, float cornerRadius)
  {
     this(color, margin, padding, cornerRadius, HorizontalAlignment.Center);
  }
  public ColumnCanvasElement(Color color, float margin, float padding)
  {
     this(color, margin, padding, 0, HorizontalAlignment.Center);
  }
  public ColumnCanvasElement(Color color, float margin)
  {
     this(color, margin, 0, 0, HorizontalAlignment.Center);
  }
  public ColumnCanvasElement(Color color)
  {
     this(color, 0, 0, 0, HorizontalAlignment.Center);
  }
  public ColumnCanvasElement()
  {
     this(Color.transparent(), 0, 0, 0, HorizontalAlignment.Center);
  }
  public ColumnCanvasElement(Color color, float margin, float padding, float cornerRadius, HorizontalAlignment elementAlign)
  {
     super(color, margin, padding, cornerRadius);
     _elementAlign = elementAlign;
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
  
      float cursorLeft;
      switch (_elementAlign)
      {
        case Left:
          cursorLeft = left;
          break;
        case Right:
          cursorLeft = left + extent._x - childExtent._x;
          break;
        case Center:
        default:
          cursorLeft = left + halfWidth - (childExtent._x / 2);
          break;
      }
  
      child.drawAt(cursorLeft, cursorTop, canvas);
  
      cursorTop += childExtent._y;
    }
  }

}