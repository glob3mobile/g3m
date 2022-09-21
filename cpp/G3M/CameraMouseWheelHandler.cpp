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

  if (touchEvent->getType() != MouseWheel) {
    return false;
  }

  if (touchEvent->getTouchCount() != 1) {
    return false;
  }

  const double wheelDelta = touchEvent->getMouseWheelDelta();
  if (wheelDelta == 0) {
    return false;
  }
  

  Camera* camera = cameraContext->getNextCamera();

  const Touch* touch = touchEvent->getTouch(0);
  const Vector2F pixel = touch->getPos();

  const Vector3D rayDir = camera->pixel2Ray(pixel);
  const Planet& planet = *eventContext->getPlanet();

  const Vector3D pos0 = camera->getCartesianPosition();
  const Vector3D p1 = planet.closestIntersection(pos0, rayDir);

  const double heightDelta = camera->getGeodeticHeight() * wheelDelta * _zoomSpeed;

  if (planet.isFlat()) {
    const Vector3D moveDir = p1.sub(pos0).normalized();
    camera->move(moveDir, heightDelta);
  }
  else {
    camera->move(pos0.normalized().times(-1), heightDelta);

    const Vector3D p2 = planet.closestIntersection(camera->getCartesianPosition(), camera->pixel2Ray(pixel));
    const Angle angleP1P2 = Vector3D::angleBetween(p1, p2);
    const Vector3D rotAxis = p2.cross(p1);

    if (!rotAxis.isNan() && !angleP1P2.isNan()) {
      const MutableMatrix44D mat = MutableMatrix44D::createGeneralRotationMatrix(angleP1P2, rotAxis, Vector3D::ZERO);
      camera->applyTransform(mat);
    }
  }

  return true;
}
