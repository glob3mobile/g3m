package org.glob3.mobile.generated; 
public class TransparentShapeWrapper extends OrderedRenderable
{
  private Shape _shape;
  private final double _squaredDistanceFromEye;
  private final boolean _renderNotReadyShapes;

<<<<<<< HEAD
  private GLState _parentGLState;

  public TransparentShapeWrapper(Shape shape, double squaredDistanceFromEye, GLState parentGLState)
  {
     _shape = shape;
     _squaredDistanceFromEye = squaredDistanceFromEye;
     _parentGLState = parentGLState;
=======
  public TransparentShapeWrapper(Shape shape, double squaredDistanceFromEye, boolean renderNotReadyShapes)
  {
     _shape = shape;
     _squaredDistanceFromEye = squaredDistanceFromEye;
     _renderNotReadyShapes = renderNotReadyShapes;

>>>>>>> webgl-port
  }

  public final double squaredDistanceFromEye()
  {
    return _squaredDistanceFromEye;
  }

  public final void render(G3MRenderContext rc)
  {
<<<<<<< HEAD
    _shape.render(rc, _parentGLState);
=======
    _shape.render(rc, parentState, _renderNotReadyShapes);
>>>>>>> webgl-port
  }
}