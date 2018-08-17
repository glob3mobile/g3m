package org.glob3.mobile.generated;import java.util.*;

public class ShapesRenderer extends DefaultRenderer
{
  private static class LoadQueueItem
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	public final URL _url = new URL();
	public final TimeInterval _timeToCache = new TimeInterval();
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	public final URL _url = new public();
	public final TimeInterval _timeToCache = new public();
//#endif
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
		_url = new URL(url);
		_priority = priority;
		_timeToCache = new TimeInterval(timeToCache);
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

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final Camera _lastCamera;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public Camera _lastCamera = new internal();
//#endif

  private GLState _glState;
  private GLState _glStateTransparent;

  private void updateGLState(Camera camera)
  {
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

  private MutableVector3D _currentCameraPosition = new MutableVector3D();

  private void requestBuffer(URL url, long priority, TimeInterval timeToCache, boolean readExpired, String uriPrefix, boolean isTransparent, Geodetic3D position, AltitudeMode altitudeMode, ShapeLoadListener listener, boolean deleteListener, boolean isBSON)
  {
  
	IDownloader downloader = _context.getDownloader();
	downloader.requestBuffer(url, priority, timeToCache, readExpired, new ShapesRenderer_SceneJSBufferDownloadListener(this, uriPrefix, isTransparent, position, altitudeMode, listener, deleteListener, _context.getThreadUtils(), isBSON), true);
  
  }


  public ShapesRenderer()
  {
	  this(true);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: ShapesRenderer(boolean renderNotReadyShapes=true) : _renderNotReadyShapes(renderNotReadyShapes), _glState(new GLState()), _glStateTransparent(new GLState()), _lastCamera(null)
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

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif

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
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
//C++ TO JAVA CONVERTER TODO TASK: There is no direct equivalent to the STL vector 'erase' method in Java:
	  _shapes.erase(_shapes.iterator() + pos);
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	  _shapes.remove(pos);
//#endif
	}
  }

