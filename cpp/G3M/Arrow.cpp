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
#include "Angle.hpp"
#include "MutableMatrix44D.hpp"
#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "MutableVector3D.hpp"
#include "ShortBufferBuilder.hpp"
#include "IndexedMesh.hpp"
#include "Color.hpp"


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
                      0.0,
                      radius * headWidthRatio);
    addMesh(createCylinderMesh(arrowTip, color, 10));

    Cylinder arrowTipCover(tipVector,
                           tipVector.add(_vector.normalized().times(0.0001).asVector3D()),
                           radius * headWidthRatio,
                           0.0);
    addMesh(createCylinderMesh(arrowTipCover, color, 10));
  }

  Cylinder cylinder(doubleHeaded? tipVector : Vector3D::ZERO, headBase, radius, radius);

  Cylinder arrowTip(headBase,
                    headBase.add(tipVector),
                    radius * headWidthRatio, 0.0);

  Cylinder arrowTipCover(headBase,
                         headBase.sub(_vector.normalized().times(0.0001)),
                         radius * headWidthRatio,
                         0.0);

  addMesh(createCylinderMesh(cylinder, color, 10));
  addMesh(createCylinderMesh(arrowTip, color, 20));
  addMesh(createCylinderMesh(arrowTipCover, color, 20));

  _state = new GLState();
  _transformGLFeature = new ModelTransformGLFeature(MutableMatrix44D::createTranslationMatrix(_base.asVector3D()).asMatrix44D());
  _state->addGLFeature(_transformGLFeature, false);
}

Mesh* Arrow::createCylinderMesh(const Cylinder& cylinder,
                                const Color& color,
                                const int nSegments) {
  Vector3D d = cylinder._end.sub(cylinder._start);
  Vector3D r = (d._z == 0) ? Vector3D(0.0,0.0,1.0) : Vector3D(1.0, 1.0, (-d._x -d._y) / d._z);
  
  Vector3D pStart = cylinder._start.add(r.times(cylinder._startRadius / r.length()));
  Vector3D pEnd = cylinder._end.add(r.times(cylinder._endRadius / r.length()));
  MutableMatrix44D mStart = MutableMatrix44D::createGeneralRotationMatrix(Angle::fromDegrees(360.0 / nSegments),
                                                                          d,
                                                                          cylinder._start);
  MutableMatrix44D mEnd = MutableMatrix44D::createGeneralRotationMatrix(Angle::fromDegrees(360.0 / nSegments),
                                                                        d,
                                                                        cylinder._end);
  
  FloatBufferBuilderFromCartesian3D* vertices = FloatBufferBuilderFromCartesian3D::builderWithFirstVertexAsCenter();
  FloatBufferBuilderFromCartesian3D* normals  = FloatBufferBuilderFromCartesian3D::builderWithoutCenter();

  MutableVector3D newStartPoint(pStart), newEndPoint(pEnd);
  for (int i = 0; i < nSegments; ++i){
    
    //Tube
    newStartPoint.set(newStartPoint.transformedBy(mStart, 1.0));
    newEndPoint.set(newEndPoint.transformedBy(mEnd, 1.0));
    
    vertices->add(newStartPoint.asVector3D());
    vertices->add(newEndPoint.asVector3D());
    
    normals->add(newStartPoint.sub(cylinder._start));
    normals->add(newEndPoint.sub(cylinder._end));
  }
  
  ShortBufferBuilder indices;
  for (int i = 0; i < nSegments*2; ++i){
    indices.add((short) i);
  }
  indices.add((short) 0);
  indices.add((short) 1);

  IndexedMesh* im = new IndexedMesh(GLPrimitive::triangleStrip(),
                                    vertices->getCenter(),
                                    vertices->create(),
                                    true,              // bool ownsVertices
                                    indices.create(),
                                    true,              // bool ownsIndices
                                    1.0f,              // float lineWidth
                                    1.0f,              // float pointSize
                                    new Color(color),  // const Color* flatColor
                                    NULL,              // IFloatBuffer* colors
                                    true,              // bool depthTest
                                    normals->create());
  delete normals;
  delete vertices;

  return im;
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
  Ray camRay(ec->getCurrentCamera()->getCartesianPosition(),
             ec->getCurrentCamera()->pixel2Ray(touch.getPos()));

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
          _grabbedPos.set(arrowPoint);
          _baseWhenGrabbed.set(_base);
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
    _base.set(base);
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
