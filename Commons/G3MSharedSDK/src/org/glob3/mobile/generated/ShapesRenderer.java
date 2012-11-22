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

  private Context _context;


  public ShapesRenderer()
  {
	  _context = null;

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
	if (_context != null)
	{
	  shape.initialize(_context);
	}
  }

  public final void onResume(Context context)
  {
	_context = context;
  }

  public final void onPause(Context context)
  {

  }

  public final void onDestroy(Context context)
  {

  }

  public final void initialize(Context context)
  {
	_context = context;

	final int shapesCount = _shapes.size();
	for (int i = 0; i < shapesCount; i++)
	{
	  Shape shape = _shapes.get(i);
	  shape.initialize(context);
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
	final Vector3D cameraPosition = rc.getCurrentCamera().getCartesianPosition();
  
	final int shapesCount = _shapes.size();
	for (int i = 0; i < shapesCount; i++)
	{
	  Shape shape = _shapes.get(i);
	  if (shape.isTransparent(rc))
	  {
		final Planet planet = rc.getPlanet();
		final Vector3D shapePosition = planet.toCartesian(shape.getPosition());
		final double squaredDistanceFromEye = shapePosition.sub(cameraPosition).squaredLength();
  
		rc.addOrderedRenderable(new TransparentShapeWrapper(shape, squaredDistanceFromEye));
	  }
	  else
	  {
		shape.render(rc);
	  }
	}
  }

}