package org.glob3.mobile.generated; 
//
//  ShapesRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/28/12.
//
//

//
//  ShapesRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/28/12.
//
//



public class ShapesRenderer extends LeafRenderer
{
  private java.util.ArrayList<Shape> _shapes = new java.util.ArrayList<Shape>();

  private InitializationContext _initializationContext;


  public ShapesRenderer()
  {
	  _initializationContext = null;

  }

  public void dispose()
  {
	final int shapesCount = _shapes.size();
	for (int i = 0; i < shapesCount; i++)
	{
	  Shape shape = _shapes.get(i);
	  if (shape != null)
		  shape.dispose();
	}
  }

  public final void addShape(Shape shape)
  {
	_shapes.add(shape);
	if (_initializationContext != null)
	{
	  shape.initialize(_initializationContext);
	}
  }

  public final void onResume(InitializationContext ic)
  {
	_initializationContext = ic;
  }

  public final void onPause(InitializationContext ic)
  {
  }

  public final void initialize(InitializationContext ic)
  {
	_initializationContext = ic;

	final int shapesCount = _shapes.size();
	for (int i = 0; i < shapesCount; i++)
	{
	  Shape shape = _shapes.get(i);
	  shape.initialize(ic);
	}
  }

  public final boolean isReadyToRender(RenderContext rc)
  {
	return true;
  }

  public final boolean onTouchEvent(EventContext ec, TouchEvent touchEvent)
  {
	return false;
  }

  public final void onResizeViewportEvent(EventContext ec, int width, int height)
  {
  }

  public final void start()
  {
  }

  public final void stop()
  {
  }

  public final void render(RenderContext rc)
  {
	final int shapesCount = _shapes.size();
	for (int i = 0; i < shapesCount; i++)
	{
	  Shape shape = _shapes.get(i);
	  shape.render(rc);
	}
  }

}