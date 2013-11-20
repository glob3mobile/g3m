//
//  Sphere.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 04/06/13.
//
//

#include "Sphere.hpp"
#include "Box.hpp"
#include "Camera.hpp"
#include "ShortBufferBuilder.hpp"
#include "IndexedMesh.hpp"
#include "GLConstants.hpp"
#include "FloatBufferBuilderFromCartesian3D.hpp"


double Sphere::projectedArea(const G3MRenderContext* rc) const {
  return rc->getCurrentCamera()->getProjectedSphereArea(*this);
}

//Vector2I Sphere::projectedExtent(const G3MRenderContext* rc) const {
//  int TODO_remove_this; // Agustin: no implementes este método que va a desaparecer
//  return Vector2I::zero();
//}

void Sphere::createWireframeMesh(Color* color,
                                 short resolution) const {
  IMathUtils* mu = IMathUtils::instance();
  const double delta = PI / (resolution-1);

  // create vertices
  FloatBufferBuilderFromCartesian3D* vertices = FloatBufferBuilderFromCartesian3D::builderWithFirstVertexAsCenter();
  for (int i=0; i<2*resolution-2; i++) {
    const double longitude = -PI + i*delta;
    for (int j=0; j<resolution; j++) {
      const double latitude = -PI/2 + j*delta;
      const double h = mu->cos(latitude);
      const double x = h * mu->cos(longitude);
      const double y = h * mu->sin(longitude);
      const double z = mu->sin(latitude);
      vertices->add(Vector3D(x,y,z).times(_radius).add(_center));
    }
  }

  // create border indices for vertical lines
  ShortBufferBuilder indices;
  for (short i=0; i<2*resolution-2; i++) {
    for (short j=0; j<resolution-1; j++) {
      indices.add((short) (j+i*resolution));
      indices.add((short) (j+1+i*resolution));
    }
  }

  // create border indices for horizontal lines
  for (short j=1; j<resolution-1; j++) {
    for (short i=0; i<2*resolution-3; i++) {
      indices.add((short) (j+i*resolution));
      indices.add((short) (j+(i+1)*resolution));
    }
  }
  for (short j=1; j<resolution-1; j++) {
    const short i = (short) (2*resolution-3);
    indices.add((short) (j+i*resolution));
    indices.add((short) (j));
  }

  _mesh = new IndexedMesh(GLPrimitive::lines(),
                          true,
                          vertices->getCenter(),
                          vertices->create(),
                          indices.create(),
                          1,
                          1,
                          color);

  delete vertices;
}


void Sphere::render(const G3MRenderContext* rc,
                    const GLState& parentState) const {
  if (_mesh == NULL) {
    createWireframeMesh(Color::newFromRGBA(1.0f, 1.0f, 0.0f, 1.0f), (short) 16);
  }
  _mesh->render(rc, &parentState);
}


bool Sphere::touchesFrustum(const Frustum *frustum) const {
  // this implementation is not right exact, but it's faster.
  if (frustum->getNearPlane().signedDistance(_center)   > _radius) return false;
  if (frustum->getFarPlane().signedDistance(_center)    > _radius) return false;
  if (frustum->getLeftPlane().signedDistance(_center)   > _radius) return false;
  if (frustum->getRightPlane().signedDistance(_center)  > _radius) return false;
  if (frustum->getTopPlane().signedDistance(_center)    > _radius) return false;
  if (frustum->getBottomPlane().signedDistance(_center) > _radius) return false;
  return true;
}


bool Sphere::touchesBox(const Box* that) const {
  const Vector3D p = that->closestPoint(_center);
  const Vector3D v = p.sub(_center);
  return v.dot(v) <= (_radius * _radius);
}


bool Sphere::touchesSphere(const Sphere* that) const {
  const Vector3D d = _center.sub(that->_center);
  const double squaredDist = d.dot(d);
  const double radiusSum = _radius + that->_radius;
  return squaredDist <= (radiusSum * radiusSum);
}


BoundingVolume* Sphere::mergedWithBox(const Box* that) const {
  if (that->fullContainedInSphere(this)) {
    return new Sphere(*this);
  }

  const Vector3D upper = that->getUpper();
  const Vector3D lower = that->getLower();

  double minX = _center._x - _radius;
  if (lower._x < minX) { minX = lower._x; }

  double maxX = _center._x + _radius;
  if (upper._x > maxX) { maxX = upper._x; }

  double minY = _center._y - _radius;
  if (lower._y < minY) { minY = lower._y; }

  double maxY = _center._y + _radius;
  if (upper._y > maxY) { maxY = upper._y; }

  double minZ = _center._z - _radius;
  if (lower._z < minZ) { minZ = lower._z; }

  double maxZ = _center._z + _radius;
  if (upper._z > maxZ) { maxZ = upper._z; }

  return new Box(Vector3D(minX, minY, minZ),
                 Vector3D(maxX, maxY, maxZ));

  /* Diego: creo que este test ya no hace falta, porque el coste del método
   fullContainedInBox es casi tanto es casi similar a todo lo anterior
   if (fullContainedInBox(that)) {
   return new Box(*that);
   }
   if (that->fullContainedInSphere(this)) {
   return new Sphere(*this);
   }*/

}


BoundingVolume* Sphere::mergedWithSphere(const Sphere* that) const {
  const double d = _center.distanceTo(that->_center);

  if (d + that->_radius <= _radius) {
    return new Sphere(*this);
  }
  if (d + _radius <= that->_radius)  {
    return new Sphere(*that);
  }

  const double radius = (d + _radius + that->_radius) / 2.0;
  const Vector3D u = _center.sub(that->_center).normalized();
  const Vector3D center = _center.add( u.times( radius - _radius ) );

  return new Sphere(center, radius);
}


bool Sphere::contains(const Vector3D& point) const {
  return _center.squaredDistanceTo(point) <= _radiusSquared;
}


bool Sphere::fullContainedInBox(const Box* that) const {
  const Vector3D upper = that->getUpper();
  const Vector3D lower = that->getLower();
  if (_center._x + _radius > upper._x) return false;
  if (_center._x - _radius < lower._x) return false;
  if (_center._y + _radius > upper._y) return false;
  if (_center._y - _radius < lower._y) return false;
  if (_center._z + _radius > upper._z) return false;
  if (_center._z - _radius < lower._z) return false;
  return true;
}


bool Sphere::fullContainedInSphere(const Sphere* that) const {
  const double d = _center.distanceTo(that->_center);
  return (d + _radius <= that->_radius);
}

Sphere* Sphere::createSphere() const {
  return new Sphere(*this);
}
