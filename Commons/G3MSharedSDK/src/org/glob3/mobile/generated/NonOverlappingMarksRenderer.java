package org.glob3.mobile.generated; 
public class NonOverlappingMarksRenderer extends DefaultRenderer
{
  private final int _maxVisibleMarks;
  private final float _viewportMargin;

  private java.util.ArrayList<NonOverlappingMark> _marks = new java.util.ArrayList<NonOverlappingMark>();

  private java.util.ArrayList<NonOverlappingMark> _visibleMarks = new java.util.ArrayList<NonOverlappingMark>();
  private IStringBuilder _visibleMarksIDsBuilder;
  private String _visibleMarksIDs;

  private java.util.ArrayList<NonOverlappingMarksVisibilityListener> _visibilityListeners = new java.util.ArrayList<NonOverlappingMarksVisibilityListener>();

  private NonOverlappingMarkTouchListener _touchListener;

  private void computeMarksToBeRendered(Camera camera, Planet planet)
  {
    _visibleMarks.clear();
    _visibleMarksIDsBuilder.clear();
  
    final Frustum frustrum = camera.getFrustumInModelCoordinates();
  
    final int marksSize = _marks.size();
    for (int i = 0; i < marksSize; i++)
    {
      NonOverlappingMark m = _marks.get(i);
  
      if (_visibleMarks.size() < _maxVisibleMarks && frustrum.contains(m.getCartesianPosition(planet)))
      {
        _visibleMarks.add(m);
  
        _visibleMarksIDsBuilder.addInt(i);
        _visibleMarksIDsBuilder.addString("/");
      }
      else
      {
        //Resetting marks location of invisible anchors
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning Do we really need this?
        m.resetWidgetPositionVelocityAndForce();
      }
    }
  
    final String currentVisibleMarksIDs = _visibleMarksIDsBuilder.getString();
    if (!_visibleMarksIDs.equals(currentVisibleMarksIDs))
    {
      _visibleMarksIDs = currentVisibleMarksIDs;
      for (int i = 0; i < _visibilityListeners.size(); i++)
      {
        _visibilityListeners.get(i).onVisibilityChange(_visibleMarks);
      }
    }
  }

  private long _lastPositionsUpdatedTime;

  private GLState _connectorsGLState;
  private void renderConnectorLines(G3MRenderContext rc)
  {
    if (_connectorsGLState == null)
    {
      _connectorsGLState = new GLState();
  
      _connectorsGLState.addGLFeature(new FlatColorGLFeature(Color.black()), false);
    }
  
    _connectorsGLState.clearGLFeatureGroup(GLFeatureGroupName.NO_GROUP);
  
    FloatBufferBuilderFromCartesian2D pos2D = new FloatBufferBuilderFromCartesian2D();
  
    final int visibleMarksSize = _visibleMarks.size();
    for (int i = 0; i < visibleMarksSize; i++)
    {
      Vector2F sp = _visibleMarks.get(i).getScreenPos();
      Vector2F asp = _visibleMarks.get(i).getAnchorScreenPos();
  
      pos2D.add(sp._x, -sp._y);
      pos2D.add(asp._x, -asp._y);
    }
  
    _connectorsGLState.addGLFeature(new Geometry2DGLFeature(pos2D.create(), 2, 0, true, 0, 3.0f, true, 10.0f, Vector2F.zero()), false);
  
    _connectorsGLState.addGLFeature(new ViewportExtentGLFeature(rc.getCurrentCamera().getViewPortWidth(), rc.getCurrentCamera().getViewPortHeight()), false);
  
    rc.getGL().drawArrays(GLPrimitive.lines(), 0, pos2D.size()/2, _connectorsGLState, rc.getGPUProgramManager());
  }

  private void computeForces(Camera cam, Planet planet)
  {
    final int visibleMarksSize = _visibleMarks.size();
  
    //Compute Mark Anchor Screen Positions
    for (int i = 0; i < visibleMarksSize; i++)
    {
      _visibleMarks.get(i).computeAnchorScreenPos(cam, planet);
    }
  
    //Compute Mark Forces
    for (int i = 0; i < visibleMarksSize; i++)
    {
      NonOverlappingMark mark = _visibleMarks.get(i);
      mark.applyHookesLaw();
  
      for (int j = i+1; j < visibleMarksSize; j++)
      {
        mark.applyCoulombsLaw(_visibleMarks.get(j));
      }
  
      for (int j = 0; j < visibleMarksSize; j++)
      {
        if (i != j)
        {
          mark.applyCoulombsLawFromAnchor(_visibleMarks.get(j));
        }
      }
    }
  }
  private void renderMarks(G3MRenderContext rc, GLState glState)
  {
    // Draw Lines
    renderConnectorLines(rc);
  
    final int visibleMarksSize = _visibleMarks.size();
  
    // draw all the widgets in a shot to avoid OpenGL state changes
    for (int i = 0; i < visibleMarksSize; i++)
    {
      _visibleMarks.get(i).renderWidget(rc, glState);
    }
  
    // draw all the anchorwidgets in a shot to avoid OpenGL state changes
    for (int i = 0; i < visibleMarksSize; i++)
    {
      _visibleMarks.get(i).renderAnchorWidget(rc, glState);
    }
  }
  private void applyForces(long now, Camera camera)
  {
  
    if (_lastPositionsUpdatedTime != 0) //If not First frame
    {
  
      final int viewPortWidth = camera.getViewPortWidth();
      final int viewPortHeight = camera.getViewPortHeight();
  
      final double elapsedMS = now - _lastPositionsUpdatedTime;
      final float timeInSeconds = (float)(elapsedMS / 1000.0);
  
      //Update Position based on last Forces
      final int visibleMarksSize = _visibleMarks.size();
      for (int i = 0; i < visibleMarksSize; i++)
      {
        _visibleMarks.get(i).updatePositionWithCurrentForce(timeInSeconds, viewPortWidth, viewPortHeight, _viewportMargin);
      }
    }
  
    _lastPositionsUpdatedTime = now;
  }


