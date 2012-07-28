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
  
  _camera->render(rc);
  
  // TEMP TO DRAW A POINT WHERE USER PRESS
  if (false) {
    if (_currentGesture == Zoom) {
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
      
      //Geodetic2D g = _planet->toGeodetic2D(_initialPoint.asVector3D());
      //printf ("zoom with initial point = (%f, %f)\n", g.latitude().degrees(), g.longitude().degrees());
    }
  }
  
  return MAX_TIME_TO_RENDER;
}

void CameraRenderer::onResizeViewportEvent(int width, int height) {
  if (_camera != NULL) {
    _camera->resizeViewport(width, height);
  }
}


Gesture CameraRenderer::getGesture(const TouchEvent& touchEvent) {
  
  return _currentGesture;
  
  /*
  int n = touchEvent.getTouchCount();
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
    
    //Dragging
    if (_currentGesture == DoubleDrag) {
      return DoubleDrag;
    }
    else {
      return None;
    }
*/
    
/*    
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
            
      Vector2D difPixel = pixel1.sub(pixel0);
      _initialFingerSeparation = difPixel.length();
      _initialFingerInclination = difPixel.orientation().radians();
      
    }
    return Zoom;
    
 */   
    
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
  //}
  //return None;
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
  const Vector3D rotatingPoint = _camera0.centerOfViewOnPlanet(_planet);
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
  Vector3D center = _camera->centerOfViewOnPlanet(_planet);
  
  if ((alpha.degrees() > 85.0) || center.isNan()){
    cameraAux.copyFrom(_camera0); //We trash the vertical rotation
  }
  
  //Rotating horizontally
  cameraAux.rotateWithAxisAndPoint(normal, rotatingPoint, horizontalAngle); //Horizontally
  
  //Finally we copy the new camera
  _camera->copyFrom(cameraAux);
}

