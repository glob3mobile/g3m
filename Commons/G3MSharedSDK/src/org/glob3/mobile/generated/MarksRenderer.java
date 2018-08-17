package org.glob3.mobile.generated;import java.util.*;

public class MarksRenderer extends DefaultRenderer
{
  private final boolean _readyWhenMarksReady;
  private java.util.ArrayList<Mark> _marks = new java.util.ArrayList<Mark>();

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final Camera _lastCamera;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public Camera _lastCamera = new internal();
//#endif

  private MarkTouchListener _markTouchListener;
  private boolean _autoDeleteMarkTouchListener;

  private long _downloadPriority;

  private GLState _glState;

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
  
	if (_glState.getGLFeature(GLFeatureID.GLF_VIEWPORT_EXTENT) == null)
	{
	  _glState.clearGLFeatureGroup(GLFeatureGroupName.NO_GROUP);
	  _glState.addGLFeature(new ViewportExtentGLFeature(camera, rc.getViewMode()), false);
	}
  }
  private IFloatBuffer _billboardTexCoords;
  private IFloatBuffer getBillboardTexCoords()
  {
	if (_billboardTexCoords == null)
	{
	  FloatBufferBuilderFromCartesian2D texCoor = new FloatBufferBuilderFromCartesian2D();
	  texCoor.add(1,1);
	  texCoor.add(1,0);
	  texCoor.add(0,1);
	  texCoor.add(0,0);
	  _billboardTexCoords = texCoor.create();
	}
	return _billboardTexCoords;
  }

  private boolean _renderInReverse;
  private boolean _progressiveInitialization;
  private ITimer _initializationTimer;


  public MarksRenderer(boolean readyWhenMarksReady, boolean renderInReverse)
  {
	  this(readyWhenMarksReady, renderInReverse, true);
  }
  public MarksRenderer(boolean readyWhenMarksReady)
  {
	  this(readyWhenMarksReady, false, true);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: MarksRenderer(boolean readyWhenMarksReady, boolean renderInReverse = false, boolean progressiveInitialization = true) : _readyWhenMarksReady(readyWhenMarksReady), _renderInReverse(renderInReverse), _progressiveInitialization(progressiveInitialization), _lastCamera(null), _markTouchListener(null), _autoDeleteMarkTouchListener(false), _downloadPriority(DownloadPriority::MEDIUM), _glState(new GLState()), _billboardTexCoords(null), _initializationTimer(null)
  public MarksRenderer(boolean readyWhenMarksReady, boolean renderInReverse, boolean progressiveInitialization)
  {
	  _readyWhenMarksReady = readyWhenMarksReady;
	  _renderInReverse = renderInReverse;
	  _progressiveInitialization = progressiveInitialization;
	  _lastCamera = null;
	  _markTouchListener = null;
	  _autoDeleteMarkTouchListener = false;
	  _downloadPriority = DownloadPriority.MEDIUM;
	  _glState = new GLState();
	  _billboardTexCoords = null;
	  _initializationTimer = null;
	_context = null;
  }

  public final void setRenderInReverse(boolean renderInReverse)
  {
	_renderInReverse = renderInReverse;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean getRenderInReverse() const
  public final boolean getRenderInReverse()
  {
	return _renderInReverse;
  }

  public final void setMarkTouchListener(MarkTouchListener markTouchListener, boolean autoDelete)
  {
	if (_autoDeleteMarkTouchListener)
	{
	  if (_markTouchListener != null)
		  _markTouchListener.dispose();
	}
  
	_markTouchListener = markTouchListener;
	_autoDeleteMarkTouchListener = autoDelete;
  }

  public void dispose()
  {
	if (_initializationTimer != null)
		_initializationTimer.dispose();
  
	final int marksSize = _marks.size();
	for (int i = 0; i < marksSize; i++)
	{
	  if (_marks.get(i) != null)
		  _marks.get(i).dispose();
	}
  
	if (_autoDeleteMarkTouchListener)
	{
	  if (_markTouchListener != null)
		  _markTouchListener.dispose();
	}
	_markTouchListener = null;
  
	_glState._release();
  
	if (_billboardTexCoords != null)
		_billboardTexCoords.dispose();
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  
  }

  public void onChangedContext()
  {
	final int marksSize = _marks.size();
	for (int i = 0; i < marksSize; i++)
	{
	  Mark mark = _marks.get(i);
	  mark.initialize(_context, _downloadPriority);
	}
  }

  public void render(G3MRenderContext rc, GLState glState)
  {
	final int marksSize = _marks.size();
  
	if (marksSize > 0)
	{
	  final Camera camera = rc.getCurrentCamera();
  
	  _lastCamera = camera; // Saving camera for use in onTouchEvent
  
	  MutableVector3D cameraPosition = new MutableVector3D();
	  tangible.RefObject<MutableVector3D> tempRef_cameraPosition = new tangible.RefObject<MutableVector3D>(cameraPosition);
	  camera.getCartesianPositionMutable(tempRef_cameraPosition);
	  cameraPosition = tempRef_cameraPosition.argvalue;
	  final double cameraHeight = camera.getGeodeticPosition()._height;
  
	  updateGLState(rc);
  
	  final Planet planet = rc.getPlanet();
	  GL gl = rc.getGL();
  
	  IFloatBuffer billboardTexCoord = getBillboardTexCoords();
  
  
	  if (_progressiveInitialization)
	  {
		if (_initializationTimer == null)
		{
		  _initializationTimer = rc.getFactory().createTimer();
		}
		else
		{
		  _initializationTimer.start();
		}
  
		for (int i = 0; i < marksSize; i++)
		{
		  if (_initializationTimer.elapsedTimeInMilliseconds() > 5)
		  {
			break;
		  }
  
		  final int ii = _renderInReverse ? i : (marksSize-1-i);
		  Mark mark = _marks.get(ii);
		  if (!mark.isInitialized())
		  {
			mark.initialize(_context, _downloadPriority);
		  }
		}
	  }
  
	  for (int i = 0; i < marksSize; i++)
	  {
		final int ii = _renderInReverse ? (marksSize-1-i) : i;
		Mark mark = _marks.get(ii);
		if (mark.isReady())
		{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: mark->render(rc, cameraPosition, cameraHeight, _glState, planet, gl, billboardTexCoord);
		  mark.render(rc, new MutableVector3D(cameraPosition), cameraHeight, _glState, planet, gl, billboardTexCoord);
		}
	  }
	}
  }

  public final void addMark(Mark mark)
  {
	_marks.add(mark);
	if ((_context != null) && !_progressiveInitialization)
	{
	  mark.initialize(_context, _downloadPriority);
	}
  }

  public final void removeMark(Mark mark)
  {
  //  int pos = -1;
  //  const int marksSize = _marks.size();
  //  for (int i = 0; i < marksSize; i++) {
  //    if (_marks[i] == mark) {
  //      pos = i;
  //      break;
  //    }
  //  }
  //  if (pos != -1) {
  ///#ifdef C_CODE
  //    _marks.erase(_marks.begin() + pos);
  ///#endif
  ///#ifdef JAVA_CODE
  //    _marks.remove(pos);
  ///#endif
  //  }
  
	final int marksSize = _marks.size();
	for (int i = 0; i < marksSize; i++)
	{
	  if (_marks.get(i) == mark)
	  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
//C++ TO JAVA CONVERTER TODO TASK: There is no direct equivalent to the STL vector 'erase' method in Java:
		_marks.erase(_marks.iterator() + i);
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
		_marks.remove(i);
//#endif
		break;
	  }
	}
  
  }

  public final void removeAllMarks()
  {
	  removeAllMarks(true);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: void removeAllMarks(boolean deleteMarks = true)
  public final void removeAllMarks(boolean deleteMarks)
  {
	if (deleteMarks)
	{
	  final int marksSize = _marks.size();
	  for (int i = 0; i < marksSize; i++)
	  {
		if (_marks.get(i) != null)
			_marks.get(i).dispose();
	  }
	}
	_marks.clear();
  }

  public final boolean onTouchEvent(G3MEventContext ec, TouchEvent touchEvent)
  {
  
	boolean handled = false;
	if (touchEvent.getType() == TouchEventType.DownUp)
	{
  
	  if (_lastCamera != null)
	  {
		final Vector2F touchedPixel = touchEvent.getTouch(0).getPos();
		final Planet planet = ec.getPlanet();
  
		double minSqDistance = IMathUtils.instance().maxDouble();
		Mark nearestMark = null;
  
		final int marksSize = _marks.size();
		for (int i = 0; i < marksSize; i++)
		{
		  Mark mark = _marks.get(i);
  
		  if (!mark.isReady())
		  {
			continue;
		  }
		  if (!mark.isRendered())
		  {
			continue;
		  }
  
		  final int markWidth = (int)mark.getTextureWidth();
		  if (markWidth <= 0)
		  {
			continue;
		  }
  
		  final int markHeight = (int)mark.getTextureHeight();
		  if (markHeight <= 0)
		  {
			continue;
		  }
  
		  final Vector3D cartesianMarkPosition = mark.getCartesianPosition(planet);
		  final Vector2F markPixel = _lastCamera.point2Pixel(cartesianMarkPosition);
  
		  final RectangleF markPixelBounds = new RectangleF(markPixel._x - ((float) markWidth / 2), markPixel._y - ((float) markHeight / 2), markWidth, markHeight);
  
		  if (markPixelBounds.contains(touchedPixel._x, touchedPixel._y))
		  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const double sqDistance = markPixel.squaredDistanceTo(touchedPixel);
			final double sqDistance = markPixel.squaredDistanceTo(new Vector2F(touchedPixel));
			if (sqDistance < minSqDistance)
			{
			  nearestMark = mark;
			  minSqDistance = sqDistance;
			}
		  }
		}
  
		if (nearestMark != null)
		{
		  handled = nearestMark.touched();
		  if (!handled)
		  {
			if (_markTouchListener != null)
			{
			  handled = _markTouchListener.touchedMark(nearestMark);
			}
		  }
		}
	  }
	}
  
	return handled;
  }

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {
	_glState.clearGLFeatureGroup(GLFeatureGroupName.NO_GROUP);
  
	int logicWidth = width;
	if (ec.getViewMode() == ViewMode.STEREO)
	{
	  logicWidth /= 2;
	}
  
	_glState.addGLFeature(new ViewportExtentGLFeature(logicWidth, height), false);
  }

  public final RenderState getRenderState(G3MRenderContext rc)
  {
	if (_readyWhenMarksReady)
	{
	  final int marksSize = _marks.size();
	  for (int i = 0; i < marksSize; i++)
	  {
		if (!_marks.get(i).isReady())
		{
		  return RenderState.busy();
		}
	  }
	}
  
	return RenderState.ready();
  }

  //TODO: WHY? VTP
  public final void onResume(G3MContext context)
  {
	_context = context;
  }

  /**
   Change the download-priority used by Marks (for downloading textures).

   Default value is 1000000
   */
  public final void setDownloadPriority(long downloadPriority)
  {
	_downloadPriority = downloadPriority;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: long getDownloadPriority() const
  public final long getDownloadPriority()
  {
	return _downloadPriority;
  }

  public final boolean isVisible(G3MRenderContext rc)
  {
	return true;
  }

  public final void modifiyGLState(GLState state)
  {

  }

  public final int removeAllMarks(MarksFilter filter, boolean deleteMarks)
  {
	int removed = 0;
	java.util.ArrayList<Mark> newMarks = new java.util.ArrayList<Mark>();
  
	final int marksSize = _marks.size();
	for (int i = 0; i < marksSize; i++)
	{
	  Mark mark = _marks.get(i);
	  if (filter.test(mark))
	  {
		if (deleteMarks)
		{
		  if (mark != null)
			  mark.dispose();
		}
		removed++;
	  }
	  else
	  {
		newMarks.add(mark);
	  }
	}
  
	if (removed > 0)
	{
	  _marks = newMarks;
	}
  
	return removed;
  }

}