  public NonOverlappingMarksRenderer(int maxVisibleMarks)
  {
     this(maxVisibleMarks, 5);
  }
  public NonOverlappingMarksRenderer(int maxVisibleMarks, float viewportMargin)
  {
     _maxVisibleMarks = maxVisibleMarks;
     _viewportMargin = viewportMargin;
     _lastPositionsUpdatedTime = 0;
     _connectorsGLState = null;
     _visibleMarksIDsBuilder = IStringBuilder.newStringBuilder();
     _visibleMarksIDs = "";
     _touchListener = null;
  
  }

  public void dispose()
  {
    _connectorsGLState._release();
  
    final int marksSize = _marks.size();
    for (int i = 0; i < marksSize; i++)
    {
      if (_marks.get(i) != null)
         _marks.get(i).dispose();
    }
  
    for (int i = 0; i < _visibilityListeners.size(); i++)
    {
      if (_visibilityListeners.get(i) != null)
         _visibilityListeners.get(i).dispose();
    }
  
    if (_visibleMarksIDsBuilder != null)
       _visibleMarksIDsBuilder.dispose();
  }

  public final void addMark(NonOverlappingMark mark)
  {
    _marks.add(mark);
  }

  public final void removeAllMarks()
  {
    final int marksSize = _marks.size();
    for (int i = 0; i < marksSize; i++)
    {
      if (_marks.get(i) != null)
         _marks.get(i).dispose();
    }
    _marks.clear();
  }

  public final void addVisibilityListener(NonOverlappingMarksVisibilityListener listener)
  {
    _visibilityListeners.add(listener);
  }

  public final void removeAllListeners()
  {
    for (int i = 0; i < _visibilityListeners.size(); i++)
    {
      if (_visibilityListeners.get(i) != null)
         _visibilityListeners.get(i).dispose();
    }
    _visibilityListeners.clear();
  }

  public final RenderState getRenderState(G3MRenderContext rc)
  {
    return RenderState.ready();
  }

  public final void render(G3MRenderContext rc, GLState glState)
  {
    final Camera camera = rc.getCurrentCamera();
    final Planet planet = rc.getPlanet();
  
    computeMarksToBeRendered(camera, planet);
    computeForces(camera, planet);
    applyForces(rc.getFrameStartTimer().nowInMilliseconds(), camera);
  
    renderMarks(rc, glState);
  }

  public final boolean onTouchEvent(G3MEventContext ec, TouchEvent touchEvent)
  {
    boolean handled = false;
  
    if (touchEvent.getType() == TouchEventType.DownUp)
    {
      final Vector2F touchedPixel = touchEvent.getTouch(0).getPos();
  
      double minSqDistance = IMathUtils.instance().maxDouble();
      NonOverlappingMark nearestMark = null;
  
      final int visibleMarksSize = _visibleMarks.size();
      for (int i = 0; i < visibleMarksSize; i++)
      {
        NonOverlappingMark mark = _visibleMarks.get(i);
  
        final int markWidth = mark.getWidth();
        if (markWidth <= 0)
        {
          continue;
        }
  
        final int markHeight = mark.getHeight();
        if (markHeight <= 0)
        {
          continue;
        }
  
        final Vector2F markPixel = mark.getScreenPos();
  
        final RectangleF markPixelBounds = new RectangleF(markPixel._x - ((float) markWidth / 2), markPixel._y - ((float) markHeight / 2), markWidth, markHeight);
  
        if (markPixelBounds.contains(touchedPixel._x, touchedPixel._y))
        {
          final double sqDistance = markPixel.squaredDistanceTo(touchedPixel);
          if (sqDistance < minSqDistance)
          {
            nearestMark = mark;
            minSqDistance = sqDistance;
          }
        }
      }
  
      if (nearestMark != null)
      {
        handled = nearestMark.onTouchEvent(touchedPixel);
        if (!handled)
        {
          if (_touchListener != null)
          {
            handled = _touchListener.touchedMark(nearestMark, touchedPixel);
          }
        }
      }
  
    }
  
    return handled;
  }

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {
    final int marksSize = _marks.size();
    for (int i = 0; i < marksSize; i++)
    {
      _marks.get(i).onResizeViewportEvent(width, height);
    }
  }

  public final void setTouchListener(NonOverlappingMarkTouchListener touchListener)
  {
    if (_touchListener != null && _touchListener != touchListener)
    {
      if (_touchListener != null)
         _touchListener.dispose();
    }
    _touchListener = touchListener;
  }

}