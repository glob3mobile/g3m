package org.glob3.mobile.generated; 
public class NonOverlappingMarksRenderer extends DefaultRenderer
{

  private final int _maxVisibleMarks;
  private final float _viewportMargin;
  private final int _maxConvergenceSteps;

  private java.util.ArrayList<NonOverlappingMark> _visibleMarks = new java.util.ArrayList<NonOverlappingMark>();
  private java.util.ArrayList<NonOverlappingMark> _marks = new java.util.ArrayList<NonOverlappingMark>();

  private void computeMarksToBeRendered(Camera camera, Planet planet)
  {
    _visibleMarks.clear();
  
    final Frustum frustrum = camera.getFrustumInModelCoordinates();
  
    final int marksSize = _marks.size();
    for (int i = 0; i < marksSize; i++)
    {
      NonOverlappingMark m = _marks.get(i);
  
      if (_visibleMarks.size() < _maxVisibleMarks && frustrum.contains(m.getCartesianPosition(planet)))
      {
        _visibleMarks.add(m);
      }
      else
      {
        //Resetting marks location of invisible anchors
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning Do we really need this?
        m.resetWidgetPositionVelocityAndForce();
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
  
    _connectorsGLState.addGLFeature(new ViewportExtentGLFeature((int)rc.getCurrentCamera().getViewPortWidth(), (int)rc.getCurrentCamera().getViewPortHeight()), false);
  
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
    //Draw Lines
    renderConnectorLines(rc);
  
    //Draw Anchors and Marks
    final int visibleMarksSize = _visibleMarks.size();
    for (int i = 0; i < visibleMarksSize; i++)
    {
      _visibleMarks.get(i).render(rc, glState);
    }
  }
  private void applyForces(long now, Camera camera)
  {
  
    if (_lastPositionsUpdatedTime != 0) //If not First frame
    {
  
      final int viewPortWidth = camera.getViewPortWidth();
      final int viewPortHeight = camera.getViewPortHeight();
  
      final double elapsedMS = now - _lastPositionsUpdatedTime;
  
      //Update Position based on last Forces
      final int visibleMarksSize = _visibleMarks.size();
      for (int i = 0; i < visibleMarksSize; i++)
      {
        _visibleMarks.get(i).updatePositionWithCurrentForce(elapsedMS, viewPortWidth, viewPortHeight, _viewportMargin);
      }
    }
  
    _lastPositionsUpdatedTime = now;
  }


  public NonOverlappingMarksRenderer(int maxVisibleMarks, float viewportMargin)
  {
     this(maxVisibleMarks, viewportMargin, -1);
  }
  public NonOverlappingMarksRenderer(int maxVisibleMarks)
  {
     this(maxVisibleMarks, 5, -1);
  }
  public NonOverlappingMarksRenderer(int maxVisibleMarks, float viewportMargin, int maxConvergenceSteps)
  {
     _maxVisibleMarks = maxVisibleMarks;
     _viewportMargin = viewportMargin;
     _maxConvergenceSteps = maxConvergenceSteps;
     _lastPositionsUpdatedTime = 0;
     _connectorsGLState = null;
  
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
  
    if (_maxConvergenceSteps > 0)
    {
      //Looking for convergence on _maxConvergenceSteps
      long timeStep = 40;
      applyForces(_lastPositionsUpdatedTime + timeStep, camera);
  
      int iteration = 0;
      while (marksAreMoving() && iteration < _maxConvergenceSteps)
      {
        computeForces(camera, planet);
        applyForces(_lastPositionsUpdatedTime + timeStep, camera);
        iteration++;
      }
    }
    else
    {
      //Real Time
      applyForces(rc.getFrameStartTimer().nowInMilliseconds(), camera);
    }
  
    renderMarks(rc, glState);
  }

  public final boolean onTouchEvent(G3MEventContext ec, TouchEvent touchEvent)
  {
  
    if (touchEvent.getTapCount() == 1)
    {
      final float x = touchEvent.getTouch(0).getPos()._x;
      final float y = touchEvent.getTouch(0).getPos()._y;
      final int visibleMarksSize = _visibleMarks.size();
      for (int i = 0; i < visibleMarksSize; i++)
      {
        if (_visibleMarks.get(i).onTouchEvent(x, y))
        {
          return true;
        }
      }
    }
    return false;
  }

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {
    final int marksSize = _marks.size();
    for (int i = 0; i < marksSize; i++)
    {
      _marks.get(i).onResizeViewportEvent(width, height);
    }
  }

  public final void start(G3MRenderContext rc)
  {

  }

  public final void stop(G3MRenderContext rc)
  {

  }

  public final SurfaceElevationProvider getSurfaceElevationProvider()
  {
    return null;
  }

  public final PlanetRenderer getPlanetRenderer()
  {
    return null;
  }

  public final boolean isPlanetRenderer()
  {
    return false;
  }

  public final boolean marksAreMoving()
  {
    final int visibleMarksSize = _visibleMarks.size();
    for (int i = 0; i < visibleMarksSize; i++)
    {
      if (_visibleMarks.get(i).isMoving())
      {
        //      printf("Mark %d is moving", i);
        return true;
      }
    }
    return false;
  }

}
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#pragma mark MarkWidget

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#pragma mark NonOverlappingMark

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#pragma-mark Renderer
