//
//  CameraZoomAndRotateHandler.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo on 26/06/13.
//
//

#include <math.h>

#include "CameraZoomAndRotateHandler.hpp"

#include "GL.hpp"
#include "TouchEvent.hpp"



bool CameraZoomAndRotateHandler::onTouchEvent(const G3MEventContext *eventContext,
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


void CameraZoomAndRotateHandler::onDown(const G3MEventContext *eventContext,
                                     const TouchEvent& touchEvent,
                                     CameraContext *cameraContext)
{
  Camera *camera = cameraContext->getNextCamera();
  _camera0.copyFrom(*camera);
  cameraContext->setCurrentGesture(DoubleDrag);
  
  // double dragging
  _initialPixel0 = touchEvent.getTouch(0)->getPos().asMutableVector2I();
  _initialPixel1 = touchEvent.getTouch(1)->getPos().asMutableVector2I();
  }


void CameraZoomAndRotateHandler::onMove(const G3MEventContext *eventContext,
                                     const TouchEvent& touchEvent,
                                     CameraContext *cameraContext) {
  //const IMathUtils* mu = IMathUtils::instance();
  
  Vector2I pixel0 = touchEvent.getTouch(0)->getPos();
  Vector2I pixel1 = touchEvent.getTouch(1)->getPos();
  const Planet *planet = eventContext->getPlanet();


  // if it is the first move, let's decide if make zoom or rotate
  if (cameraContext->getCurrentGesture() == DoubleDrag) {
    Vector2I difPixel0 = pixel0.sub(_initialPixel0.asVector2I());
    Vector2I difPixel1 = pixel1.sub(_initialPixel1.asVector2I());
    
    // test if starting a zoom action
    if ((difPixel0._y<-1 && difPixel1._y>1) || (difPixel0._y>1 && difPixel1._y<-1) ||
            (difPixel0._x<-1 && difPixel1._x>1) || (difPixel0._x>1 && difPixel1._x<-1)) {
      
      // compute intersection of view direction with the globe
      Vector3D intersection = planet->closestIntersection(_camera0.getCartesianPosition(), _camera0.getViewDirection());
      if (!intersection.isNan()) {
        Vector2I dif = pixel1.sub(pixel0);
        _fingerSep0 = (float) sqrt((float)(dif._x*dif._x+dif._y*dif._y));
        _lastAngle = _angle0 = atan2(dif._y, dif._x);
        cameraContext->setCurrentGesture(Zoom);
      }
      else
        ILogger::instance()->logInfo("Zoom is no possible. View direction does not intersect the globe");
    }

    // test if starting a rotate action
    if ((difPixel0._y<-1 && difPixel1._y<-1) || (difPixel0._y>1 && difPixel1._y>1) ||
        (difPixel0._x<-1 && difPixel1._x<-1) || (difPixel0._x>1 && difPixel1._x>1)) {
      cameraContext->setCurrentGesture(Rotate);
    }
  }

  // call specific transformation
  switch (cameraContext->getCurrentGesture()) {
    case Zoom:
      if (_processZoom) zoom();
      break;
      
    case Rotate:
      if (_processRotation) rotate();
      break;
  }
}


void CameraZoomAndRotateHandler::onUp(const G3MEventContext *eventContext,
                                   const TouchEvent& touchEvent,
                                   CameraContext *cameraContext)
{
  cameraContext->setCurrentGesture(None);
  //_initialPixel0 = _initialPixel1 = Vector2I(-1,-1);
  
  //printf ("end 2 fingers.  gesture=%d\n", _currentGesture);
}


void CameraZoomAndRotateHandler::render(const G3MRenderContext* rc,
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
  //      //printf ("zoom with initial point = (%f, %f)\n", g.latitude()._degrees, g.longitude()._degrees);
  //    }
  //  }
  
}



void CameraZoomAndRotateHandler::zoom()
{
  printf ("zooming.....\n");
  
  static double lastAngle=0;
  
/*
  // compute angle value
  double angle = atan2(c2y-c1y, c2x-c1x);
  while (fabs(lastAngle-angle)>PI/2) {
    if (angle<lastAngle) angle+=PI;  else  angle-=PI;
  }
  lastAngle = angle;
  
  // make zoom
  float fingerSep = (float) sqrt((float)((c2x-c1x)*(c2x-c1x)+(c2y-c1y)*(c2y-c1y)));
  
  //iprintf ("haciendo zoom. fingersep=%.2f  angle=%.2f\n", fingerSep, angle);
  
  camera.ZoomCamera (center, fingerSep0/fingerSep, angle-angle0, camera0);
  
  //********************************
  
  void Camera::ZoomCamera (Point3D p0, double factor, double angle, const Camera &camera0)
  {
  Globe *g = SceneController::GetInstance()->getCurrentGlobe();
	double R = g->GetGlobeRadius();
	
	// we move the camera in that direction
	double wx=p0.x-camera0.pos.x, wy=p0.y-camera0.pos.y, wz=p0.z-camera0.pos.z;
	double dist = sqrt (wx*wx+wy*wy+wz*wz);
  
	// don't allow much closer
	if (dist*factor<MIN_CAMERA_HEIGHT && factor<0.999) return;
	
	// don't allow much further away
	if (dist*factor>11*R && factor>1.001) return;
	
	// make rotation
	double M[16]={1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1};
	double desp = 1-factor;
  Glu::ComputeRotationMatrix (angle, p0.x, p0.y, p0.z, M);
	Glu::ComputeTranslationMatrix (desp*wx, desp*wy, desp*wz, M);
	ApplyTransform(camera0, M);
  
	// save displacement and angle, in the case of zoom afterwards
	zoomStep = desp*dist - zoomDesp;
	zoomDesp = desp*dist;
	
  // update angle value
	if (angle>5) angle-=2*PI;
	if (angle<-5) angle+=2*PI;
	rotStep = angle - rotAngle;
	if (fabs(rotStep)>1) {
		//iprintf ("rotstep grande = %f   angle=%f  rotangle=%f\n", rotStep, angle, rotAngle);
		rotStep *= 1/fabs(rotStep);
	}
	rotAngle = angle;
  

  return true;*/

}

void CameraZoomAndRotateHandler::rotate()
{
  printf ("rotating....\n");
}
