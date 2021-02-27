//
//  Arrow.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 20/2/21.
//

#include "Arrow.hpp"

#include "Vector3D.hpp"
#include "Cylinder.hpp"
#include "GLState.hpp"
#include "TouchEvent.hpp"
#include "Ray.hpp"
#include "G3MEventContext.hpp"
#include "Camera.hpp"


Arrow::Arrow(const Vector3D& base,
             const Vector3D& vector,
             double radius,
             const Color& color,
             double headLength,
             double headWidthRatio,
             bool doubleHeaded) :
MeshRenderer(false), //No culling as geometry is displaced!!!
_base(base),
_vector(vector),
_radius(radius),
_grabbed(false),
_listener(NULL)
{
  Vector3D tipVector = _vector.normalized().times(headLength).asVector3D();
  Vector3D headBase  = _vector.sub(tipVector);

  if (doubleHeaded) {
    Cylinder arrowTip(Vector3D::ZERO,
                      tipVector,
                      0.0, radius * headWidthRatio);
    addMesh(arrowTip.createMesh(color, 10));

    Cylinder arrowTipCover(tipVector,
                           tipVector.add(_vector.normalized().times(0.0001).asVector3D()),
                           radius * headWidthRatio, 0.0);
    addMesh(arrowTipCover.createMesh(color, 10));
  }

  Cylinder cylinder(doubleHeaded? tipVector : Vector3D::ZERO, headBase, radius, radius);

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
  _transformGLFeature = new ModelTransformGLFeature(MutableMatrix44D::createTranslationMatrix(_base.asVector3D()).asMatrix44D());
  _state->addGLFeature(_transformGLFeature, false);
}

Arrow::~Arrow() {
  _state->_release();
}


bool Arrow::onTouchEvent(const G3MEventContext* ec, const TouchEvent* touchEvent) {

  if (touchEvent->getTouchCount() != 1 || touchEvent->getTapCount() != 1 || touchEvent->getType() == TouchEventType::Up) {
    if (_grabbed && _listener) {
      _listener->onDraggingEnded(*this);
    }
    _grabbed = false;
    return false;
  }

  const Touch& touch = *touchEvent->getTouch(0);

  Ray arrowRay(_base.asVector3D(), _vector.asVector3D());
  Ray camRay(ec->_currentCamera->getCartesianPosition(), ec->_currentCamera->pixel2Ray(touch.getPos()));

  MutableVector3D arrowPoint, camRayPoint;
  Ray::closestPointsOnTwoRays(arrowRay, camRay, arrowPoint, camRayPoint);

#define SELECTION_RADIUS_RATIO 4.0

  switch (touchEvent->getType()) {
    case Down: {
      const double distArrow = arrowPoint.asVector3D().sub(arrowRay._origin).div(_vector.asVector3D()).maxAxis();
      const bool onArrow = distArrow >= 0. && distArrow <= 1.;

      if (onArrow) {
        const double dist = arrowPoint.asVector3D().distanceTo(camRayPoint.asVector3D());

        if (dist < _radius * SELECTION_RADIUS_RATIO && onArrow) {
          _grabbedPos = arrowPoint;
          _baseWhenGrabbed = _base;
          _grabbed = true;
          return true;
        }
      }


      break;
    }

    case Move: {
      if (_grabbed) {
        MutableVector3D disp = arrowPoint.sub(_grabbedPos);
        setBase(_baseWhenGrabbed.add(disp).asVector3D());
      }

      break;
    }
    default:
      break;
  }

  return false;
}

void Arrow::render(const G3MRenderContext* rc, GLState* glState) {
  _state->setParent(glState);
  MeshRenderer::render(rc, _state);
}

void Arrow::setBase(const Vector3D& base,
                    bool notifyListeners) {
  if (!base.isEquals(_base.asVector3D())) {
    _base = base.asMutableVector3D();
    _transformGLFeature->setMatrix(MutableMatrix44D::createTranslationMatrix(_base.asVector3D()).asMatrix44D());
    if (_listener && notifyListeners) {
      _listener->onBaseChanged(*this);
    }
  }
}

const Vector3D Arrow::getBase() const {
  return _base.asVector3D();
}

void Arrow::setArrowListener(ArrowListener* listener) {
  _listener = listener;
}
