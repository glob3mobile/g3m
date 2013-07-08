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
  AGUSTIN_TODO;
}

Vector2I Sphere::projectedExtent(const G3MRenderContext* rc) const {
  int TODO_remove_this; // Agustin: no implementes este método que va a desaparecer
  return Vector2I::zero();
}

void Sphere::render(const G3MRenderContext* rc,
                    const GLState& parentState) {
  AGUSTIN_TODO;
}

bool Sphere::touchesFrustum(const Frustum *frustum) const {
  AGUSTIN_TODO;
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
  if (fullContainedInBox(that)) {
    return new Box(*that);
  }

  if (that->fullContainedInSphere(this)) {
    return new Sphere(*this);
  }

  AGUSTIN_TODO;
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
  AGUSTIN_TODO;
}

bool Sphere::fullContainedInSphere(const Sphere* that) const {
  const double d = _center.distanceTo(that->_center);
  return (d + _radius <= that->_radius);
}
