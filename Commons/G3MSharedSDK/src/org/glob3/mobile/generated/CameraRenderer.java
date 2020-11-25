package org.glob3.mobile.generated;
//
//  CameraRenderer.cpp
//  G3M
//
//  Created by Agustin Trujillo Pino on 30/07/12.
//

//
//  CameraRenderer.hpp
//  G3M
//
//  Created by Agustin Trujillo Pino on 30/07/12.
//




//class CameraEventHandler;
//class CameraContext;
//class TouchEvent;
//class RenderState;


public class CameraRenderer implements ProtoRenderer
{
  private boolean _processTouchEvents;

  private java.util.ArrayList<CameraEventHandler> _handlers = new java.util.ArrayList<CameraEventHandler>();
  private int _handlersSize;

  private java.util.ArrayList<String> _errors = new java.util.ArrayList<String>();

  private CameraContext _cameraContext;

  public CameraRenderer()
  {
     _cameraContext = null;
     _processTouchEvents = true;
     _handlersSize = 0;
  }

  public void dispose()
  {
    if (_cameraContext != null)
       _cameraContext.dispose();
    for (int i = 0; i < _handlersSize; i++)
    {
      CameraEventHandler handler = _handlers.get(i);
      if (handler != null)
         handler.dispose();
    }
  }

  public final void setProcessTouchEvents(boolean processTouchEvents)
  {
    _processTouchEvents = processTouchEvents;
  }

  public final void render(G3MRenderContext rc, GLState glState)
  {
    if (_cameraContext == null)
    {
      _cameraContext = new CameraContext(CameraEventGesture.None, rc.getNextCamera());
    }
  
    //rc->getCurrentCamera()->render(rc, parentState);
  
    for (int i = 0; i < _handlersSize; i++)
    {
      CameraEventHandler handler = _handlers.get(i);
      handler.render(rc, _cameraContext);
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
      for (int i = 0; i < _handlersSize; i++)
      {
        CameraEventHandler handler = _handlers.get(i);
        if (handler.onTouchEvent(ec, touchEvent, _cameraContext))
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
    _errors.clear();
    boolean busyFlag = false;
    boolean errorFlag = false;
  
    for (int i = 0; i < _handlersSize; i++)
    {
      CameraEventHandler handler = _handlers.get(i);
      final RenderState handlerRenderState = handler.getRenderState(rc);
  
      final RenderState_Type childRenderStateType = handlerRenderState._type;
  
      if (childRenderStateType == RenderState_Type.RENDER_ERROR)
      {
        errorFlag = true;
  
        final java.util.ArrayList<String> handlerErrors = handlerRenderState.getErrors();
        _errors.addAll(handlerErrors);
      }
      else if (childRenderStateType == RenderState_Type.RENDER_BUSY)
      {
        busyFlag = true;
      }
    }
  
    if (errorFlag)
    {
      return RenderState.error(_errors);
    }
    else if (busyFlag)
    {
      return RenderState.busy();
    }
    else
    {
      return RenderState.ready();
    }
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

  public final void removeAllHandlers(boolean deleteHandlers)
  {
    if (deleteHandlers)
    {
      for (int i = 0; i < _handlersSize; i++)
      {
        CameraEventHandler handler = _handlers.get(i);
        if (handler != null)
           handler.dispose();
      }
    }
    _handlers.clear();
    _handlersSize = 0;
  }

  public final void addHandler(CameraEventHandler handler)
  {
    _handlers.add(handler);
    _handlersSize = _handlers.size();
  }

  public final void removeHandler(CameraEventHandler handler)
  {
    if ( _handlers.remove(handler) ) {
      _handlersSize = _handlers.size();
      return;
    }
    ILogger.instance().logError("Could not remove camera handler.");
  }

}