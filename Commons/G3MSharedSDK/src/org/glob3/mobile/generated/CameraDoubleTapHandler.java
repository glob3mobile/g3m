package org.glob3.mobile.generated;
//
//  CameraDoubleTapHandler.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 07/08/12.
//


//
//  CameraDoubleTapHandler.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 07/08/12.
//




public class CameraDoubleTapHandler extends CameraEventHandler
{


  public void dispose()
  {
    super.dispose();
  }

  public final boolean onTouchEvent(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
    // only one finger needed
    if (touchEvent.getTouchCount() != 1)
    {
      return false;
    }
    if (touchEvent.getTapCount() != 2)
    {
      return false;
    }
    if (touchEvent.getType() != TouchEventType.Down)
    {
      return false;
    }
  
    onDown(eventContext, touchEvent, cameraContext);
    return true;
  }

  public final void render(G3MRenderContext rc, CameraContext cameraContext)
  {

  }

  public final void onDown(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
    final Vector2F pixel = touchEvent.getTouch(0).getPos();
    final Planet planet = eventContext.getPlanet();
    final Camera camera = cameraContext.getNextCamera();
    Effect effect = planet.createDoubleTapEffect(camera.getCartesianPosition(), camera.getViewDirection(), camera.pixel2Ray(pixel));
  
    if (effect != null)
    {
      EffectTarget target = cameraContext.getNextCamera().getEffectTarget();
      eventContext.getEffectsScheduler().startEffect(effect, target);
    }
  }
  public final void onMove(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
  }
  public final void onUp(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
  }

}
