package org.glob3.mobile.generated; 
public class TrailsRenderer extends LeafRenderer
{
  private java.util.ArrayList<Trail> _trails = new java.util.ArrayList<Trail>();

  public TrailsRenderer()
  {
  }

  public final void addTrail(Trail trail)
  {
	_trails.add(trail);
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
  }

  public final void onResume(InitializationContext ic)
  {

  }

  public final void onPause(InitializationContext ic)
  {

  }

  public final void initialize(InitializationContext ic)
  {

  }

  public final boolean isReadyToRender(RenderContext rc)
  {
	return true;
  }

  public final boolean onTouchEvent(EventContext ec, TouchEvent touchEvent)
  {
	return false;
  }

  public final void onResizeViewportEvent(EventContext ec, int width, int height)
  {

  }

  public final void start()
  {

  }

  public final void stop()
  {

  }

  public final void render(RenderContext rc)
  {
	final int trailsCount = _trails.size();
	for (int i = 0; i < trailsCount; i++)
	{
	  Trail trail = _trails.get(i);
	  trail.render(rc);
	}
  }

}