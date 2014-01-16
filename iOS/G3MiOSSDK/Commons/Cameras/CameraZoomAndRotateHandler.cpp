//
//  CameraZoomAndRotateHandler.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo on 26/06/13.
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
  
  Vector2I pixel0 = touchEvent.getTouch(0)->getPos();
  Vector2I pixel1 = touchEvent.getTouch(1)->getPos();
  Vector2I difCurrentPixels = pixel1.sub(pixel0);
  const Planet* planet = eventContext->getPlanet();

  // if it is the first move, let's decide if make zoom or rotate
  if (cameraContext->getCurrentGesture() == DoubleDrag) {
    Vector2I difPixel0 = pixel0.sub(_initialPixel0.asVector2I());
    Vector2I difPixel1 = pixel1.sub(_initialPixel1.asVector2I());
    if ((difPixel0._y<-1 && difPixel1._y>1) || (difPixel0._y>1 && difPixel1._y<-1) ||
        (difPixel0._x<-1 && difPixel1._x>1) || (difPixel0._x>1 && difPixel1._x<-1)) {
      //printf ("zoom..\n");
      cameraContext->setCurrentGesture(Zoom);
    }
    
    // test if starting a zoom action
    if ((difPixel0._y<-1 && difPixel1._y>1) || (difPixel0._y>1 && difPixel1._y<-1) ||
            (difPixel0._x<-1 && difPixel1._x>1) || (difPixel0._x>1 && difPixel1._x<-1)) {
      
      // compute intersection of view direction with the globe
      Vector3D intersection = planet->closestIntersection(_camera0.getCartesianPosition(), _camera0.getViewDirection());
      if (!intersection.isNan()) {
        _centralGlobePoint = intersection.asMutableVector3D();
        _centralGlobeNormal = planet->geodeticSurfaceNormal(_centralGlobePoint).asMutableVector3D();
        _fingerSep0 = sqrt((difCurrentPixels._x*difCurrentPixels._x+difCurrentPixels._y*difCurrentPixels._y));
        _lastAngle = _angle0 = atan2(difCurrentPixels._y, difCurrentPixels._x);
        cameraContext->setCurrentGesture(Zoom);
      }
      else
        ILogger::instance()->logInfo("Zoom is no possible. View direction does not intersect the globe");
    }

    // test if starting a rotate action
    if ((difPixel0._y<-1 && difPixel1._y<-1) || (difPixel0._y>1 && difPixel1._y>1) ||
        (difPixel0._x<-1 && difPixel1._x<-1) || (difPixel0._x>1 && difPixel1._x>1)) {
      //cameraContext->setCurrentGesture(Rotate);
    }
  }

  // call specific transformation
  switch (cameraContext->getCurrentGesture()) {
    case Zoom:
      zoom(cameraContext->getNextCamera(), difCurrentPixels);
      break;
      
    case Rotate:
      rotate();
      break;
      
    default:
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
  //      //printf ("zoom with initial point = (%f, %f)\n", g._latitude._degrees, g._longitude._degrees);
  //    }
  //  }
  
}



void CameraZoomAndRotateHandler::zoom(Camera* camera, Vector2I difCurrentPixels)
{
  const double MIN_CAMERA_HEIGHT = 30;
  
  // compute angle params
  double angle = atan2(difCurrentPixels._y, difCurrentPixels._x);
  while (fabs(_lastAngle-angle)>PI/2) {
    if (angle<_lastAngle) angle+=PI;  else  angle-=PI;
  }
  _lastAngle = angle;
  angle -= _angle0;
  
  // compute zoom params
  double fingerSep = sqrt((difCurrentPixels._x*difCurrentPixels._x+difCurrentPixels._y*difCurrentPixels._y));
  double factor = _fingerSep0 / fingerSep;
  double desp = 1-factor;
  Vector3D w = _centralGlobePoint.asVector3D().sub(_camera0.getCartesianPosition());
  double dist = w.length();
    
	// don't allow much closer
	if (dist*factor<MIN_CAMERA_HEIGHT && factor<0.999)
    return;
	
	// don't allow much further away
  double R = _centralGlobePoint.length();
	if (dist*factor>11*R && factor>1.001)
    return;
	
	// make zoom and rotation
  camera->copyFrom(_camera0);
  
  // make rotation
  camera->rotateWithAxisAndPoint(_centralGlobeNormal.asVector3D(),
                                 _centralGlobePoint.asVector3D(),
                                 Angle::fromRadians(angle));
  //camera->rotateWithAxis(_centralGlobePoint.asVector3D(), Angle::fromRadians(angle));
  
  // make zoom
  camera->moveForward(desp*dist);
  
  /*printf("dist=%.2f.  desp=%f.   factor=%f   new dist=%.2f\n", dist, desp, factor, dist-desp*dist);
  printf ("camera en (%.2f, %.2f, %.2f)     centralpoint en (%.2f, %.2f, %.2f). \n",
          _camera0.getCartesianPosition().x(),  _camera0.getCartesianPosition().y(),  _camera0.getCartesianPosition().z(),
          _centralGlobePoint.x(), _centralGlobePoint.y(), _centralGlobePoint.z());*/
}

void CameraZoomAndRotateHandler::rotate()
{
  printf ("rotating....\n");
}
