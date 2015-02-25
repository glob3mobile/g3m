package org.glob3.mobile.generated; 
//
//  CameraDoubleTapHandler.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 07/08/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//


//
//  CameraDoubleTapHandler.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 07/08/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
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
    if (touchEvent.getTouchCount()!=1)
       return false;
    if (touchEvent.getTapCount()!=2)
       return false;
    if (touchEvent.getType()!=TouchEventType.Down)
       return false;
  
    onDown(eventContext, touchEvent, cameraContext);
    return true;
  }

  public final void render(G3MRenderContext rc, CameraContext cameraContext)
  {

  }

  public final void onDown(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
  /* // compute globe point where user tapped
    const Vector2I pixel = touchEvent.getTouch(0)->getPos();
    Camera* camera = cameraContext->getNextCamera();
    const Vector3D initialPoint = camera->pixel2PlanetPoint(pixel);
    if (initialPoint.isNan()) return;
  
    // compute central point of view
    const Vector3D centerPoint = camera->getXYZCenterOfView();
  
    // compute drag parameters
    const Vector3D axis = initialPoint.cross(centerPoint);
    const Angle angle   = Angle::fromRadians(- IMathUtils::instance()->asin(axis.length()/initialPoint.length()/centerPoint.length()));
  
    // compute zoom factor
    const double height   = camera->getGeodeticPosition()._height;
    const double distance = height * 0.6;
  
    // create effect
    Effect* effect = new DoubleTapEffect(TimeInterval::fromSeconds(0.75), axis, angle, distance);
    */
  
    final Vector2F pixel = touchEvent.getTouch(0).getPos();
    final Planet planet = eventContext.getPlanet();
    Camera camera = cameraContext.getNextCamera();
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