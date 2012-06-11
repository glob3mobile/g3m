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
_camera0(0,0),
_initialPoint(0,0,0),
_currentGesture(None),
_camera(NULL),
_initialPixel(0,0,0)
{
}

void CameraRenderer::initialize(const InitializationContext* ic){}

int CameraRenderer::render(const RenderContext* rc)
{
  _camera = rc->getCamera(); //Saving camera reference 
  _planet = rc->getPlanet();
  
  rc->getCamera()->draw(*rc);   //We "draw" the camera with IGL
  return 0;
}

bool CameraRenderer::onResizeViewportEvent(int width, int height)
{
  if (_camera != NULL)
  {
    _camera->resizeViewport(width, height);
    return true;
  } else 
    return false;
}

void CameraRenderer::onDown(const TouchEvent& touchEvent)
{
  //Saving Camera0
  Camera c(*_camera);
  _camera0 = c;
  
  //Initial Point for Dragging
  Vector2D pixel = touchEvent.getTouch(0)->getPos();
  Vector3D ray = _camera0.pixel2Vector(pixel);
  _initialPoint = _planet->closestIntersection(_camera0.getPos(), ray).asMutableVector3D();
  _currentGesture = Drag; //Dragging
}

void CameraRenderer::makeDrag(const TouchEvent& touchEvent)
{
  if (!_initialPoint.isNan()) //VALID INITIAL POINT
  { 
    Vector2D pixel = touchEvent.getTouch(0)->getPos();
    Vector3D ray = _camera0.pixel2Vector(pixel);
    Vector3D pos = _camera0.getPos();
    
    MutableVector3D finalPoint = _planet->closestIntersection(pos, ray).asMutableVector3D();
    if (finalPoint.isNan()){ //INVALID FINAL POINT
      finalPoint = _planet->closestPointToSphere(pos, ray).asMutableVector3D();
    }
    
    _camera->copyFrom(_camera0);
    _camera->dragCamera(_initialPoint.asVector3D(), finalPoint.asVector3D());
  }
}

void CameraRenderer::makeZoom(const TouchEvent& touchEvent)
{
  Vector2D pixel0 = touchEvent.getTouch(0)->getPos();
  Vector2D pixel1 = touchEvent.getTouch(1)->getPos();
  Vector2D pixelCenter = pixel0.add(pixel1).div(2.0);
  
  Vector3D ray = _camera0.pixel2Vector(pixelCenter);
  _initialPoint = _planet->closestIntersection(_camera0.getPos(), ray).asMutableVector3D();
  
  Vector2D centerOfViewport(_camera0.getWidth() / 2, _camera0.getHeight() / 2);
  Vector3D ray2 = _camera0.pixel2Vector(centerOfViewport);
  Vector3D pointInCenterOfView = _planet->closestIntersection(_camera0.getPos(), ray2);
  
  //IF CENTER PIXEL INTERSECTS THE PLANET
  if (!_initialPoint.isNan()){
    
    //IF THE CENTER OF THE VIEW INTERSECTS THE PLANET
    if (!pointInCenterOfView.isNan()){
      
      Vector2D prevPixel0 = touchEvent.getTouch(0)->getPrevPos();
      Vector2D prevPixel1 = touchEvent.getTouch(1)->getPrevPos();
      
      double dist = pixel0.sub(pixel1).length();
      double prevDist = prevPixel0.sub(prevPixel1).length();
      
      Vector2D pixelDelta = pixel1.sub(pixel0);
      Vector2D prevPixelDelta = prevPixel1.sub(prevPixel0);
      
      Angle angle = pixelDelta.angle();
      Angle prevAngle = prevPixelDelta.angle();
      
      //We rotate and zoom the camera with the same gesture
      _camera->zoom(prevDist /dist);
      _camera->pivotOnCenter(angle.sub(prevAngle));
    }
  }
}

