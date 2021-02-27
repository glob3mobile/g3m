//
//  Arrow.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 20/2/21.
//

#ifndef Arrow_hpp
#define Arrow_hpp

#include "MeshRenderer.hpp"
#include "MutableVector3D.hpp"

class ModelTransformGLFeature;
class Arrow;

class ArrowListener{
public:
  virtual ~ArrowListener() {

  }

  virtual void onBaseChanged(const Arrow& arrow) = 0;

  virtual void onDraggingEnded(const Arrow& arrow) = 0;

};


class Arrow: public MeshRenderer{
private:
  bool _grabbed;
  MutableVector3D _grabbedPos;
  MutableVector3D _baseWhenGrabbed;
  
  GLState* _state;
  ModelTransformGLFeature* _transformGLFeature;
  
  MutableVector3D _base, _vector;
  const double _radius;
  
  ArrowListener* _listener;
  
public:
  Arrow(const Vector3D& base,
        const Vector3D& vector,
        double radius,
        const Color& color,
        double headLength = 3.0,
        double headWidthRatio = 1.2,
        bool doubleHeaded = false);
  
  ~Arrow();
  
  bool onTouchEvent(const G3MEventContext* ec, const TouchEvent* touchEvent);
  
  void render(const G3MRenderContext* rc, GLState* glState);
  
  void setBase(const Vector3D& base, bool notifyListeners = true);
  
  const Vector3D getBase() const;
  
  void setArrowListener(ArrowListener* listener);
  
};

#endif
