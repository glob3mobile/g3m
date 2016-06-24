package org.glob3.mobile.generated; 
public class MapBooOLD_ErrorRenderer extends DefaultRenderer
{
  private java.util.ArrayList<String> _errors = new java.util.ArrayList<String>();
  public MapBooOLD_ErrorRenderer()
  {
  }
  public void dispose()
  {
  }
  public final void setErrors(java.util.ArrayList<String> errors)
  {
    _errors = errors;
  }
  public final RenderState getRenderState(G3MRenderContext rc)
  {
    if (_errors.size() > 0)
    {
      return RenderState.error(_errors);
    }
    return RenderState.ready();
  }
  public final void render(G3MRenderContext rc, GLState glState)
  {
  }
  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {
  }
}