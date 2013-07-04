//
//  CameraZoomAndRotateHandler.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo on 26/06/13.
//
//

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

  // if it is the first move, let's decide if make zoom or rotate
  if (cameraContext->getCurrentGesture() == DoubleDrag) {
    Vector2I difPixel0 = pixel0.sub(_initialPixel0.asVector2I());
    Vector2I difPixel1 = pixel1.sub(_initialPixel1.asVector2I());
    if ((difPixel0._y<-1 && difPixel1._y>1) || (difPixel0._y>1 && difPixel1._y<-1) ||
        (difPixel0._x<-1 && difPixel1._x>1) || (difPixel0._x>1 && difPixel1._x<-1)) {
      printf ("zoom..\n");
      cameraContext->setCurrentGesture(Zoom);
    }
    
    if ((difPixel0._y<-1 && difPixel1._y<-1) || (difPixel0._y>1 && difPixel1._y>1) ||
        (difPixel0._x<-1 && difPixel1._x<-1) || (difPixel0._x>1 && difPixel1._x>1)) {
      printf ("rotate..\n");
      cameraContext->setCurrentGesture(Rotate);
    }

    
    /*
      // compute intersection of view direction with the globe
      validFingerPos = camera.IntersectionViewWithGlobe (center);
      if (!validFingerPos) return false;
      // save the initial two points
      fingerSep0 = (float) sqrt((float)((c2x-c1x)*(c2x-c1x)+(c2y-c1y)*(c2y-c1y)));
      lastAngle = angle0 = atan2(c2y-c1y, c2x-c1x);
      camera0 = Camera(camera);//camera0 = camera;
      //WARNING: probably, when viewer be very close to the ground, looking at horizon,
      // the intersection with a point of lower scanlines must be found
      
      //iprintf (" --- empiezo zoom. Fingersep0=%.2f. angle0=%.2f.  Gesture anterior=%d\n", fingerSep0, angle0, gesture);
      gesture=Zoom;*/
  }

  
  // call specific transformation
  const Gesture gesture = cameraContext->getCurrentGesture();
  if (gesture == Zoom) {
    if (_processZoom) {
      zoom();
    }
  }
  else if (gesture == Rotate) {
    if (_processRotation) {
      rotate();
    }
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
}

void CameraZoomAndRotateHandler::rotate()
{
  printf ("rotating....\n");
}
