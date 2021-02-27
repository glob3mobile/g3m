package org.glob3.mobile.generated;
//
//  CameraMouseWheelHandler.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 16/2/21.
//

//
//  CameraMouseWheelHandler.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 16/2/21.
//



public class CameraMouseWheelHandler extends CameraEventHandler
{
  private double _zoomSpeed;

  public CameraMouseWheelHandler()
  {
     this(0.05);
  }
  public CameraMouseWheelHandler(double zoomSpeed)
  {
     _zoomSpeed = zoomSpeed;
  }

  public void dispose()
  {
  }

  public final RenderState getRenderState(G3MRenderContext rc)
  {
    return RenderState.ready();
  }

  public final boolean onTouchEvent(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
  
    final Touch touch = touchEvent.getTouch(0);
    final double wheelDelta = touch.getMouseWheelDelta();
  
    if (wheelDelta != 0)
    {
      Camera cam = cameraContext.getNextCamera();
      Vector2F pixel = touch.getPos();
  
      Vector3D rayDir = cam.pixel2Ray(pixel);
      final Planet planet = eventContext.getPlanet();
  
      Vector3D pos0 = cam.getCartesianPosition();
      Vector3D p1 = planet.closestIntersection(pos0, rayDir);
  
      final double heightDelta = cam.getGeodeticHeight() * wheelDelta * _zoomSpeed;
  
      if (planet.isFlat())
      {
        Vector3D moveDir = p1.sub(pos0).normalized();
        cam.move(moveDir, heightDelta);
      }
      else
      {
        cam.move(pos0.normalized().times(-1), heightDelta);
  
        Vector3D p2 = planet.closestIntersection(cam.getCartesianPosition(), cam.pixel2Ray(pixel));
        Angle angleP1P2 = Vector3D.angleBetween(p1, p2);
        Vector3D rotAxis = p2.cross(p1);
  
        if (!rotAxis.isNan() && !angleP1P2.isNan())
        {
          MutableMatrix44D mat = MutableMatrix44D.createGeneralRotationMatrix(angleP1P2, rotAxis, Vector3D.ZERO);
          cam.applyTransform(mat);
        }
      }
  
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
}