//
//  CameraRenderer.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 04/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "CameraRenderer.hpp"

#include "Camera.hpp"

CameraRenderer::CameraRenderer():
_camera0(NULL, 0,0),
_initialPoint(0,0,0),
_currentGesture(None),
_camera(NULL),
_initialPixel(0,0,0)
{
}

void CameraRenderer::initialize(const InitializationContext* ic){
  _logger = ic->getLogger();
}

int CameraRenderer::render(const RenderContext* rc) {
  _camera = rc->getCamera(); //Saving camera reference 
  _planet = rc->getPlanet();
  gl = rc->getGL();
  
  _camera->render(*rc);
  
  // TEMP TO DRAW A POINT WHERE USER PRESS
  if (true) if (_currentGesture==Zoom) {
    float vertices[] = { 0,0,0};
    unsigned int indices[] = {0,1};
    gl->enableVerticesPosition();
    gl->disableTexture2D();
    gl->disableTextures();
    gl->vertexPointer(3, 0, vertices);
    gl->color((float) 1, (float) 1, (float) 1, 1);
    gl->pushMatrix();
    MutableMatrix44D T = MutableMatrix44D::createTranslationMatrix(_initialPoint.asVector3D().times(1.01));
    gl->multMatrixf(T);
    gl->drawPoints(1, indices);
    gl->popMatrix();
    
    Geodetic2D g = _planet->toGeodetic2D(_initialPoint.asVector3D());
    //printf ("zoom with initial point = (%f, %f)\n", g.latitude().degrees(), g.longitude().degrees());
  }

  
  return MAX_TIME_TO_RENDER;
}

void CameraRenderer::onResizeViewportEvent(int width, int height) {
  if (_camera != NULL) {
    _camera->resizeViewport(width, height);
  }
}

void CameraRenderer::onDown(const TouchEvent& touchEvent) {
  //Saving Camera0
  _camera0 = Camera(*_camera);
  
  //Initial Point for interaction
  MutableVector2D pixel = touchEvent.getTouch(0)->getPos().asMutableVector2D();
      
  const Vector3D ray = _camera0.pixel2Ray(pixel.asVector2D());
  _initialPoint = _planet->closestIntersection(_camera0.getPosition(), ray).asMutableVector3D();
  _currentGesture = Drag; //Dragging
}

void CameraRenderer::makeDrag(const TouchEvent& touchEvent) {
  if (!_initialPoint.isNan()) { 
    //VALID INITIAL POINT
    
    const Vector2D pixel = touchEvent.getTouch(0)->getPos();
    const Vector3D ray = _camera0.pixel2Ray(pixel);
    const Vector3D pos = _camera0.getPosition();
    
    MutableVector3D finalPoint = _planet->closestIntersection(pos, ray).asMutableVector3D();
    if (finalPoint.isNan()) {
      //INVALID FINAL POINT
      finalPoint = _planet->closestPointToSphere(pos, ray).asMutableVector3D();
    }
    
    _camera->copyFrom(_camera0);
    _camera->dragCamera(_initialPoint.asVector3D(), finalPoint.asVector3D());
  }
}

void CameraRenderer::makeDoubleDrag(const TouchEvent& touchEvent) {
  
  int __agustin_at_work;
  
  if (!_initialPoint.isNan()) {
        
    Vector2D pixel0 = touchEvent.getTouch(0)->getPos();
    Vector2D pixel1 = touchEvent.getTouch(1)->getPos();    
    Vector2D difPixel = pixel1.sub(pixel0);
    double finalFingerSeparation = difPixel.length();
    double angle = difPixel.orientation().radians() - _initialFingerInclination;
    double factor = finalFingerSeparation/_initialFingerSeparation;
    
    _camera->copyFrom(_camera0);
    //_camera->dragCameraWith2Fingers(_initialPoint.asVector3D(), centerP, finalPoint, finalFingerSeparation/_initialFingerSeparation);
    
    // computer center view point
    Vector2D centerPixel(_camera->getWidth()*0.5, _camera->getHeight()*0.5);
    Vector3D centerRay = _camera->pixel2Ray(centerPixel);
    Vector3D centerPoint = _planet->closestIntersection(_camera->getPosition(), centerRay);
    
    // rotate globe from initialPoint to centerPoing
    {
      Vector3D initialPoint = _initialPoint.asVector3D();
      const Vector3D rotationAxis = initialPoint.cross(centerPoint);
      const Angle rotationDelta = Angle::fromRadians( - acos(initialPoint.normalized().dot(centerPoint.normalized())) );
      if (rotationDelta.isNan()) return; 
      _camera->rotateWithAxis(rotationAxis, rotationDelta);  
    }
    
    // move the camara 
    {
      double distance = _camera->getPosition().sub(centerPoint).length();
      _camera->moveForward(distance*(factor-1)/factor);
      
      Geodetic3D g = _planet->toGeodetic3D(_camera->getPosition());
      printf ("camera height = %f\n", g.height());
    }
    
    // rotate the camera
    {
      _camera->rotateWithAxis(_camera->getCenter().sub(_camera->getPosition()), Angle::fromRadians(-angle));
    }
    
    // detect new final point
    {
      _camera->updateModelMatrix();
      
      // compute 3D point of view center
      Vector2D centerPixel(_camera->getWidth()*0.5, _camera->getHeight()*0.5);
      Vector3D centerRay = _camera->pixel2Ray(centerPixel);
      Vector3D centerPoint = _planet->closestIntersection(_camera->getPosition(), centerRay);
      
      // middle point in 3D
       Vector3D ray0 = _camera->pixel2Ray(pixel0);
       Vector3D P0 = _planet->closestIntersection(_camera->getPosition(), ray0);
       Vector3D ray1 = _camera->pixel2Ray(pixel1);
       Vector3D P1 = _planet->closestIntersection(_camera->getPosition(), ray1);
       Geodetic2D g = _planet->getMidPoint(_planet->toGeodetic2D(P0), _planet->toGeodetic2D(P1));
       Vector3D finalPoint = _planet->toVector3D(g);     

      /*
      // middle point in 2D
      Vector2D averagePixel = pixel0.add(pixel1).div(2);
      Vector3D ray = _camera->pixel2Ray(averagePixel);
      Vector3D finalPoint = _planet->closestIntersection(_camera->getPosition(), ray);*/
      
      // rotate globe from centerPoint to finalPoint
      const Vector3D rotationAxis = centerPoint.cross(finalPoint);
      const Angle rotationDelta = Angle::fromRadians( - acos(centerPoint.normalized().dot(finalPoint.normalized())) );
      if (rotationDelta.isNan()) return; 
      _camera->rotateWithAxis(rotationAxis, rotationDelta);  
    }

  }
}


