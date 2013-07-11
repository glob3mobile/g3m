//
//  CameraFlatDoubleDragHandler.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo on 11/07/13.
//
//

#include "CameraFlatDoubleDragHandler.hpp"
#include "GL.hpp"
#include "TouchEvent.hpp"



bool CameraFlatDoubleDragHandler::onTouchEvent(const G3MEventContext *eventContext,
                                           const TouchEvent* touchEvent,
                                           CameraContext *cameraContext)
{
  // only one finger needed
  if (touchEvent->getTouchCount()!=2) return false;
  
  switch (touchEvent->getType()) {
    case Down:
      onDown(eventContext, *touchEvent, cameraContext);
      break;
    case Move:
      onMove(eventContext, *touchEvent, cameraContext);
      break;
    case Up:
      onUp(eventContext, *touchEvent, cameraContext);
    default:
      break;
  }
  
  return true;
}


void CameraFlatDoubleDragHandler::onDown(const G3MEventContext *eventContext,
                                     const TouchEvent& touchEvent,
                                     CameraContext *cameraContext)
{
  Camera *camera = cameraContext->getNextCamera();
  _camera0.copyFrom(*camera);
  cameraContext->setCurrentGesture(DoubleDrag);
  
  // double dragging
  Vector3D origin = _camera0.getCartesianPosition();
  Vector2I pixel0 = touchEvent.getTouch(0)->getPos();
  _initialPoint0 = Plane::intersectionXYPlaneWithRay(origin, _camera0.pixel2Ray(pixel0)).asMutableVector3D();
  Vector2I pixel1 = touchEvent.getTouch(1)->getPos();
  _initialPoint1 = Plane::intersectionXYPlaneWithRay(origin, _camera0.pixel2Ray(pixel1)).asMutableVector3D();
  
  // both pixels must intersect globe
  if (_initialPoint0.isNan() || _initialPoint1.isNan()) {
    cameraContext->setCurrentGesture(None);
    return;
  }
  
  // middle point in 3D
  const Planet *planet = eventContext->getPlanet();
  Geodetic2D g0 = planet->toGeodetic2D(_initialPoint0.asVector3D());
  Geodetic2D g1 = planet->toGeodetic2D(_initialPoint1.asVector3D());
  Geodetic2D g  = planet->getMidPoint(g0, g1);
  _initialPoint = planet->toCartesian(g).asMutableVector3D();
  
  // fingers difference
  Vector2I difPixel = pixel1.sub(pixel0);
  _initialFingerSeparation = difPixel.length();
  _initialFingerInclination = difPixel.orientation()._radians;
  
  printf ("down 2 finger  (%.2f, %.2f)  y (%.2f, %.2f)\n", g0.latitude().degrees(), g0.longitude().degrees(),
          g1.latitude().degrees(), g1.longitude().degrees());
}


