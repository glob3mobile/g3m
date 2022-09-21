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
  private final String _name;

  protected CameraEventHandler(String name)
  {
     _name = name;

  }


  public final String name()
  {
    return _name;
  }

  public void dispose()
  {

  }

  public abstract RenderState getRenderState(G3MRenderContext rc);

  public abstract void render(G3MRenderContext rc, CameraContext cameraContext);

  public abstract boolean onTouchEvent(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext);

}