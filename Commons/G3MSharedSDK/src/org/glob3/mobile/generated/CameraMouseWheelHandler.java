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

  public final void render(G3MRenderContext rc, CameraContext cameraContext)
  {
  }

  public final boolean onTouchEvent(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
  
    if (touchEvent.getType() != TouchEventType.MouseWheel)
    {
      return false;
    }
  
    if (touchEvent.getTouchCount() != 1)
    {
      return false;
    }
  
    final double wheelDelta = touchEvent.getMouseWheelDelta();
    if (wheelDelta == 0)
    {
      return false;
    }
  
  
    Camera camera = cameraContext.getNextCamera();
  
    final Touch touch = touchEvent.getTouch(0);
    final Vector2F pixel = touch.getPos();
  
    final Vector3D rayDir = camera.pixel2Ray(pixel);
    final Planet planet = eventContext.getPlanet();
  
    final Vector3D pos0 = camera.getCartesianPosition();
    final Vector3D p1 = planet.closestIntersection(pos0, rayDir);
  
    final double heightDelta = camera.getGeodeticHeight() * wheelDelta * _zoomSpeed;
  
    if (planet.isFlat())
    {
      final Vector3D moveDir = p1.sub(pos0).normalized();
      camera.move(moveDir, heightDelta);
    }
    else
    {
      camera.move(pos0.normalized().times(-1), heightDelta);
  
      final Vector3D p2 = planet.closestIntersection(camera.getCartesianPosition(), camera.pixel2Ray(pixel));
      final Angle angleP1P2 = Vector3D.angleBetween(p1, p2);
      final Vector3D rotAxis = p2.cross(p1);
  
      if (!rotAxis.isNan() && !angleP1P2.isNan())
      {
        final MutableMatrix44D mat = MutableMatrix44D.createGeneralRotationMatrix(angleP1P2, rotAxis, Vector3D.ZERO);
        camera.applyTransform(mat);
      }
    }
  
    return true;
  }

}