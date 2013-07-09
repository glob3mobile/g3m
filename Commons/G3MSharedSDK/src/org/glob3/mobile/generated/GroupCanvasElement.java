package org.glob3.mobile.generated; 
//
//  GroupCanvasElement.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/12/13.
//
//

//
//  GroupCanvasElement.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/12/13.
//
//




public abstract class GroupCanvasElement extends CanvasElement
{
  private Vector2F _extent;

  protected final Color _color ;
  protected java.util.ArrayList<CanvasElement> _children = new java.util.ArrayList<CanvasElement>();

  protected GroupCanvasElement(Color color)
  {
     _color = new Color(color);
     _extent = null;

  }

  protected void clearCaches()
  {
    if (_extent != null)
       _extent.dispose();
    _extent = null;
  }

  protected abstract Vector2F calculateExtent(ICanvas canvas);

  public void dispose()
  {
    final int childrenSize = _children.size();
    for (int i = 0; i < childrenSize; i++)
    {
      CanvasElement child = _children.get(i);
      if (child != null)
         child.dispose();
    }
  
    if (_extent != null)
       _extent.dispose();
  }

  public final void add(CanvasElement child)
  {
    _children.add(child);
    clearCaches();
  }

  public final Vector2F getExtent(ICanvas canvas)
  {
    if (_extent == null)
    {
      _extent = calculateExtent(canvas);
    }
  
    return _extent;
  }

  public abstract void drawAt(float left, float top, ICanvas canvas);

}