package org.glob3.mobile.generated;import java.util.*;

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
  private Vector2F _rawExtent;

  protected Color _color = new Color();
  protected float _margin;
  protected float _padding;
  protected float _cornerRadius;

  protected java.util.ArrayList<CanvasElement> _children = new java.util.ArrayList<CanvasElement>();

  protected GroupCanvasElement(Color color, float margin, float padding, float cornerRadius)
  {
	  _color = new Color(color);
	  _margin = margin;
	  _padding = padding;
	  _cornerRadius = cornerRadius;
	  _extent = null;
	  _rawExtent = null;

  }

  protected void clearCaches()
  {
	if (_extent != null)
		_extent.dispose();
	_extent = null;
  
	if (_rawExtent != null)
		_rawExtent.dispose();
	_rawExtent = null;
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
	if (_rawExtent != null)
		_rawExtent.dispose();
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
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
	  _rawExtent = calculateExtent(canvas);
  
	  final float extra = (_margin + _padding) * 2;
	  _extent = new Vector2F(_rawExtent._x + extra, _rawExtent._y + extra);
	}
  
	return _extent;
  }

  public final void drawAt(float left, float top, ICanvas canvas)
  {
	final Vector2F extent = getExtent(canvas);
  
  //  canvas->setLineColor(Color::yellow());
  //  canvas->strokeRectangle(left,
  //                          top,
  //                          extent._x,
  //                          extent._y);
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: canvas->setFillColor(_color);
	canvas.setFillColor(new Color(_color));
	canvas.fillRoundedRectangle(left + _margin, top + _margin, extent._x - _margin *2, extent._y - _margin *2, _cornerRadius);
  
	final float extra = _margin + _padding;
	rawDrawAt(left + extra, top + extra, _rawExtent, canvas);
  }

  public abstract void rawDrawAt(float left, float top, Vector2F extent, ICanvas canvas);

}
