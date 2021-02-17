//
//  CameraMouseWheelHandler.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 16/2/21.
//

#include "CameraMouseWheelHandler.hpp"


#include "TouchEvent.hpp"
#include "Vector3D.hpp"
#include "CameraContext.hpp"
#include "Camera.hpp"
#include "G3MEventContext.hpp"
#include "Planet.hpp"
#include "Angle.hpp"
#include "MutableMatrix44D.hpp"

bool CameraMouseWheelHandler::onTouchEvent(const G3MEventContext *eventContext,
                                           const TouchEvent* touchEvent,
                                           CameraContext *cameraContext){
  
  const Touch* touch = touchEvent->getTouch(0);
  const double wheelDelta = touch->getMouseWheelDelta();

  if (wheelDelta != 0) {
    Camera& cam = *cameraContext->getNextCamera();
    Vector2F pixel = touch->getPos();
    
    Vector3D rayDir = cam.pixel2Ray(pixel);
    const Planet& planet = *eventContext->getPlanet();
    
    Vector3D pos0 = cam.getCartesianPosition();
    Vector3D p1 = planet.closestIntersection(pos0, rayDir);
    
    const double heightDelta = cam.getGeodeticHeight() * wheelDelta * _zoomSpeed;
    
    if (planet.isFlat()) {
      Vector3D moveDir = p1.sub(pos0).normalized();
      cam.move(moveDir, heightDelta);
    }
    else {
      cam.move(pos0.normalized().times(-1), heightDelta);
      
      Vector3D p2 = planet.closestIntersection(cam.getCartesianPosition(), cam.pixel2Ray(pixel));
      Angle angleP1P2 = Vector3D::angleBetween(p1, p2);
      Vector3D rotAxis = p2.cross(p1);
      
      if (!rotAxis.isNan() && !angleP1P2.isNan()) {
        MutableMatrix44D mat = MutableMatrix44D::createGeneralRotationMatrix(angleP1P2, rotAxis, Vector3D::ZERO);
        cam.applyTransform(mat);
      }
    }

    return true;
  }
  
  return false;
}
