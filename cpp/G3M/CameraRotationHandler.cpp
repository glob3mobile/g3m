//
//  CameraRotationHandler.cpp
//  G3M
//
//  Created by Agustin Trujillo Pino on 28/07/12.
//


#include "CameraRotationHandler.hpp"

#include "TouchEvent.hpp"
#include "Camera.hpp"
#include "ILogger.hpp"
#include "IMathUtils.hpp"
#include "G3MEventContext.hpp"
#include "Planet.hpp"
#include "Angle.hpp"
#include "CameraContext.hpp"
#include "RenderState.hpp"


RenderState CameraRotationHandler::getRenderState(const G3MRenderContext* rc) {
  return RenderState::ready();
}

bool CameraRotationHandler::onTouchEvent(const G3MEventContext *eventContext,
                                         const TouchEvent* touchEvent,
                                         CameraContext *cameraContext) {
  if (touchEvent->getTouchCount() != 3) {
    return false;
  }

  switch (touchEvent->getType()) {
    case Down:
      onDown(eventContext, *touchEvent, cameraContext);
      return true;

    case Move:
      onMove(eventContext, *touchEvent, cameraContext);
      return true;

    case Up:
      onUp(eventContext, *touchEvent, cameraContext);
      return true;

    default:
      return false;
  }

}


void CameraRotationHandler::onDown(const G3MEventContext *eventContext,
                                   const TouchEvent& touchEvent,
                                   CameraContext *cameraContext) {
  const Camera *camera = cameraContext->getNextCamera();
  camera->getLookAtParamsInto(_cameraPosition, _cameraCenter, _cameraUp);
  cameraContext->setCurrentGesture(Rotate);

  // middle pixel in 2D
  Vector2F pixel0 = touchEvent.getTouch(0)->getPos();
  Vector2F pixel1 = touchEvent.getTouch(1)->getPos();
  Vector2F pixel2 = touchEvent.getTouch(2)->getPos();
  Vector2F averagePixel = pixel0.add(pixel1).add(pixel2).div(3.0f);
  _pivotPixel.set(averagePixel._x, averagePixel._y);
  //_lastYValid = _initialPixel.y();

  // compute center of view
  //  _pivotPoint = camera->getXYZCenterOfView().asMutableVector3D();
  _pivotPoint.set(camera->getXYZCenterOfView());
  if (_pivotPoint.isNan()) {
    ILogger::instance()->logError("CAMERA ERROR: center point does not intersect globe!!\n");
    cameraContext->setCurrentGesture(None);
  }

  //printf ("down 3 fingers\n");
}


void CameraRotationHandler::onMove(const G3MEventContext *eventContext,
                                   const TouchEvent& touchEvent,
                                   CameraContext *cameraContext) {
  const IMathUtils* mu = IMathUtils::instance();

  //_currentGesture = getGesture(touchEvent);
  if (cameraContext->getCurrentGesture() != Rotate) return;

  // current middle pixel in 2D
  const Vector2F c0 = touchEvent.getTouch(0)->getPos();
  const Vector2F c1 = touchEvent.getTouch(1)->getPos();
  const Vector2F c2 = touchEvent.getTouch(2)->getPos();
  const Vector2F cm = c0.add(c1).add(c2).div(3.0f);

  // compute normal to Initial point
  Vector3D normal = eventContext->getPlanet()->geodeticSurfaceNormal(_pivotPoint );

  // vertical rotation around normal vector to globe
  Camera *camera = cameraContext->getNextCamera();
  camera->setLookAtParams(_cameraPosition, _cameraCenter, _cameraUp);
  Angle angle_v             = Angle::fromDegrees((_pivotPixel._x-cm._x)*0.25);
  camera->rotateWithAxisAndPoint(normal, _pivotPoint.asVector3D(), angle_v);

  // compute angle between normal and view direction
  Vector3D view = camera->getViewDirection();
  double dot = normal.normalized().dot(view.normalized().times(-1));
  double initialAngle = mu->acos(dot) / PI * 180;

  // rotate more than 85 degrees or less than 0 degrees is not allowed
  double delta = (cm._y - _pivotPixel._y) * 0.25;
  double finalAngle = initialAngle + delta;
  if (finalAngle > 85)  delta = 85 - initialAngle;
  if (finalAngle < 0)   delta = -initialAngle;

  // create temporal camera to test if next rotation is valid
  camera->getLookAtParamsInto(_tempCameraPosition, _tempCameraCenter, _tempCameraUp);

  // horizontal rotation over the original camera horizontal axix
  Vector3D u = camera->getHorizontalVector();
  camera->rotateWithAxisAndPoint(u, _pivotPoint.asVector3D(), Angle::fromDegrees(delta));

  // update camera only if new view intersects globe
  if (camera->getXYZCenterOfView().isNan()) {
    camera->setLookAtParams(_tempCameraPosition, _tempCameraCenter, _tempCameraUp);
  }

}


void CameraRotationHandler::onUp(const G3MEventContext *eventContext,
                                 const TouchEvent& touchEvent,
                                 CameraContext *cameraContext) {
  cameraContext->setCurrentGesture(None);
  _pivotPixel.set(MutableVector2F::zero());
}
