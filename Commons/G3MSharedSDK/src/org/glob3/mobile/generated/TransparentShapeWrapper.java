package org.glob3.mobile.generated; 
public class TransparentShapeWrapper extends OrderedRenderable
{
  private Shape _shape;
  private final double _squaredDistanceFromEye;
  private final boolean _renderNotReadyShapes;

  public TransparentShapeWrapper(Shape shape, double squaredDistanceFromEye, boolean renderNotReadyShapes)
  {
     _shape = shape;
     _squaredDistanceFromEye = squaredDistanceFromEye;
     _renderNotReadyShapes = renderNotReadyShapes;

  }

  public final double squaredDistanceFromEye()
  {
    return _squaredDistanceFromEye;
  }

  public final void render(G3MRenderContext rc, GLState parentState)
  {
    _shape.render(rc, parentState, _renderNotReadyShapes);
  }
}