void CameraRenderer::makeZoom(const TouchEvent& touchEvent) {
  const Vector2D pixel0 = touchEvent.getTouch(0)->getPos();
  const Vector2D pixel1 = touchEvent.getTouch(1)->getPos();
  const Vector2D pixelCenter = pixel0.add(pixel1).div(2.0);
  
  const Vector3D ray = _camera0.pixel2Ray(pixelCenter);
  _initialPoint = _planet->closestIntersection(_camera0.getPosition(), ray).asMutableVector3D();
  
  const Vector2D centerOfViewport(_camera0.getWidth() / 2, _camera0.getHeight() / 2);
  const Vector3D ray2 = _camera0.pixel2Ray(centerOfViewport);
  const Vector3D pointInCenterOfView = _planet->closestIntersection(_camera0.getPosition(), ray2);
  
  //IF CENTER PIXEL INTERSECTS THE PLANET
  if (!_initialPoint.isNan()){
    //IF THE CENTER OF THE VIEW INTERSECTS THE PLANET
    if (!pointInCenterOfView.isNan()){
      const Vector2D prevPixel0 = touchEvent.getTouch(0)->getPrevPos();
      const Vector2D prevPixel1 = touchEvent.getTouch(1)->getPrevPos();
      
      const double dist = pixel0.sub(pixel1).length();
      const double prevDist = prevPixel0.sub(prevPixel1).length();
      
      const Vector2D pixelDelta = pixel1.sub(pixel0);
      const Vector2D prevPixelDelta = prevPixel1.sub(prevPixel0);
      
      const Angle angle = pixelDelta.angle();
      const Angle prevAngle = prevPixelDelta.angle();
      
      //We rotate and zoom the camera with the same gesture
      _camera->zoom(prevDist /dist);
      _camera->pivotOnCenter(angle.sub(prevAngle));
    }
  }
}



Gesture CameraRenderer::getGesture(const TouchEvent& touchEvent) {
  int n = touchEvent.getNumTouch();
  if (n == 1) {
    //Dragging
    if (_currentGesture == Drag) {
      return Drag;
    }
    else {
      return None;
    }
  }
  
  if (n== 2){
    
    // if it's the first movement, init zoom with the middle 3D point
    if (_currentGesture != Zoom) {
      Vector2D pixel0 = touchEvent.getTouch(0)->getPos();
      Vector2D pixel1 = touchEvent.getTouch(1)->getPos();
      
      // middle point in 3D
      Vector3D ray0 = _camera0.pixel2Ray(pixel0);
      Vector3D P0 = _planet->closestIntersection(_camera0.getPosition(), ray0);
      Vector3D ray1 = _camera0.pixel2Ray(pixel1);
      Vector3D P1 = _planet->closestIntersection(_camera0.getPosition(), ray1);
      Geodetic2D g = _planet->getMidPoint(_planet->toGeodetic2D(P0), _planet->toGeodetic2D(P1));
      _initialPoint = _planet->toVector3D(g).asMutableVector3D();
      
      /*
      // middle point in 2D
      Vector2D averagePixel = pixel0.add(pixel1).div(2);
      Vector3D ray = _camera0.pixel2Ray(averagePixel);
      _initialPoint = _planet->closestIntersection(_camera0.getPosition(), ray).asMutableVector3D();*/
      
      Vector2D difPixel = pixel1.sub(pixel0);
      _initialFingerSeparation = difPixel.length();
      _initialFingerInclination = difPixel.orientation().radians();
      
    }
    return Zoom;
    
    
    
/*    //If the gesture is set we don't have to change it
    if (_currentGesture == Zoom) return Zoom;
    if (_currentGesture == Rotate) return Rotate;
    
    //We have to fingers and the previous event was Drag
    const Vector2D pixel0 = touchEvent.getTouch(0)->getPos();
    const Vector2D pixel1 = touchEvent.getTouch(1)->getPos();
    
    const Vector2D prevPixel0 = touchEvent.getTouch(0)->getPrevPos();
    const Vector2D prevPixel1 = touchEvent.getTouch(1)->getPrevPos();
    
    //If both fingers go in the same direction we should rotate the camera
    if ( (pixel0.y() > prevPixel0.y() && pixel1.y() > prevPixel1.y()) ||
        (pixel0.x() > prevPixel0.x() && pixel1.x() > prevPixel1.x()) ||
        (pixel0.y() < prevPixel0.y() && pixel1.y() < prevPixel1.y()) ||
        (pixel0.x() < prevPixel0.x() && pixel1.x() < prevPixel1.x())) {
      return Rotate;
    }
    else {
      //If fingers are diverging it is zoom
      return Zoom;
    }
    */
  }
  return None;
}