void CameraFlatDoubleDragHandler::onMove(const G3MEventContext *eventContext,
                                     const TouchEvent& touchEvent,
                                     CameraContext *cameraContext) {
  
  const IMathUtils* mu = IMathUtils::instance();
  
  if (cameraContext->getCurrentGesture() != DoubleDrag) return;
  if (_initialPoint.isNan()) return;
  
  Vector2I pixel0 = touchEvent.getTouch(0)->getPos();
  Vector2I pixel1 = touchEvent.getTouch(1)->getPos();
  Vector2I difPixel = pixel1.sub(pixel0);
  double finalFingerSeparation = difPixel.length();
  double factor = finalFingerSeparation/_initialFingerSeparation;
  
  // compute camera translation using numerical iterations until convergence
  double dAccum=0;
  {
    Camera tempCamera(_camera0);
    Angle originalAngle = _initialPoint0.angleBetween(_initialPoint1);
    double angle = originalAngle._degrees;
    
    // compute estimated camera translation
    Vector3D centerPoint = tempCamera.getXYZCenterOfView();
    double distance = tempCamera.getCartesianPosition().sub(centerPoint).length();
    double d = distance*(factor-1)/factor;
    tempCamera.moveForward(d);
    dAccum += d;
    //tempCamera.updateModelMatrix();
    double angle0 = tempCamera.compute3DAngularDistance(pixel0, pixel1)._degrees;
    if (mu->isNan(angle0)) return;
    //printf("distancia angular original = %.4f     d=%.1f   angulo step0=%.4f\n", angle, d, angle0);
    
    // step 1
    d = mu->abs((distance-d)*0.3);
    if (angle0 < angle) d*=-1;
    tempCamera.moveForward(d);
    dAccum += d;
    //tempCamera.updateModelMatrix();
    double angle1 = tempCamera.compute3DAngularDistance(pixel0, pixel1)._degrees;
    double angle_n1=angle0, angle_n=angle1;
    
    // iterations
    //    int iter=0;
    double precision = mu->pow(10, mu->log10(distance)-8.5);
    while (mu->abs(angle_n-angle) > precision) {
      //      iter++;
      if ((angle_n1-angle_n)/(angle_n-angle) < 0) d*=-0.5;
      tempCamera.moveForward(d);
      dAccum += d;
      //tempCamera.updateModelMatrix();
      angle_n1 = angle_n;
      angle_n = tempCamera.compute3DAngularDistance(pixel0, pixel1)._degrees;
    }
    //printf("-----------  iteraciones=%d  precision=%f angulo final=%.4f  distancia final=%.1f\n", iter, precision, angle_n, dAccum);
  }
  
  // create temp camera to test gesture first
  Camera tempCamera(_camera0);
  
  // computer center view point
  Vector3D centerPoint = tempCamera.getXYZCenterOfView();
  
  // drag from initialPoint to centerPoint
  {
    Vector3D initialPoint = _initialPoint.asVector3D();
    const Vector3D rotationAxis = initialPoint.cross(centerPoint);
    const Angle rotationDelta = Angle::fromRadians( - mu->acos(initialPoint.normalized().dot(centerPoint.normalized())) );
    if (rotationDelta.isNan()) return;
    tempCamera.rotateWithAxis(rotationAxis, rotationDelta);
  }
  
  // move the camera
  if (_processZoom) {
    tempCamera.moveForward(dAccum);
  }
  
  // compute 3D point of view center
  //tempCamera.updateModelMatrix();
  Vector3D centerPoint2 = tempCamera.getXYZCenterOfView();
  
  // middle point in 3D
  Vector3D P0 = tempCamera.pixel2PlanetPoint(pixel0);
  Vector3D P1 = tempCamera.pixel2PlanetPoint(pixel1);
  const Planet *planet = eventContext->getPlanet();
  Geodetic2D g = planet->getMidPoint(planet->toGeodetic2D(P0), planet->toGeodetic2D(P1));
  Vector3D finalPoint = planet->toCartesian(g);
  
  // drag globe from centerPoint to finalPoint
  const Vector3D rotationAxis = centerPoint2.cross(finalPoint);
  const Angle rotationDelta = Angle::fromRadians( - mu->acos(centerPoint2.normalized().dot(finalPoint.normalized())) );
  if (rotationDelta.isNan()) {
    return;
  }
  tempCamera.rotateWithAxis(rotationAxis, rotationDelta);
  
  // the gesture was valid. Copy data to final camera
  //tempCamera.updateModelMatrix();
  
  // camera rotation
  if (_processRotation) {
    Vector3D normal = planet->geodeticSurfaceNormal(centerPoint2);
    Vector3D v0     = _initialPoint0.asVector3D().sub(centerPoint2).projectionInPlane(normal);
    Vector3D v1     = tempCamera.pixel2PlanetPoint(pixel0).sub(centerPoint2).projectionInPlane(normal);
    double angle    = v0.angleBetween(v1)._degrees;
    double sign     = v1.cross(v0).dot(normal);
    if (sign<0) angle = -angle;
    tempCamera.rotateWithAxisAndPoint(normal, centerPoint2, Angle::fromDegrees(angle));
  }
  
  // copy final transformation to camera
  //tempCamera.updateModelMatrix();
  cameraContext->getNextCamera()->copyFrom(tempCamera);
  
  //printf ("moving 2 fingers\n");
}


void CameraFlatDoubleDragHandler::onUp(const G3MEventContext *eventContext,
                                   const TouchEvent& touchEvent,
                                   CameraContext *cameraContext)
{
  cameraContext->setCurrentGesture(None);
  _initialPixel = Vector3D::nan().asMutableVector3D();
  
  //printf ("end 2 fingers.  gesture=%d\n", _currentGesture);
}


void CameraFlatDoubleDragHandler::render(const G3MRenderContext* rc,
                                     CameraContext *cameraContext)
{
}