Gesture CameraRenderer::getGesture(const TouchEvent& touchEvent) const
{
  int n = touchEvent.getNumTouch();
  if (n == 1){
    //Dragging
    if (_currentGesture == Drag) 
      return Drag;
    else 
      return None;
  }
  
  if (n== 2){
    
    //If the gesture is set we don't have to change it
    if (_currentGesture == Zoom) return Zoom;
    if (_currentGesture == Rotate) return Rotate;
    
    //We have to fingers and the previous event was Drag
    Vector2D pixel0 = touchEvent.getTouch(0)->getPos();
    Vector2D pixel1 = touchEvent.getTouch(1)->getPos();
    
    Vector2D prevPixel0 = touchEvent.getTouch(0)->getPrevPos();
    Vector2D prevPixel1 = touchEvent.getTouch(1)->getPrevPos();
    
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
 
  }
  return None;
}

void CameraRenderer::makeRotate(const TouchEvent& touchEvent)
{
  int todo_JM_working;
  
  Vector2D pixel0 = event.getTouch(0)->getPos();
  Vector2D pixel1 = event.getTouch(1)->getPos();
  Vector2D pixelCenter = pixel0.add(pixel1).div(2.0);
  
  //The gesture is starting
  if (_initialPixel.isNan()){
    Vector3D v(pixelCenter.x(), pixelCenter.y(), 0); 
    _initialPixel = v.asMutableVector3D(); //Storing starting pixel
  }
  
  Vector3D ray = _camera0.pixel2Vector(pixelCenter);
  _initialPoint = _planet->closestIntersection(_camera0.getPos(), ray).asMutableVector3D();
  
  //Calculating the point we are going to rotate around
  Vector2D centerViewport(_camera->getWidth() /2, _camera->getHeight() /2);
  Vector3D rayCV = _camera0.pixel2Vector(pixelCenter);
  Vector3D rotatingPoint = _planet->closestIntersection(_camera0.getPos(), rayCV);
  
  //We don't rotate
  if (_initialPoint.isNan() || rotatingPoint.isNan()) return;
  
  //Rotating axis
  Vector3D camVec = _camera0.getPos().sub(_camera0.getCenter());
  Vector3D normal = _planet->geodeticSurfaceNormal(rotatingPoint);
  Vector3D horizontalAxis = normal.cross(camVec);

  //Calculating the angle we have to rotate the camera vertically
  double distY = pixelCenter.y() - _initialPixel.y();
  Angle horizontalAngle = Angle::fromDegrees( ((double)distY / (double)_camera0.getHeight()) * 180.0 );
  
  //We don't put the camera upside down
  if (horizontalAngle.degrees() < 0.0 || horizontalAngle.degrees() > 85.0) return;
  printf("%f\n", horizontalAngle.degrees());
  
  //Back-Up camera
  Camera cameraAux(0,0);
  cameraAux.copyFrom(*_camera);
  
  //Apply rotation
  _camera->copyFrom(_camera0);
  _camera->rotateWithAxisAndPoint(horizontalAxis, _initialPoint.asVector3D(), horizontalAngle);
  
  //If the final camera don't intersect the planet we don't apply the transformation
  
  Vector3D newCamVec = _camera->getPos().sub(_camera->getCenter());
  Angle a = Angle::fromRadians( newCamVec.angleBetween(normal) );
  printf("ANGLE NCV : %f\n", a.degrees());
  
  //If the camera is too low or doesn't intersect the planet
  if ((a.degrees() > 85.0) || !cameraLooksToPlanet(*_camera))
  {
    printf("MOVEMENT NOT ALLOWED\n");
    // we don't apply the transformation
    _camera->copyFrom(cameraAux);
  }
}

bool CameraRenderer::cameraLooksToPlanet(const Camera& c) const
{
    int todo_JM_working;
  
  Vector2D centerViewport(c.getWidth() /2, c.getHeight() /2);
  Vector3D rayCV = c.pixel2Vector(centerViewport);
  return ! _planet->closestIntersection(c.getPos(), rayCV).isNan() ;
}


void CameraRenderer::onMove(const TouchEvent& touchEvent)
{
  _currentGesture = getGesture(touchEvent);
  
  switch (_currentGesture) {
    case Drag:
      makeDrag(touchEvent);
      break;
    case Zoom:
      makeZoom(touchEvent);
      break;
    case Rotate:
      makeRotate(touchEvent);
      break;
    default:
      break;
  }
}

void CameraRenderer::onUp(const TouchEvent& touchEvent)
{
  _currentGesture = None;
  _initialPixel = Vector3D::nan().asMutableVector3D();
}

bool CameraRenderer::onTouchEvent(const TouchEvent* touchEvent)
{
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