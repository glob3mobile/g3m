package org.glob3.mobile.generated;
//
//  CameraEventHandler.hpp
//  G3M
//
//  Created by Agustin Trujillo Pino on 07/08/12.
//


//class RenderState;
//class G3MRenderContext;
//class CameraContext;
//class G3MEventContext;
//class TouchEvent;


public abstract class CameraEventHandler
{
  protected CameraEventHandler()
  {

  }


  public void dispose()
  {

  }

  public abstract RenderState getRenderState(G3MRenderContext rc);

  public abstract void render(G3MRenderContext rc, CameraContext cameraContext);

  public abstract boolean onTouchEvent(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext);

  public abstract void onDown(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext);

  public abstract void onMove(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext);

  public abstract void onUp(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext);

}
