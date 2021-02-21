//
//  Arrow.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 20/2/21.
//

#ifndef Arrow_hpp
#define Arrow_hpp

#include <stdio.h>
#include "MeshRenderer.hpp"
#include "Vector3D.hpp"
#include "Cylinder.hpp"
#include "Ray.hpp"
#include "G3MEventContext.hpp"
#include "Camera.hpp"
#include "TouchEvent.hpp"

class Arrow: public MeshRenderer{
private:
  const Vector3D _base, _tip;
  const double _radius;
public:
  Arrow(const Vector3D& base,
        const Vector3D& tip,
        double radius,
        const Color& color,
        double headLength = 3.0,
        double headWidthRatio = 1.2 ):
  _base(base), _tip(tip), _radius(radius){
    Vector3D dir = tip.sub(base);
    
    Vector3D headBase = base.add(dir.normalized().times(dir.length() - headLength));
    Cylinder cylinder(base, headBase, radius, radius);
    
    Cylinder arrowTip(headBase,
                      tip,
                      radius * headWidthRatio, 0.0);
    
    Cylinder arrowTipCover(headBase,
                           headBase.sub(dir.normalized().times(0.0001)),
                           radius * headWidthRatio, 0.0);
    
    addMesh(cylinder.createMesh(color, 10));
    addMesh(arrowTip.createMesh(color, 20));
    addMesh(arrowTipCover.createMesh(color, 20));
  }
  
  bool onTouchEvent(const G3MEventContext* ec, const TouchEvent* touchEvent) override {
    
    if (touchEvent->getTouchCount() != 1 || touchEvent->getTapCount() != 1){
      return false;
    }
    const Touch& touch = *touchEvent->getTouch(0);
    
    Ray arrowRay(_base, _tip.sub(_base));
    Ray camRay(ec->_currentCamera->getCartesianPosition(), ec->_currentCamera->pixel2Ray(touch.getPos()));
    
    MutableVector3D arrowPoint, camRayPoint;
    Ray::closestPointsOnTwoRays(arrowRay, camRay, arrowPoint, camRayPoint);
    double dist = arrowPoint.asVector3D().distanceTo(camRayPoint.asVector3D());
    
    if (dist < _radius){
      printf("Touched Arrow %f %s\n", dist, arrowPoint.sub(camRayPoint).description().c_str());
      return true;
    }
    
    return false;
  }
  
};

#endif /* Arrow_hpp */
