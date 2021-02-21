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
#include "GLFeature.hpp"
#include "GLState.hpp"

class Arrow: public MeshRenderer{
private:
  bool _grabbed;
  MutableVector3D _grabbedPos;
  MutableVector3D _baseWhenGrabbed;
  
  GLState* _state;
  ModelTransformGLFeature* _transformGLFeature;
  
  MutableVector3D _base, _vector;
  const double _radius;
public:
  Arrow(const Vector3D& base,
        const Vector3D& vector,
        double radius,
        const Color& color,
        double headLength = 3.0,
        double headWidthRatio = 1.2 ):
  _base(base), _vector(vector), _radius(radius), _grabbed(false){
    
    Vector3D tipVector = _vector.normalized().times(headLength).asVector3D();
    
    Vector3D headBase = _base.add(_vector).sub(tipVector);
    Cylinder cylinder(base, headBase, radius, radius);
    
    Cylinder arrowTip(headBase,
                      headBase.add(tipVector),
                      radius * headWidthRatio, 0.0);
    
    Cylinder arrowTipCover(headBase,
                           headBase.sub(_vector.normalized().times(0.0001)),
                           radius * headWidthRatio, 0.0);
    
    addMesh(cylinder.createMesh(color, 10));
    addMesh(arrowTip.createMesh(color, 20));
    addMesh(arrowTipCover.createMesh(color, 20));
    
    
    _state = new GLState();
    _transformGLFeature = new ModelTransformGLFeature(Matrix44D::createIdentity());
    _state->addGLFeature(_transformGLFeature, false);
    
    //_transformGLFeature->setMatrix(MutableMatrix44D::createTranslationMatrix(_base.asVector3D()).asMatrix44D());
  }
  
  ~Arrow(){
    _state->_release();
  }
  
  bool onTouchEvent(const G3MEventContext* ec, const TouchEvent* touchEvent) override {
    
    if (touchEvent->getTouchCount() != 1 || touchEvent->getTapCount() != 1 || touchEvent->getType() == TouchEventType::Up){
      _grabbed = false;
      return false;
    }
    const Touch& touch = *touchEvent->getTouch(0);
    
    Ray arrowRay(_base.asVector3D(), _vector.asVector3D());
    Ray camRay(ec->_currentCamera->getCartesianPosition(), ec->_currentCamera->pixel2Ray(touch.getPos()));
    
    MutableVector3D arrowPoint, camRayPoint;
    Ray::closestPointsOnTwoRays(arrowRay, camRay, arrowPoint, camRayPoint);
    
    switch (touchEvent->getType()) {
      case TouchEventType::Down:{
        double dist = arrowPoint.asVector3D().distanceTo(camRayPoint.asVector3D());
        
        if (dist < _radius){
          printf("Touched Arrow Base %s\n", arrowPoint.sub(camRayPoint).description().c_str());
          _grabbedPos = arrowPoint;
          _baseWhenGrabbed = _base;
          _grabbed = true;
          return true;
        }
        break;
      }
      case TouchEventType::Move:{
        if (_grabbed){
          MutableVector3D disp = arrowPoint.sub(_grabbedPos);
          _base = _baseWhenGrabbed.add(disp);
          printf("Arrow new base %s\n", _base.description().c_str());
        }
        
        break;
      }
      default:
        break;
    }
    
    return false;
  }
  
  void render(const G3MRenderContext* rc, GLState* glState) override{
    
    _state->setParent(glState);
    
    
    MeshRenderer::render(rc, _state);
  }
  
};

#endif /* Arrow_hpp */
