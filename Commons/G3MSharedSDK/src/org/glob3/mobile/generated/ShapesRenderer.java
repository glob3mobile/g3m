package org.glob3.mobile.generated; 
public class ShapesRenderer extends DefaultRenderer
{
  private static class LoadQueueItem
  {
    public final URL _url;
    public final TimeInterval _timeToCache;
    public final long _priority;
    public final boolean _readExpired;
    public final String _uriPrefix;
    public final boolean _isTransparent;
    public Geodetic3D _position;
    public final AltitudeMode _altitudeMode;
    public ShapeLoadListener _listener;
    public final boolean _deleteListener;
    public final boolean _isBSON;

    public LoadQueueItem(URL url, long priority, TimeInterval timeToCache, boolean readExpired, String uriPrefix, boolean isTransparent, Geodetic3D position, AltitudeMode altitudeMode, ShapeLoadListener listener, boolean deleteListener, boolean isBSON)
    {
       _url = url;
       _priority = priority;
       _timeToCache = timeToCache;
       _readExpired = readExpired;
       _uriPrefix = uriPrefix;
       _isTransparent = isTransparent;
       _position = position;
       _altitudeMode = altitudeMode;
       _listener = listener;
       _deleteListener = deleteListener;
       _isBSON = isBSON;

    }

    public void dispose()
    {
    }
  }


  private final boolean _renderNotReadyShapes;

  private java.util.ArrayList<Shape> _shapes = new java.util.ArrayList<Shape>();

  private Camera    _lastCamera;

  private GLState _glState;
  private GLState _glStateTransparent;

  private void updateGLState(G3MRenderContext rc)
  {
  
    final Camera camera = rc.getCurrentCamera();
    ModelViewGLFeature f = (ModelViewGLFeature) _glState.getGLFeature(GLFeatureID.GLF_MODEL_VIEW);
    if (f == null)
    {
      _glState.addGLFeature(new ModelViewGLFeature(camera), true);
    }
    else
    {
      f.setMatrix(camera.getModelViewMatrix44D());
    }
  
    f = (ModelViewGLFeature) _glStateTransparent.getGLFeature(GLFeatureID.GLF_MODEL_VIEW);
    if (f == null)
    {
      _glStateTransparent.addGLFeature(new ModelViewGLFeature(camera), true);
    }
    else
    {
      f.setMatrix(camera.getModelViewMatrix44D());
    }
  
  }

  private java.util.ArrayList<LoadQueueItem> _loadQueue = new java.util.ArrayList<LoadQueueItem>();

  private void drainLoadQueue()
  {
  
    final int loadQueueSize = _loadQueue.size();
    for (int i = 0; i < loadQueueSize; i++)
    {
      LoadQueueItem item = _loadQueue.get(i);
      requestBuffer(item._url, item._priority, item._timeToCache, item._readExpired, item._uriPrefix, item._isTransparent, item._position, item._altitudeMode, item._listener, item._deleteListener, item._isBSON);
  
      if (item != null)
         item.dispose();
    }
  
    _loadQueue.clear();
  }

  private void cleanLoadQueue()
  {
    final int loadQueueSize = _loadQueue.size();
    for (int i = 0; i < loadQueueSize; i++)
    {
      LoadQueueItem item = _loadQueue.get(i);
      if (item != null)
         item.dispose();
    }
    _loadQueue.clear();
  }


  private void requestBuffer(URL url, long priority, TimeInterval timeToCache, boolean readExpired, String uriPrefix, boolean isTransparent, Geodetic3D position, AltitudeMode altitudeMode, ShapeLoadListener listener, boolean deleteListener, boolean isBSON)
  {
  
    IDownloader downloader = _context.getDownloader();
    downloader.requestBuffer(url, priority, timeToCache, readExpired, new ShapesRenderer_SceneJSBufferDownloadListener(this, uriPrefix, isTransparent, position, altitudeMode, listener, deleteListener, _context.getThreadUtils(), isBSON), true);
  
  }


