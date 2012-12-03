package org.glob3.mobile.generated; 
public class TransparentShapeWrapper extends OrderedRenderable
{
  private Shape _shape;
  private final double _squaredDistanceFromEye;

  public TransparentShapeWrapper(Shape shape, double squaredDistanceFromEye)
  {
	  _shape = shape;
	  _squaredDistanceFromEye = squaredDistanceFromEye;

  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double squaredDistanceFromEye() const
  public final double squaredDistanceFromEye()
  {
	return _squaredDistanceFromEye;
  }

  public final void render(G3MRenderContext rc)
  {
	_shape.render(rc);
  }

}