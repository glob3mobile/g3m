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

  public final void onMouseWheel(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
    ILogger.instance().logInfo("Mouse Wheel detected");
  
    Camera cam = cameraContext.getNextCamera();
    final Planet planet = eventContext.getPlanet();
  
    final Vector3D dir = cam.pixel2Ray(touchEvent.getTouch(0).getPos());
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning USE ZRENDER IN THE FUTURE
    java.util.ArrayList<Double> dists = planet.intersectionsDistances(cam.getCartesianPosition(), dir);
  
    if (dists.size() > 0) //Research other behaviours as Google Earth
    {
  
      final double delta = touchEvent.getMouseWheelDelta();
      double factor = 0.1;
      if (delta < 0)
      {
        factor *= -1;
      }
  
      double dist = dists.get(0);
      Vector3D translation = dir.normalized().times(dist * factor);
  
      cam.translateCamera(translation);
    }
  
  
  }



}