  public ShapesRenderer()
  {
     this(true);
  }
  public ShapesRenderer(boolean renderNotReadyShapes)
  {
     _renderNotReadyShapes = renderNotReadyShapes;
     _glState = new GLState();
     _glStateTransparent = new GLState();
     _lastCamera = null;
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

  public final void removeShape(Shape shape)
  {
    int pos = -1;
    final int shapesSize = _shapes.size();
    for (int i = 0; i < shapesSize; i++)
    {
      if (_shapes.get(i) == shape)
      {
        pos = i;
        break;
      }
    }
    if (pos != -1)
    {
      _shapes.remove(pos);
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

  public final void enableAll()
  {
    final int shapesCount = _shapes.size();
    for (int i = 0; i < shapesCount; i++)
    {
      Shape shape = _shapes.get(i);
      shape.setEnable(true);
    }
  }

  public final void disableAll()
  {
    final int shapesCount = _shapes.size();
    for (int i = 0; i < shapesCount; i++)
    {
      Shape shape = _shapes.get(i);
      shape.setEnable(false);
    }
  }


  public final void onResume(G3MContext context)
  {
    _context = context;
  }

  public final void onChangedContext()
  {
    if (_context != null)
    {
      final int shapesCount = _shapes.size();
      for (int i = 0; i < shapesCount; i++)
      {
        Shape shape = _shapes.get(i);
        shape.initialize(_context);
      }
  
      drainLoadQueue();
    }
  }

  public final void onLostContext()
  {
    if (_context == null)
    {
      cleanLoadQueue();
    }
  }

  public final RenderState getRenderState(G3MRenderContext rc)
  {
    if (!_renderNotReadyShapes)
    {
      final int shapesCount = _shapes.size();
      for (int i = 0; i < shapesCount; i++)
      {
        Shape shape = _shapes.get(i);
        final boolean shapeReady = shape.isReadyToRender(rc);
        if (!shapeReady)
        {
          return RenderState.busy();
        }
      }
    }
    return RenderState.ready();
  }

  public final boolean onTouchEvent(G3MEventContext ec, TouchEvent touchEvent)
  {
    if (_lastCamera != null)
    {
      if (touchEvent.getTouchCount() == 1 && touchEvent.getTapCount() == 1 && touchEvent.getType() == TouchEventType.Down)
      {
        final Vector3D origin = _lastCamera.getCartesianPosition();
        final Vector2F pixel = touchEvent.getTouch(0).getPos();
        final Vector3D direction = _lastCamera.pixel2Ray(pixel);
        final Planet planet = ec.getPlanet();
        if (!direction.isNan())
        {
          java.util.ArrayList<ShapeDistance> shapeDistances = intersectionsDistances(planet, origin, direction);
  
          if (!shapeDistances.isEmpty())
          {
            //        printf ("Found %d intersections with shapes:\n",
            //                (int)shapeDistances.size());
            for (int i = 0; i<shapeDistances.size(); i++)
            {
  //            printf ("   %d: shape %x to distance %f\n",
  //                    i+1,
  //                    (unsigned int)shapeDistances[i]._shape,
  //                    shapeDistances[i]._distance);
            }
          }
        }
        else
        {
          ILogger.instance().logWarning("ShapesRenderer::onTouchEvent: direction ( - _lastCamera->pixel2Ray(pixel) - ) is NaN");
        }
  
      }
    }
    return false;
  }

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {
  }

  public final void render(G3MRenderContext rc, GLState glState)
  {
    // Saving camera for use in onTouchEvent
    _lastCamera = rc.getCurrentCamera();
  
    MutableVector3D cameraPosition = new MutableVector3D();
    rc.getCurrentCamera().getCartesianPositionMutable(cameraPosition);
  
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

  public final java.util.ArrayList<ShapeDistance> intersectionsDistances(Planet planet, Vector3D origin, Vector3D direction)
  {
    java.util.ArrayList<ShapeDistance> shapeDistances = new java.util.ArrayList<ShapeDistance>();
    for (int n = 0; n<_shapes.size(); n++)
    {
      Shape shape = _shapes.get(n);
      java.util.ArrayList<Double> distances = shape.intersectionsDistances(planet, origin, direction);
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

  public final void loadJSONSceneJS(URL url, long priority, TimeInterval timeToCache, boolean readExpired, String uriPrefix, boolean isTransparent, Geodetic3D position, AltitudeMode altitudeMode, ShapeLoadListener listener)
  {
     loadJSONSceneJS(url, priority, timeToCache, readExpired, uriPrefix, isTransparent, position, altitudeMode, listener, true);
  }
  public final void loadJSONSceneJS(URL url, long priority, TimeInterval timeToCache, boolean readExpired, String uriPrefix, boolean isTransparent, Geodetic3D position, AltitudeMode altitudeMode)
  {
     loadJSONSceneJS(url, priority, timeToCache, readExpired, uriPrefix, isTransparent, position, altitudeMode, null, true);
  }
  public final void loadJSONSceneJS(URL url, long priority, TimeInterval timeToCache, boolean readExpired, String uriPrefix, boolean isTransparent, Geodetic3D position, AltitudeMode altitudeMode, ShapeLoadListener listener, boolean deleteListener)
  {
    if (_context == null)
    {
      _loadQueue.add(new LoadQueueItem(url, priority, timeToCache, readExpired, uriPrefix, isTransparent, position, altitudeMode, listener, deleteListener, false)); // isBson
    }
    else
    {
      requestBuffer(url, priority, timeToCache, readExpired, uriPrefix, isTransparent, position, altitudeMode, listener, deleteListener, false); // isBson
    }
  }

  public final void loadJSONSceneJS(URL url, String uriPrefix, boolean isTransparent, Geodetic3D position, AltitudeMode altitudeMode, ShapeLoadListener listener)
  {
     loadJSONSceneJS(url, uriPrefix, isTransparent, position, altitudeMode, listener, true);
  }
  public final void loadJSONSceneJS(URL url, String uriPrefix, boolean isTransparent, Geodetic3D position, AltitudeMode altitudeMode)
  {
     loadJSONSceneJS(url, uriPrefix, isTransparent, position, altitudeMode, null, true);
  }
  public final void loadJSONSceneJS(URL url, String uriPrefix, boolean isTransparent, Geodetic3D position, AltitudeMode altitudeMode, ShapeLoadListener listener, boolean deleteListener)
  {
    loadJSONSceneJS(url, DownloadPriority.MEDIUM, TimeInterval.fromDays(30), true, uriPrefix, isTransparent, position, altitudeMode, listener, deleteListener);
  }

  public final void loadBSONSceneJS(URL url, long priority, TimeInterval timeToCache, boolean readExpired, String uriPrefix, boolean isTransparent, Geodetic3D position, AltitudeMode altitudeMode, ShapeLoadListener listener)
  {
     loadBSONSceneJS(url, priority, timeToCache, readExpired, uriPrefix, isTransparent, position, altitudeMode, listener, true);
  }
  public final void loadBSONSceneJS(URL url, long priority, TimeInterval timeToCache, boolean readExpired, String uriPrefix, boolean isTransparent, Geodetic3D position, AltitudeMode altitudeMode)
  {
     loadBSONSceneJS(url, priority, timeToCache, readExpired, uriPrefix, isTransparent, position, altitudeMode, null, true);
  }
  public final void loadBSONSceneJS(URL url, long priority, TimeInterval timeToCache, boolean readExpired, String uriPrefix, boolean isTransparent, Geodetic3D position, AltitudeMode altitudeMode, ShapeLoadListener listener, boolean deleteListener)
  {
    if (_context == null)
    {
      _loadQueue.add(new LoadQueueItem(url, priority, timeToCache, readExpired, uriPrefix, isTransparent, position, altitudeMode, listener, deleteListener, true)); // isBson
    }
    else
    {
      requestBuffer(url, priority, timeToCache, readExpired, uriPrefix, isTransparent, position, altitudeMode, listener, deleteListener, true); // isBson
    }
  }

  public final void loadBSONSceneJS(URL url, String uriPrefix, boolean isTransparent, Geodetic3D position, AltitudeMode altitudeMode, ShapeLoadListener listener)
  {
     loadBSONSceneJS(url, uriPrefix, isTransparent, position, altitudeMode, listener, true);
  }
  public final void loadBSONSceneJS(URL url, String uriPrefix, boolean isTransparent, Geodetic3D position, AltitudeMode altitudeMode)
  {
     loadBSONSceneJS(url, uriPrefix, isTransparent, position, altitudeMode, null, true);
  }
  public final void loadBSONSceneJS(URL url, String uriPrefix, boolean isTransparent, Geodetic3D position, AltitudeMode altitudeMode, ShapeLoadListener listener, boolean deleteListener)
  {
    loadBSONSceneJS(url, DownloadPriority.MEDIUM, TimeInterval.fromDays(30), true, uriPrefix, isTransparent, position, altitudeMode, listener, deleteListener);
  }

}