package org.glob3.mobile.generated; 
public class TransparentShapeWrapper extends OrderedRenderable
{
  private Shape _shape;
  private final double _squaredDistanceFromEye;

  private GLState _parentGLState;

  public TransparentShapeWrapper(Shape shape, double squaredDistanceFromEye, GLState parentGLState)
  {
     _shape = shape;
     _squaredDistanceFromEye = squaredDistanceFromEye;
     _parentGLState = parentGLState;
  }

  public final double squaredDistanceFromEye()
  {
    return _squaredDistanceFromEye;
  }

  public final void render(G3MRenderContext rc)
  {
    _shape.render(rc, _parentGLState);
  }

}