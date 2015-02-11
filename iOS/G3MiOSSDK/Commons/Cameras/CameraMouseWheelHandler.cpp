//
//  CameraMouseWheelHandler.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 10/10/14.
//
//

#include "CameraMouseWheelHandler.hpp"
#include "Camera.hpp"
#include "TouchEvent.hpp"
#include "G3MWidget.hpp"

bool CameraMouseWheelHandler::onTouchEvent(const G3MEventContext *eventContext,
                  const TouchEvent* touchEvent,
                  CameraContext *cameraContext){
  
  if (touchEvent->getType() == MouseWheelChanged){
    onMouseWheel(eventContext, *touchEvent, cameraContext);
    return true;
  }
  return false;

}


void CameraMouseWheelHandler::onMouseWheel(const G3MEventContext *eventContext,
                                           const TouchEvent& touchEvent,
                                           CameraContext *cameraContext){
  Camera* cam = cameraContext->getNextCamera();
  
  Vector2I pixel = touchEvent.getTouch(0)->getPos();
  Vector3D touchedPosition = eventContext->getWidget()->getScenePositionForPixel(pixel._x, pixel._y);
  
  if (!touchedPosition.isNan()){
    
    const Vector3D dir = cam->pixel2Ray(pixel).normalized();
    
    double dist = touchedPosition.distanceTo(cam->getCartesianPosition());
    
    const double delta = touchEvent.getMouseWheelDelta();
    double factor = 0.1;
    if (delta < 0){
      factor *= -1;
    }
    
    Vector3D translation = dir.normalized().times(dist * factor);
    
    cam->translateCamera(translation);
    
  }
  
  //NO ZRENDER
  
//  const Planet* planet = eventContext->getPlanet();
//  const Vector3D dir = cam->pixel2Ray(touchEvent.getTouch(0)->getPos()).normalized();
//  
//#warning USE ZRENDER IN THE FUTURE
//  std::vector<double> dists = planet->intersectionsDistances(cam->getCartesianPosition(), dir);
//  
//  if (dists.size() > 0){     //Research other behaviours as Google Earth
//
//    const double delta = touchEvent.getMouseWheelDelta();
//    double factor = 0.1;
//    if (delta < 0){
//      factor *= -1;
//    }
//    
//    double dist = dists.at(0);
//    Vector3D translation = dir.normalized().times(dist * factor);
//    
//    cam->translateCamera(translation);
//  }
  
  
}