//
//  CameraDoubleDragHandler.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 28/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#include "CameraDoubleDragHandler.hpp"
#include "GL.hpp"
#include "TouchEvent.hpp"



bool CameraDoubleDragHandler::onTouchEvent(const G3MEventContext *eventContext,
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


void CameraDoubleDragHandler::onDown(const G3MEventContext *eventContext,
                                     const TouchEvent& touchEvent, 
                                     CameraContext *cameraContext) 
{
  Camera *camera = cameraContext->getNextCamera();
  _camera0.copyFrom(*camera);
  cameraContext->setCurrentGesture(DoubleDrag);  

  // double dragging
  Vector2I pixel0 = touchEvent.getTouch(0)->getPos();
  Vector2I pixel1 = touchEvent.getTouch(1)->getPos();

  
  
  _initialPoint0  = _camera0.pixel2PlanetPoint(pixel0).asMutableVector3D();
  _initialPoint1  = _camera0.pixel2PlanetPoint(pixel1).asMutableVector3D();

  
  
  
  
   // compute rays
   const Vector3D origin = _camera0.getCartesianPosition();
   const Vector3D ray0 = _camera0.pixel2Ray(pixel0);
   const Vector3D ray1 = _camera0.pixel2Ray(pixel1);
  _initialRaysAngle = ray0.angleBetween(ray1).degrees();
  

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
  
  //printf ("down 2 finger\n");
}


void CameraDoubleDragHandler::onMove(const G3MEventContext *eventContext,
                                     const TouchEvent& touchEvent, 
                                     CameraContext *cameraContext) {

  const IMathUtils* mu = IMathUtils::instance();

  if (cameraContext->getCurrentGesture() != DoubleDrag) return;
  if (_initialPoint.isNan()) return;
    
  Vector2I pixel0 = touchEvent.getTouch(0)->getPos();
  Vector2I pixel1 = touchEvent.getTouch(1)->getPos();    
  Vector2I difPixel = pixel1.sub(pixel0);
  MutableVector3D ray0 = _camera0.pixel2Ray(pixel0).asMutableVector3D();
  MutableVector3D ray1 = _camera0.pixel2Ray(pixel1).asMutableVector3D();
  //double finalFingerSeparation = difPixel.length();
  //double factor = finalFingerSeparation/_initialFingerSeparation;
  double finalRaysAngle = ray0.angleBetween(ray1).degrees();
  double factor = finalRaysAngle / _initialRaysAngle;
  
  
  const Planet* _planet = eventContext->getPlanet();

  const Vector3D origin = _camera0.getCartesianPosition();
  MutableVector3D positionCamera = origin.asMutableVector3D();
  MutableVector3D viewDirection = _camera0.getViewDirection().asMutableVector3D();
  MutableVector3D upDirection = _camera0.getUp().asMutableVector3D();
  Vector3D centerPoint = _planet->closestIntersection(positionCamera.asVector3D(), viewDirection.asVector3D());


  
  // compute camera translation using numerical iterations until convergence
  double dAccum=0;
  {
    //Camera tempCamera(_camera0);
    Angle originalAngle = _initialPoint0.angleBetween(_initialPoint1);
    double angle = originalAngle._degrees;
    
    // compute estimated camera translation
    //Vector3D centerPoint = tempCamera.getXYZCenterOfView();
    
    double distance = positionCamera.asVector3D().sub(centerPoint).length();
    double d = distance*(factor-1)/factor;
    //tempCamera.moveForward(d);
    MutableMatrix44D translation = MutableMatrix44D::createTranslationMatrix(viewDirection.asVector3D().normalized().times(d));
    positionCamera = positionCamera.transformedBy(translation, 1.0);
    dAccum += d;
    //double angle0 = tempCamera.compute3DAngularDistance(pixel0, pixel1)._degrees;
    double angle0;
    {
      const Vector3D point0 = _planet->closestIntersection(positionCamera.asVector3D(), ray0.asVector3D());
      if (point0.isNan()) {
        printf ("error: point0 nan!\n");
      }
      const Vector3D point1 = _planet->closestIntersection(positionCamera.asVector3D(), ray1.asVector3D());
      if (point1.isNan()) {
        printf ("error: point1 nan!\n");
      }
      angle0 = point0.angleBetween(point1)._degrees;
    }

    
    
    
    if (mu->isNan(angle0)) return;
    //printf("distancia angular original = %.4f     d=%.1f   angulo step0=%.4f\n", angle, d, angle0);
 
    // step 1
    d = mu->abs((distance-d)*0.3);
    if (angle0 < angle) d*=-1;
    //tempCamera.moveForward(d);
    translation = MutableMatrix44D::createTranslationMatrix(viewDirection.asVector3D().normalized().times(d));
    positionCamera = positionCamera.transformedBy(translation, 1.0);

    dAccum += d;
    //tempCamera.updateModelMatrix();
    //double angle1 = tempCamera.compute3DAngularDistance(pixel0, pixel1)._degrees;
    double angle1;
    {
      const Vector3D point0 = _planet->closestIntersection(positionCamera.asVector3D(), ray0.asVector3D());
      if (point0.isNan()) {
        printf ("error: point0 nan!\n");
      }
      const Vector3D point1 = _planet->closestIntersection(positionCamera.asVector3D(), ray1.asVector3D());
      if (point1.isNan()) {
        printf ("error: point1 nan!\n");
      }
      angle1 = point0.angleBetween(point1)._degrees;
    }
   
    
    
    
    
    double angle_n1=angle0, angle_n=angle1;
    
    // iterations
//    int iter=0;
    double precision = mu->pow(10, mu->log10(distance)-8.5);
    while (mu->abs(angle_n-angle) > precision) {
//      iter++;
      if ((angle_n1-angle_n)/(angle_n-angle) < 0) d*=-0.5;
      //tempCamera.moveForward(d);
      translation = MutableMatrix44D::createTranslationMatrix(viewDirection.asVector3D().normalized().times(d));
      positionCamera = positionCamera.transformedBy(translation, 1.0);

      dAccum += d;
      //tempCamera.updateModelMatrix();
      angle_n1 = angle_n;
      //angle_n = tempCamera.compute3DAngularDistance(pixel0, pixel1)._degrees;
      {
        const Vector3D point0 = _planet->closestIntersection(positionCamera.asVector3D(), ray0.asVector3D());
        if (point0.isNan()) {
          printf ("error: point0 nan!\n");
        }
        const Vector3D point1 = _planet->closestIntersection(positionCamera.asVector3D(), ray1.asVector3D());
        if (point1.isNan()) {
          printf ("error: point1 nan!\n");
        }
        angle_n = point0.angleBetween(point1)._degrees;
      }

    }
    //printf("-----------  iteraciones=%d  precision=%f angulo final=%.4f  distancia final=%.1f\n", iter, precision, angle_n, dAccum);
  }
  
  
  
  //// *********** FUNCIONA HASTA AQUI
  
  // create temp camera to test gesture first
        Camera tempCamera(_camera0);
        positionCamera = origin.asMutableVector3D();
  
  // drag from initialPoint to centerPoint
  MutableMatrix44D matrix = MutableMatrix44D::identity();
  {
    Vector3D initialPoint = _initialPoint.asVector3D();
    const Vector3D rotationAxis = initialPoint.cross(centerPoint);
    const Angle rotationDelta = Angle::fromRadians( - mu->acos(initialPoint.normalized().dot(centerPoint.normalized())) );
    if (rotationDelta.isNan()) return; 
        //tempCamera.rotateWithAxis(rotationAxis, rotationDelta);
    
    MutableMatrix44D rotation = MutableMatrix44D::createRotationMatrix(rotationDelta, rotationAxis);
    positionCamera = positionCamera.transformedBy(rotation, 1.0);
    viewDirection = viewDirection.transformedBy(rotation, 0.0);
    upDirection = upDirection.transformedBy(rotation, 0.0);
    ray0 = ray0.transformedBy(rotation, 0.0);
    ray1 = ray1.transformedBy(rotation, 0.0);
    matrix = matrix.multiply(rotation);
    tempCamera.applyTransform(rotation);
    
  }
  
  // move the camera
  if (_processZoom) {
        //tempCamera.moveForward(dAccum);
    
    MutableMatrix44D translation = MutableMatrix44D::createTranslationMatrix(viewDirection.asVector3D().normalized().times(dAccum));
    positionCamera = positionCamera.transformedBy(translation, 1.0);
    matrix = translation.multiply(matrix);
    tempCamera.applyTransform(translation);
    
  }
  
  // compute 3D point of view center
  //tempCamera.updateModelMatrix();
        //Vector3D centerPoint2 = tempCamera.getXYZCenterOfView();
        Vector3D centerPoint2 = _planet->closestIntersection(positionCamera.asVector3D(), viewDirection.asVector3D());
  
  
  // middle point in 3D
        //Vector3D P0 = tempCamera.pixel2PlanetPoint(pixel0);
        Vector3D P0 = _planet->closestIntersection(positionCamera.asVector3D(), ray0.asVector3D());
        //Vector3D P1 = tempCamera.pixel2PlanetPoint(pixel1);
        Vector3D P1 = _planet->closestIntersection(positionCamera.asVector3D(), ray1.asVector3D());
  //const Planet *planet = eventContext->getPlanet();
  Geodetic2D g = _planet->getMidPoint(_planet->toGeodetic2D(P0), _planet->toGeodetic2D(P1));
  Vector3D finalPoint = _planet->toCartesian(g);
  
  // drag globe from centerPoint to finalPoint
  const Vector3D rotationAxis = centerPoint2.cross(finalPoint);
  const Angle rotationDelta = Angle::fromRadians( - mu->acos(centerPoint2.normalized().dot(finalPoint.normalized())) );
  if (rotationDelta.isNan()) {
    return;
  }
        //tempCamera.rotateWithAxis(rotationAxis, rotationDelta);
  
  MutableMatrix44D rotation = MutableMatrix44D::createRotationMatrix(rotationDelta, rotationAxis);
  positionCamera = positionCamera.transformedBy(rotation, 1.0);
  viewDirection = viewDirection.transformedBy(rotation, 0.0);
  upDirection = upDirection.transformedBy(rotation, 0.0);
  ray0 = ray0.transformedBy(rotation, 0.0);
  ray1 = ray1.transformedBy(rotation, 0.0);
  matrix = rotation.multiply(matrix);
  tempCamera.applyTransform(rotation);

  
  
  
  
  // the gesture was valid. Copy data to final camera
  //tempCamera.updateModelMatrix();
    
  // camera rotation
  if (_processRotation) {
    Vector3D normal = _planet->geodeticSurfaceNormal(centerPoint2);
    Vector3D v0     = _initialPoint0.asVector3D().sub(centerPoint2).projectionInPlane(normal);
        //Vector3D v1     = tempCamera.pixel2PlanetPoint(pixel0).sub(centerPoint2).projectionInPlane(normal);
    
    Vector3D P0 = _planet->closestIntersection(positionCamera.asVector3D(), ray0.asVector3D());
    Vector3D v1 = P0.sub(centerPoint2).projectionInPlane(normal);

    
    double angle    = v0.angleBetween(v1)._degrees;
    double sign     = v1.cross(v0).dot(normal);
    if (sign<0) angle = -angle;
          //tempCamera.rotateWithAxisAndPoint(normal, centerPoint2, Angle::fromDegrees(angle));
    
    MutableMatrix44D rotation = MutableMatrix44D::createGeneralRotationMatrix(Angle::fromDegrees(angle), normal, centerPoint2);
    matrix = rotation.multiply(matrix);
    tempCamera.applyTransform(rotation);
    
  }
  
  // copy final transformation to camera
  //tempCamera.updateModelMatrix();
        //cameraContext->getNextCamera()->copyFrom(tempCamera);

  // apply transformation
  Camera *camera = cameraContext->getNextCamera();
  camera->copyFrom(_camera0);
  camera->applyTransform(matrix);


  //printf ("moving 2 fingers\n");
}


void CameraDoubleDragHandler::onUp(const G3MEventContext *eventContext,
                                   const TouchEvent& touchEvent, 
                                   CameraContext *cameraContext) 
{
  cameraContext->setCurrentGesture(None);
  _initialPixel = Vector3D::nan().asMutableVector3D();
  
  //printf ("end 2 fingers.  gesture=%d\n", _currentGesture);
}


void CameraDoubleDragHandler::render(const G3MRenderContext* rc,
                                     CameraContext *cameraContext)
{  
//  // TEMP TO DRAW A POINT WHERE USER PRESS
//  if (false) {
//    if (cameraContext->getCurrentGesture() == DoubleDrag) {
//      GL* gl = rc->getGL();
//      float vertices[] = { 0,0,0};
//      int indices[] = {0};
//      gl->enableVerticesPosition();
//      gl->disableTexture2D();
//      gl->disableTextures();
//      gl->vertexPointer(3, 0, vertices);
//      gl->color((float) 1, (float) 1, (float) 1, 1);
//      gl->pointSize(10);
//      gl->pushMatrix();
//      MutableMatrix44D T = MutableMatrix44D::createTranslationMatrix(_initialPoint.asVector3D());
//      gl->multMatrixf(T);
//      gl->drawPoints(1, indices);
//      gl->popMatrix();
//      
//      // draw each finger
//      gl->pointSize(60);
//      gl->pushMatrix();
//      MutableMatrix44D T0 = MutableMatrix44D::createTranslationMatrix(_initialPoint0.asVector3D());
//      gl->multMatrixf(T0);
//      gl->drawPoints(1, indices);
//      gl->popMatrix();
//      gl->pushMatrix();
//      MutableMatrix44D T1 = MutableMatrix44D::createTranslationMatrix(_initialPoint1.asVector3D());
//      gl->multMatrixf(T1);
//      gl->drawPoints(1, indices);
//      gl->popMatrix();
//      
//      
//      //Geodetic2D g = _planet->toGeodetic2D(_initialPoint.asVector3D());
//      //printf ("zoom with initial point = (%f, %f)\n", g._latitude._degrees, g._longitude._degrees);
//    }
//  }

}
