package org.glob3.mobile.generated; 
public class TrailsRenderer extends LeafRenderer
{
  private java.util.ArrayList<Trail> _trails = new java.util.ArrayList<Trail>();


  private GLState _glState;

  private void updateGLState(G3MRenderContext rc)
  {
  
    final Camera cam = rc.getCurrentCamera();
    if (_projection == null)
    {
      _projection = new ProjectionGLFeature(cam.getProjectionMatrix44D());
      _glState.addGLFeature(_projection, true);
    }
    else
    {
      _projection.setMatrix(cam.getProjectionMatrix44D());
    }
  
    if (_model == null)
    {
      _model = new ModelGLFeature(cam.getModelMatrix44D());
      _glState.addGLFeature(_model, true);
    }
    else
    {
      _model.setMatrix(cam.getModelMatrix44D());
    }
  }
  private ProjectionGLFeature _projection;
  private ModelGLFeature _model;

  public TrailsRenderer()
  {
     _projection = null;
     _model = null;
     _glState = new GLState();
  }

  public final void addTrail(Trail trail)
  {
    if (trail != null)
    {
      _trails.add(trail);
    }
  }

  public void dispose()
  {
    final int trailsCount = _trails.size();
    for (int i = 0; i < trailsCount; i++)
    {
      Trail trail = _trails.get(i);
      if (trail != null)
         trail.dispose();
    }
    _trails.clear();
  
    _glState._release();
  }

  public final void onResume(G3MContext context)
  {

  }

  public final void onPause(G3MContext context)
  {

  }

  public final void onDestroy(G3MContext context)
  {

  }

  public final void initialize(G3MContext context)
  {
  }

  public final boolean isReadyToRender(G3MRenderContext rc)
  {
    return true;
  }

  public final boolean onTouchEvent(G3MEventContext ec, TouchEvent touchEvent)
  {
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
    final int trailsCount = _trails.size();
    final Frustum frustum = rc.getCurrentCamera().getFrustumInModelCoordinates();
    updateGLState(rc);
    for (int i = 0; i < trailsCount; i++)
    {
      Trail trail = _trails.get(i);
      if (trail != null)
      {
        trail.render(rc, frustum, _glState);
      }
    }
  }

}
//#define MAX_POSITIONS_PER_SEGMENT 64

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#pragma mark TrailsRenderer

