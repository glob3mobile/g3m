package org.glob3.mobile.generated; 
//
//  CameraMouseWheelHandler.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 10/10/14.
//
//

//
//  CameraMouseWheelHandler.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 10/10/14.
//
//



public class CameraMouseWheelHandler extends CameraEventHandler
{

  public CameraMouseWheelHandler()
  {
  }

  public void dispose()
  {
    super.dispose();

  }

  public final boolean onTouchEvent(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
  
    if (touchEvent.getType() == TouchEventType.MouseWheelChanged)
    {
      onMouseWheel(eventContext, touchEvent, cameraContext);
      return true;
    }
    return false;
  
  }

  public final void render(G3MRenderContext rc, CameraContext cameraContext)
  {
  }

  public final void onDown(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
  }

  public final void onMove(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
  }

  public final void onUp(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
  }


  /*void CameraMouseWheelHandler::onMouseWheel(const G3MEventContext *eventContext,
                                             const TouchEvent& touchEvent,
                                             CameraContext *cameraContext){
    Camera* cam = cameraContext->getNextCamera();
    const Planet* planet = eventContext->getPlanet();
    
    const Vector3D dir = cam->pixel2Ray(touchEvent.getTouch(0)->getPos()).normalized();
    
  #warning USE ZRENDER IN THE FUTURE
    std::vector<double> dists = planet->intersectionsDistances(cam->getCartesianPosition(), dir);
    
    if (dists.size() > 0){ //Research other behaviours as Google Earth
  
      const double delta = touchEvent.getMouseWheelDelta();
      double factor = 0.1;
      if (delta < 0){
        factor *= -1;
      }
      
      double dist = dists.at(0);
      Vector3D translation = dir.normalized().times(dist * factor);
      
      cam->translateCamera(translation);
    }*/
  
  
  public final void onMouseWheel(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
    Camera cam = cameraContext.getNextCamera();
    final Vector2I pixel = touchEvent.getTouch(0).getPos();
    Vector3D touchedPosition = eventContext.getWidget().getScenePositionForPixel(pixel._x, pixel._y);
  
    if (!touchedPosition.isNan())
    {
      final Vector3D dir = cam.pixel2Ray(pixel).normalized();
      double dist = touchedPosition.distanceTo(cam.getCartesianPosition());
      final double delta = touchEvent.getMouseWheelDelta();
      double factor = 0.1;
      if (delta < 0)
      {
        factor *= -1;
      }
  
      Vector3D translation = dir.normalized().times(dist * factor);
      cam.translateCamera(translation);
    }
  }



}