  public final void removeAllShapes()
  {
	  removeAllShapes(true);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: void removeAllShapes(boolean deleteShapes =true)
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
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D direction = _lastCamera->pixel2Ray(pixel);
		final Vector3D direction = _lastCamera.pixel2Ray(new Vector2F(pixel));
		final Planet planet = ec.getPlanet();
		if (!direction.isNan())
		{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: java.util.ArrayList<ShapeDistance> shapeDistances = intersectionsDistances(planet, origin, direction);
		  java.util.ArrayList<ShapeDistance> shapeDistances = intersectionsDistances(planet, new Vector3D(origin), new Vector3D(direction));
  
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
  
	final int shapesCount = _shapes.size();
	if (shapesCount > 0)
	{
	  tangible.RefObject<MutableVector3D> tempRef__currentCameraPosition = new tangible.RefObject<MutableVector3D>(_currentCameraPosition);
	  _lastCamera.getCartesianPositionMutable(tempRef__currentCameraPosition);
	  _currentCameraPosition = tempRef__currentCameraPosition.argvalue;
  
	  //Setting camera matrixes
	  updateGLState(_lastCamera);
  
	  _glState.setParent(glState);
	  _glStateTransparent.setParent(glState);
  
  
	  for (int i = 0; i < shapesCount; i++)
	  {
		Shape shape = _shapes.get(i);
		if (shape.isEnable())
		{
		  if (shape.isTransparent(rc))
		  {
			final Planet planet = rc.getPlanet();
			final Vector3D shapePosition = planet.toCartesian(shape.getPosition());
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const double squaredDistanceFromEye = shapePosition.sub(_currentCameraPosition).squaredLength();
			final double squaredDistanceFromEye = shapePosition.sub(new MutableVector3D(_currentCameraPosition)).squaredLength();
  
			rc.addOrderedRenderable(new TransparentShapeWrapper(shape, squaredDistanceFromEye, _glStateTransparent, _renderNotReadyShapes));
		  }
		  else
		  {
			shape.render(rc, _glState, _renderNotReadyShapes);
		  }
		}
	  }
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: java.util.ArrayList<ShapeDistance> intersectionsDistances(const Planet* planet, const Vector3D& origin, const Vector3D& direction) const
  public final java.util.ArrayList<ShapeDistance> intersectionsDistances(Planet planet, Vector3D origin, Vector3D direction)
  {
	java.util.ArrayList<ShapeDistance> shapeDistances = new java.util.ArrayList<ShapeDistance>();
	for (int n = 0; n<_shapes.size(); n++)
	{
	  Shape shape = _shapes.get(n);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: java.util.ArrayList<double> distances = shape->intersectionsDistances(planet, origin, direction);
	  java.util.ArrayList<Double> distances = shape.intersectionsDistances(planet, new Vector3D(origin), new Vector3D(direction));
	  for (int i = 0; i<distances.size(); i++)
	  {
		shapeDistances.add(new ShapeDistance(distances.get(i), shape));
	  }
	}
  
	// sort vector
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	std.sort(shapeDistances.iterator(), shapeDistances.end(), GlobalMembersShapesRenderer.sortShapeDistanceObject);
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	java.util.Collections.sort(shapeDistances, new java.util.Comparator<ShapeDistance>() { Override public int compare(final ShapeDistance sd1, final ShapeDistance sd2) { return Double.compare(sd1._distance, sd2._distance); } });
//#endif
  
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
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: void loadJSONSceneJS(const URL& url, long priority, const TimeInterval& timeToCache, boolean readExpired, const String& uriPrefix, boolean isTransparent, Geodetic3D* position, AltitudeMode altitudeMode, ShapeLoadListener* listener =null, boolean deleteListener =true)
  public final void loadJSONSceneJS(URL url, long priority, TimeInterval timeToCache, boolean readExpired, String uriPrefix, boolean isTransparent, Geodetic3D position, AltitudeMode altitudeMode, ShapeLoadListener listener, boolean deleteListener)
  {
	if (_context == null)
	{
	  _loadQueue.add(new LoadQueueItem(url, priority, timeToCache, readExpired, uriPrefix, isTransparent, position, altitudeMode, listener, deleteListener, false)); // isBson
	}
	else
	{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: requestBuffer(url, priority, timeToCache, readExpired, uriPrefix, isTransparent, position, altitudeMode, listener, deleteListener, false);
	  requestBuffer(new URL(url), priority, new TimeInterval(timeToCache), readExpired, uriPrefix, isTransparent, position, altitudeMode, listener, deleteListener, false); // isBson
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
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: void loadJSONSceneJS(const URL& url, const String& uriPrefix, boolean isTransparent, Geodetic3D* position, AltitudeMode altitudeMode, ShapeLoadListener* listener=null, boolean deleteListener=true)
  public final void loadJSONSceneJS(URL url, String uriPrefix, boolean isTransparent, Geodetic3D position, AltitudeMode altitudeMode, ShapeLoadListener listener, boolean deleteListener)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: loadJSONSceneJS(url, DownloadPriority::MEDIUM, TimeInterval::fromDays(30), true, uriPrefix, isTransparent, position, altitudeMode, listener, deleteListener);
	loadJSONSceneJS(new URL(url), DownloadPriority.MEDIUM, TimeInterval.fromDays(30), true, uriPrefix, isTransparent, position, altitudeMode, listener, deleteListener);
  }

  public final void loadBSONSceneJS(URL url, long priority, TimeInterval timeToCache, boolean readExpired, String uriPrefix, boolean isTransparent, Geodetic3D position, AltitudeMode altitudeMode, ShapeLoadListener listener)
  {
	  loadBSONSceneJS(url, priority, timeToCache, readExpired, uriPrefix, isTransparent, position, altitudeMode, listener, true);
  }
  public final void loadBSONSceneJS(URL url, long priority, TimeInterval timeToCache, boolean readExpired, String uriPrefix, boolean isTransparent, Geodetic3D position, AltitudeMode altitudeMode)
  {
	  loadBSONSceneJS(url, priority, timeToCache, readExpired, uriPrefix, isTransparent, position, altitudeMode, null, true);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: void loadBSONSceneJS(const URL& url, long priority, const TimeInterval& timeToCache, boolean readExpired, const String& uriPrefix, boolean isTransparent, Geodetic3D* position, AltitudeMode altitudeMode, ShapeLoadListener* listener =null, boolean deleteListener =true)
  public final void loadBSONSceneJS(URL url, long priority, TimeInterval timeToCache, boolean readExpired, String uriPrefix, boolean isTransparent, Geodetic3D position, AltitudeMode altitudeMode, ShapeLoadListener listener, boolean deleteListener)
  {
	if (_context == null)
	{
	  _loadQueue.add(new LoadQueueItem(url, priority, timeToCache, readExpired, uriPrefix, isTransparent, position, altitudeMode, listener, deleteListener, true)); // isBson
	}
	else
	{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: requestBuffer(url, priority, timeToCache, readExpired, uriPrefix, isTransparent, position, altitudeMode, listener, deleteListener, true);
	  requestBuffer(new URL(url), priority, new TimeInterval(timeToCache), readExpired, uriPrefix, isTransparent, position, altitudeMode, listener, deleteListener, true); // isBson
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
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: void loadBSONSceneJS(const URL& url, const String& uriPrefix, boolean isTransparent, Geodetic3D* position, AltitudeMode altitudeMode, ShapeLoadListener* listener=null, boolean deleteListener=true)
  public final void loadBSONSceneJS(URL url, String uriPrefix, boolean isTransparent, Geodetic3D position, AltitudeMode altitudeMode, ShapeLoadListener listener, boolean deleteListener)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: loadBSONSceneJS(url, DownloadPriority::MEDIUM, TimeInterval::fromDays(30), true, uriPrefix, isTransparent, position, altitudeMode, listener, deleteListener);
	loadBSONSceneJS(new URL(url), DownloadPriority.MEDIUM, TimeInterval.fromDays(30), true, uriPrefix, isTransparent, position, altitudeMode, listener, deleteListener);
  }

}
