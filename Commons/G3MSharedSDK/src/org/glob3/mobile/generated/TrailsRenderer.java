package org.glob3.mobile.generated; 
public class TrailsRenderer extends DefaultRenderer
{
  private java.util.ArrayList<Trail> _trails = new java.util.ArrayList<Trail>();


  private GLState _glState;

  private void updateGLState(G3MRenderContext rc)
  {
  
    final Camera camera = rc.getCurrentCamera();
    if (_projection == null)
    {
      _projection = new ProjectionGLFeature(camera.getProjectionMatrix44D());
      _glState.addGLFeature(_projection, true);
    }
    else
    {
      _projection.setMatrix(camera.getProjectionMatrix44D());
    }
  
    if (_model == null)
    {
      _model = new ModelGLFeature(camera.getModelMatrix44D());
      _glState.addGLFeature(_model, true);
    }
    else
    {
      _model.setMatrix(camera.getModelMatrix44D());
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

  public final void removeTrail(Trail trail)
  {
     removeTrail(trail, true);
  }
  public final void removeTrail(Trail trail, boolean deleteTrail)
  {
    final int trailsCount = _trails.size();
    int foundIndex = -1;
    for (int i = 0; i < trailsCount; i++)
    {
      Trail each = _trails.get(i);
      if (trail == each)
      {
        foundIndex = i;
        break;
      }
    }
    if (foundIndex >= 0)
    {
      _trails.remove(foundIndex);
      if (deleteTrail)
      {
        if (trail != null)
           trail.dispose();
      }
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

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
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
//#define MAX_POSITIONS_PER_SEGMENT 128

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#pragma mark TrailsRenderer
