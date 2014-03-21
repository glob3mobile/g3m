package org.glob3.mobile.generated; 
public class CameraRenderer implements ProtoRenderer
{
  private boolean _processTouchEvents;
  private java.util.ArrayList<CameraEventHandler> _handlers = new java.util.ArrayList<CameraEventHandler>();
  private CameraContext _cameraContext;

  public CameraRenderer()
  {
     _cameraContext = null;
     _processTouchEvents = true;
  }

  public void dispose()
  {
    if (_cameraContext != null)
       _cameraContext.dispose();
    final int handlersSize = _handlers.size();
    for (int i = 0; i < handlersSize; i++)
    {
      CameraEventHandler handler = _handlers.get(i);
      if (handler != null)
         handler.dispose();
    }
  }

  public final void addHandler(CameraEventHandler handler)
  {
    _handlers.add(handler);
  }

  public final void setProcessTouchEvents(boolean processTouchEvents)
  {
    _processTouchEvents = processTouchEvents;
  }

  public final void render(G3MRenderContext rc, GLState glState)
  {
  
    // create the CameraContext
    if (_cameraContext == null)
    {
      _cameraContext = new CameraContext(Gesture.None, rc.getNextCamera());
    }
  
    // render camera object
  //  rc->getCurrentCamera()->render(rc, parentState);
  
    final int handlersSize = _handlers.size();
    for (int i = 0; i < handlersSize; i++)
    {
      _handlers.get(i).render(rc, _cameraContext);
    }
  }

  public final void initialize(G3MContext context)
  {

  }

  public final boolean onTouchEvent(G3MEventContext ec, TouchEvent touchEvent)
  {
    if (_processTouchEvents)
    {
      // abort all the camera effect currently running
      if (touchEvent.getType() == TouchEventType.Down)
      {
        EffectTarget target = _cameraContext.getNextCamera().getEffectTarget();
        ec.getEffectsScheduler().cancelAllEffectsFor(target);
      }
  
      // pass the event to all the handlers
      final int handlersSize = _handlers.size();
      for (int i = 0; i < handlersSize; i++)
      {
        if (_handlers.get(i).onTouchEvent(ec, touchEvent, _cameraContext))
        {
          return true;
        }
      }
    }
  
    // if no handler processed the event, return not-handled
    return false;
  }

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {

  }

  public final RenderState getRenderState(G3MRenderContext rc)
  {
    return RenderState.ready();
  }

  public final void start(G3MRenderContext rc)
  {

  }

  public final void stop(G3MRenderContext rc)
  {

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
}