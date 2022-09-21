//
//  CameraZoomAndRotateHandler.cpp
//  G3M
//
//  Created by Agustin Trujillo on 26/06/13.
//
//

#include "CameraZoomAndRotateHandler.hpp"

#include "GL.hpp"
#include "TouchEvent.hpp"
#include "G3MEventContext.hpp"
#include "Planet.hpp"
#include "Camera.hpp"
#include "CameraContext.hpp"
#include "RenderState.hpp"
#include "ILogger.hpp"


RenderState CameraZoomAndRotateHandler::getRenderState(const G3MRenderContext* rc) {
  return RenderState::ready();
}

bool CameraZoomAndRotateHandler::onTouchEvent(const G3MEventContext *eventContext,
                                              const TouchEvent* touchEvent,
                                              CameraContext *cameraContext) {
  // only one finger needed
  if (touchEvent->getTouchCount()!=2) return false;

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


void CameraZoomAndRotateHandler::onDown(const G3MEventContext *eventContext,
                                        const TouchEvent& touchEvent,
                                        CameraContext *cameraContext) {
  Camera *camera = cameraContext->getNextCamera();
  camera->getLookAtParamsInto(_cameraPosition, _cameraCenter, _cameraUp);
  cameraContext->setCurrentGesture(DoubleDrag);

  // double dragging
  _initialPixel0.set(MutableVector2F(touchEvent.getTouch(0)->getPos()));
  _initialPixel1.set(MutableVector2F(touchEvent.getTouch(1)->getPos()));
}


void CameraZoomAndRotateHandler::onMove(const G3MEventContext *eventContext,
                                        const TouchEvent& touchEvent,
                                        CameraContext *cameraContext) {

  Vector2F pixel0 = touchEvent.getTouch(0)->getPos();
  Vector2F pixel1 = touchEvent.getTouch(1)->getPos();
  Vector2F difCurrentPixels = pixel1.sub(pixel0);
  const Planet* planet = eventContext->getPlanet();

  // if it is the first move, let's decide if make zoom or rotate
  if (cameraContext->getCurrentGesture() == DoubleDrag) {
    Vector2F difPixel0 = pixel0.sub(_initialPixel0.asVector2F());
    Vector2F difPixel1 = pixel1.sub(_initialPixel1.asVector2F());
    if ((difPixel0._y<-1 && difPixel1._y>1) || (difPixel0._y>1 && difPixel1._y<-1) ||
        (difPixel0._x<-1 && difPixel1._x>1) || (difPixel0._x>1 && difPixel1._x<-1)) {
      //printf ("zoom..\n");
      cameraContext->setCurrentGesture(Zoom);
    }

    // test if starting a zoom action
    if ((difPixel0._y<-1 && difPixel1._y>1) || (difPixel0._y>1 && difPixel1._y<-1) ||
        (difPixel0._x<-1 && difPixel1._x>1) || (difPixel0._x>1 && difPixel1._x<-1)) {

      // compute intersection of view direction with the globe
      Vector3D intersection = planet->closestIntersection(_cameraPosition.asVector3D(),
                                                          _cameraCenter.sub(_cameraPosition).asVector3D());
      if (!intersection.isNan()) {
        //        _centralGlobePoint = intersection.asMutableVector3D();
        _centralGlobePoint.set(intersection);
        //        _centralGlobeNormal = planet->geodeticSurfaceNormal(_centralGlobePoint).asMutableVector3D();
        _centralGlobeNormal.set(planet->geodeticSurfaceNormal(_centralGlobePoint));
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

void CameraZoomAndRotateHandler::zoom(Camera* camera, const Vector2F& difCurrentPixels) {
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
  Vector3D w = _centralGlobePoint.asVector3D().sub(_cameraPosition);
  double dist = w.length();

  // don't allow much closer
  if (dist*factor<MIN_CAMERA_HEIGHT && factor<0.999)
    return;

  // don't allow much further away
  double R = _centralGlobePoint.length();
  if (dist*factor>11*R && factor>1.001)
    return;

  // make zoom and rotation
  camera->setLookAtParams(_cameraPosition, _cameraCenter, _cameraUp);

  // make rotation
  camera->rotateWithAxisAndPoint(_centralGlobeNormal.asVector3D(),
                                 _centralGlobePoint.asVector3D(),
                                 Angle::fromRadians(angle));
  //camera->rotateWithAxis(_centralGlobePoint.asVector3D(), Angle::fromRadians(angle));

  // make zoom
  camera->moveForward(desp*dist);
}

void CameraZoomAndRotateHandler::rotate() {
  printf ("rotating....\n");
}
