package org.glob3.mobile.generated; 
public class CameraRenderer extends Renderer
{


  private java.util.ArrayList<CameraEventHandler > _handlers = new java.util.ArrayList<CameraEventHandler >();

  private CameraContext _cameraContext;

  public CameraRenderer()
  {
	  _cameraContext = null;
  }
  public void dispose()
  {
	  if (_cameraContext!=null)
		  if (_cameraContext != null)
			  _cameraContext.dispose();
  }

  public final void addHandler(CameraEventHandler handler)
  {
	  _handlers.add(handler);
  }

  public final void render(RenderContext rc)
  {
	// create the CameraContext
	if (_cameraContext == null)
	{
	  _cameraContext = new CameraContext(Gesture.None, rc.getNextCamera());
	}
  
	// render camera object
	rc.getCurrentCamera().render(rc);
  
	for (int i = 0; i<_handlers.size(); i++)
	{
	  _handlers.get(i).render(rc, _cameraContext);
	}
  }
  public final void initialize(InitializationContext ic)
  {
	//_logger = ic->getLogger();
	//cameraContext = new CameraContext(
  }

  public final boolean onTouchEvent(EventContext ec, TouchEvent touchEvent)
  {
	// abort all the camera effect currently running
	if (touchEvent.getType() == TouchEventType.Down)
	  ec.getEffectsScheduler().cancellAllEffectsFor((EffectTarget) _cameraContext);
  
	// pass the event to all the handlers
	for (int n = 0; n<_handlers.size(); n++)
	  if (_handlers.get(n).onTouchEvent(ec, touchEvent, _cameraContext))
		  return true;
  
	// if any of them processed the event, return false
	return false;
  }

  public final void onResizeViewportEvent(EventContext ec, int width, int height)
  {
	if (_cameraContext != null)
	{
	  _cameraContext.getNextCamera().resizeViewport(width, height);
	}
  }

  public final boolean isReadyToRender(RenderContext rc)
  {
	return true;
  }

  public final void start()
  {

  }

  public final void stop()
  {

  }


}