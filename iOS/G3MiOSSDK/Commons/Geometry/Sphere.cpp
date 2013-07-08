//
//  Sphere.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 04/06/13.
//
//

#include "Sphere.hpp"
#include "Box.hpp"


double Sphere::projectedArea(const G3MRenderContext* rc) const {
//  AGUSTIN_TODO;
}


Vector2I Sphere::projectedExtent(const G3MRenderContext* rc) const {
  int TODO_remove_this; // Agustin: no implementes este método que va a desaparecer
  return Vector2I::zero();
}


void Sphere::render(const G3MRenderContext* rc,
                    const GLState& parentState) {
//  AGUSTIN_TODO;
}


bool Sphere::touchesFrustum(const Frustum *frustum) const {
  if (frustum->getNearPlane().signedDistance(_center)>_radius) return false;
  if (frustum->getFarPlane().signedDistance(_center)>_radius) return false;
  if (frustum->getLeftPlane().signedDistance(_center)>_radius) return false;
  if (frustum->getRightPlane().signedDistance(_center)>_radius) return false;
  if (frustum->getTopPlane().signedDistance(_center)>_radius) return false;
  if (frustum->getBottomPlane().signedDistance(_center)>_radius) return false;
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
  const Vector3D upper = that->getUpper();
  const Vector3D lower = that->getLower();
  double minX = _center._x - _radius;
  if (lower._x < minX) minX = lower._x;
  double maxX = _center._x + _radius;
  if (upper._x > maxX) maxX = upper._x;
  double minY = _center._y - _radius;
  if (lower._y < minY) minY = lower._y;
  double maxY = _center._y + _radius;
  if (upper._y > maxY) maxY = upper._y;
  double minZ = _center._z - _radius;
  if (lower._z < minZ) minZ = lower._z;
  double maxZ = _center._z + _radius;
  if (upper._z > maxZ) maxZ = upper._z;
  
  return new Box(Vector3D(minX, minY, minZ), Vector3D(maxX, maxY, maxZ));
  
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
  if (_center._x+_radius > upper._x) return false;
  if (_center._x-_radius < lower._x) return false;
  if (_center._y+_radius > upper._y) return false;
  if (_center._y-_radius < lower._y) return false;
  if (_center._z+_radius > upper._z) return false;
  if (_center._z-_radius < lower._z) return false;
  return true;
}


bool Sphere::fullContainedInSphere(const Sphere* that) const {
  const double d = _center.distanceTo(that->_center);
  return (d + _radius <= that->_radius);
}
