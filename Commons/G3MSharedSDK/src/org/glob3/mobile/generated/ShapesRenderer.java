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
  }

  public final void onResume(InitializationContext ic)
  {

  }

  public final void onPause(InitializationContext ic)
  {

  }

  public final void initialize(InitializationContext ic)
  {

  }

  public final boolean isReadyToRender(RenderContext rc)
  {
	return true;
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

}