void CameraRenderer::makeRotate(const TouchEvent& touchEvent) {
  int todo_JM_there_is_a_bug;
  
  const Vector2D pixel0 = touchEvent.getTouch(0)->getPos();
  const Vector2D pixel1 = touchEvent.getTouch(1)->getPos();
  const Vector2D pixelCenter = pixel0.add(pixel1).div(2.0);
  
  //The gesture is starting
  if (_initialPixel.isNan()){
     //Storing starting pixel
    _initialPixel = Vector3D(pixelCenter.x(), pixelCenter.y(), 0).asMutableVector3D();
  }
  
  //Calculating the point we are going to rotate around
  const Vector3D rotatingPoint = centerOfViewOnPlanet(_camera0);
  if (rotatingPoint.isNan()) {
    return; //We don't rotate without a valid rotating point
  }
  
  //Rotating axis
  const Vector3D camVec = _camera0.getPosition().sub(_camera0.getCenter());
  const Vector3D normal = _planet->geodeticSurfaceNormal(rotatingPoint);
  const Vector3D horizontalAxis = normal.cross(camVec);
  
  //Calculating the angle we have to rotate the camera vertically
  double distY = pixelCenter.y() - _initialPixel.y();
  double distX = pixelCenter.x() - _initialPixel.x();
  const Angle verticalAngle = Angle::fromDegrees( (distY / (double)_camera0.getHeight()) * 180.0 );
  const Angle horizontalAngle = Angle::fromDegrees( (distX / (double)_camera0.getWidth()) * 360.0 );
  
//  _logger->logInfo("ROTATING V=%f H=%f\n", verticalAngle.degrees(), horizontalAngle.degrees());
  
  //Back-Up camera0
  Camera cameraAux(_camera0);
  
  //Rotating vertically
  cameraAux.rotateWithAxisAndPoint(horizontalAxis, rotatingPoint, verticalAngle); //Up and down
  
  //Check if the view isn't too low
  Vector3D vCamAux = cameraAux.getPosition().sub(cameraAux.getCenter());
  Angle alpha = vCamAux.angleBetween(normal);
  Vector3D center = centerOfViewOnPlanet(*_camera);
  
  if ((alpha.degrees() > 85.0) || center.isNan()){
    cameraAux.copyFrom(_camera0); //We trash the vertical rotation
  }
  
  //Rotating horizontally
  cameraAux.rotateWithAxisAndPoint(normal, rotatingPoint, horizontalAngle); //Horizontally
  
  //Finally we copy the new camera
  _camera->copyFrom(cameraAux);
}

Vector3D CameraRenderer::centerOfViewOnPlanet(const Camera& c) const
{
  const Vector2D centerViewport(c.getWidth() / 2, c.getHeight() / 2);
  const Vector3D rayCV = c.pixel2Ray(centerViewport);
  
  return _planet->closestIntersection(c.getPosition(), rayCV);
}


void CameraRenderer::onMove(const TouchEvent& touchEvent) {
  _currentGesture = getGesture(touchEvent);
  
  switch (_currentGesture) {
    case Drag:
      makeDrag(touchEvent);
      break;
    case Zoom:
      //makeZoom(touchEvent);
      makeDoubleDrag(touchEvent);
      break;
    case Rotate:
      makeRotate(touchEvent);
      break;
    default:
      break;
  }
}

void CameraRenderer::onUp(const TouchEvent& touchEvent) {
  _currentGesture = None;
  _initialPixel = Vector3D::nan().asMutableVector3D();
}

bool CameraRenderer::onTouchEvent(const TouchEvent* touchEvent) {
  switch (touchEvent->getType()) {
    case Down:
      onDown(*touchEvent);
      break;
    case Move:
      onMove(*touchEvent);
      break;
    case Up:
      onUp(*touchEvent);
    default:
      break;
  }
  
  return true;
}