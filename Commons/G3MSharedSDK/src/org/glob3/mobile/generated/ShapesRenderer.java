package org.glob3.mobile.generated; 
public class ShapesRenderer extends LeafRenderer
{
  private final boolean _renderNotReadyShapes;

  private java.util.ArrayList<Shape> _shapes = new java.util.ArrayList<Shape>();

  private G3MContext _context;
  private Camera    _lastCamera;

  private GLState _glState;
  private GLState _glStateTransparent;

//  ProjectionGLFeature* _projection;
//  ModelGLFeature*      _model;
  private void updateGLState(G3MRenderContext rc)
  {
  
    final Camera cam = rc.getCurrentCamera();
    /*
  
    if (_projection == NULL) {
      _projection = new ProjectionGLFeature(cam->getProjectionMatrix44D());
      _glState->addGLFeature(_projection, true);
      _glStateTransparent->addGLFeature(_projection, true);
    } else{
      _projection->setMatrix(cam->getProjectionMatrix44D());
    }
  
    if (_model == NULL) {
      _model = new ModelGLFeature(cam->getModelMatrix44D());
      _glState->addGLFeature(_model, true);
      _glStateTransparent->addGLFeature(_model, true);
    } else{
      _model->setMatrix(cam->getModelMatrix44D());
    }
  */
    ModelViewGLFeature f = (ModelViewGLFeature) _glState.getGLFeature(GLFeatureID.GLF_MODEL_VIEW);
    if (f == null)
    {
      _glState.addGLFeature(new ModelViewGLFeature(cam), true);
    }
    else
    {
      f.setMatrix(cam.getModelViewMatrix44D());
    }
  
    f = (ModelViewGLFeature) _glStateTransparent.getGLFeature(GLFeatureID.GLF_MODEL_VIEW);
    if (f == null)
    {
      _glStateTransparent.addGLFeature(new ModelViewGLFeature(cam), true);
    }
    else
    {
      f.setMatrix(cam.getModelViewMatrix44D());
    }
  
  }


  public ShapesRenderer()
  {
     this(true);
  }
  public ShapesRenderer(boolean renderNotReadyShapes)
//  _projection(NULL),
//  _model(NULL),
  {
     _renderNotReadyShapes = renderNotReadyShapes;
     _context = null;
     _glState = new GLState();
     _glStateTransparent = new GLState();
     _lastCamera = null;
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

    _glState._release();
    _glStateTransparent._release();

  super.dispose();

  }

  public final void addShape(Shape shape)
  {
    _shapes.add(shape);
    if (_context != null)
    {
      shape.initialize(_context);
    }
  }

  public final void removeAllShapes()
  {
     removeAllShapes(true);
  }
  public final void removeAllShapes(boolean deleteShapes)
  {
    if (deleteShapes)
    {
      final int shapesCount = _shapes.size();
      for (int i = 0; i < shapesCount; i++)
      {
        Shape shape = _shapes.get(i);
        if (shape != null)
           shape.dispose();
      }
    }
  
    _shapes.clear();
  }

  public final void onResume(G3MContext context)
  {
    _context = context;
  }

  public final void onPause(G3MContext context)
  {

  }

  public final void onDestroy(G3MContext context)
  {

  }

  public final void initialize(G3MContext context)
  {
    _context = context;

    final int shapesCount = _shapes.size();
    for (int i = 0; i < shapesCount; i++)
    {
      Shape shape = _shapes.get(i);
      shape.initialize(context);
    }
  }

  public final boolean isReadyToRender(G3MRenderContext rc)
  {
    if (_renderNotReadyShapes)
    {
      return true;
    }
  
    final int shapesCount = _shapes.size();
    for (int i = 0; i < shapesCount; i++)
    {
      Shape shape = _shapes.get(i);
      final boolean shapeReady = shape.isReadyToRender(rc);
      if (!shapeReady)
      {
        return false;
      }
    }
    return true;
  }

  public final boolean onTouchEvent(G3MEventContext ec, TouchEvent touchEvent)
  {
    if (_lastCamera != null)
    {
      if (touchEvent.getTouchCount() == 1 && touchEvent.getTapCount() == 1 && touchEvent.getType() == TouchEventType.Down)
      {
        final Vector3D origin = _lastCamera.getCartesianPosition();
        final Vector2I pixel = touchEvent.getTouch(0).getPos();
        final Vector3D direction = _lastCamera.pixel2Ray(pixel);
        java.util.ArrayList<ShapeDistance> shapeDistances = intersectionsDistances(origin, direction);
  
        if (!shapeDistances.isEmpty())
        {
  //        printf ("Found %d intersections with shapes:\n",
  //                (int)shapeDistances.size());
          for (int i = 0; i<shapeDistances.size(); i++)
          {
  //          printf ("   %d: shape %x to distance %f\n",
  //                  i+1,
  //                  (unsigned int)shapeDistances[i]._shape,
  //                  shapeDistances[i]._distance);
          }
        }
      }
    }
    return false;
  }

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {
  }

  public final void start(G3MRenderContext rc)
  {
  }

  public final void stop(G3MRenderContext rc)
  {
  }

  public final void render(G3MRenderContext rc, GLState glState)
  {
    // Saving camera for use in onTouchEvent
    _lastCamera = rc.getCurrentCamera();
  
    final Vector3D cameraPosition = rc.getCurrentCamera().getCartesianPosition();
  
    //Setting camera matrixes
    updateGLState(rc);
  
    _glState.setParent(glState);
    _glStateTransparent.setParent(glState);
  
  
    final int shapesCount = _shapes.size();
    for (int i = 0; i < shapesCount; i++)
    {
      Shape shape = _shapes.get(i);
      if (shape.isEnable())
      {
        if (shape.isTransparent(rc))
        {
          final Planet planet = rc.getPlanet();
          final Vector3D shapePosition = planet.toCartesian(shape.getPosition());
          final double squaredDistanceFromEye = shapePosition.sub(cameraPosition).squaredLength();
  
          rc.addOrderedRenderable(new TransparentShapeWrapper(shape, squaredDistanceFromEye, _glStateTransparent, _renderNotReadyShapes));
        }
        else
        {
          shape.render(rc, _glState, _renderNotReadyShapes);
        }
      }
    }
  }

  public final java.util.ArrayList<ShapeDistance> intersectionsDistances(Vector3D origin, Vector3D direction)
  {
    java.util.ArrayList<ShapeDistance> shapeDistances = new java.util.ArrayList<ShapeDistance>();
    for (int n = 0; n<_shapes.size(); n++)
    {
      Shape shape = _shapes.get(n);
      java.util.ArrayList<Double> distances = shape.intersectionsDistances(origin, direction);
      for (int i = 0; i<distances.size(); i++)
      {
        shapeDistances.add(new ShapeDistance(distances.get(i), shape));
      }
    }
  
    // sort vector
    java.util.Collections.sort(shapeDistances,
                               new java.util.Comparator<ShapeDistance>() {
                                 @Override
                                 public int compare(final ShapeDistance sd1,
                                                    final ShapeDistance sd2) {
                                   return Double.compare(sd1._distance, sd2._distance);
                                 }
                               });
  
    return shapeDistances;
  }


}