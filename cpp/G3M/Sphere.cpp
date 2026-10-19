//
//  Sphere.cpp
//  G3M
//
//  Created by Jose Miguel SN on 04/06/13.
//
//

#include "Sphere.hpp"
#include "Box.hpp"
#include "Camera.hpp"
#include "G3MRenderContext.hpp"
#include "Ray.hpp"
#include "IMathUtils.hpp"
#include "Ellipsoid.hpp"


Sphere::~Sphere() {
#ifdef JAVA_CODE
  super.dispose();
#endif

}


Sphere* Sphere::enclosingSphere(const std::vector<Vector3D>& points,
                                const double radiusDelta) {
  const size_t size = points.size();

  if (size < 2) {
    return NULL;
  }

  const Vector3D p0 = points[0];
  double xMin = p0._x;
  double xMax = p0._x;
  double yMin = p0._y;
  double yMax = p0._y;
  double zMin = p0._z;
  double zMax = p0._z;

  for (size_t i = 1; i < size; i++) {
    const Vector3D pi = points[i];
    const double x = pi._x;
    const double y = pi._y;
    const double z = pi._z;

    if (x < xMin) { xMin = x; }
    if (x > xMax) { xMax = x; }
    if (y < yMin) { yMin = y; }
    if (y > yMax) { yMax = y; }
    if (z < zMin) { zMin = z; }
    if (z > zMax) { zMax = z; }
  }

  const Vector3D center = Vector3D(xMin/2 + xMax/2,
                                   yMin/2 + yMax/2,
                                   zMin/2 + zMax/2);
  double squaredRadius = center.squaredDistanceTo(p0);
  for (size_t i = 1; i < size; i++) {
    const Vector3D pi = points[i];
    const double squaredRadiusI = center.squaredDistanceTo(pi);
    if (squaredRadiusI > squaredRadius) {
      squaredRadius = squaredRadiusI;
    }
  }

  const double radius = IMathUtils::instance()->sqrt(squaredRadius) + radiusDelta;
  return new Sphere(center, radius);
}

double Sphere::projectedArea(const G3MRenderContext* rc) const {
  return rc->getCurrentCamera()->getProjectedSphereArea(*this);
}

bool Sphere::touchesFrustum(const Frustum *frustum) const {
#warning This implementation could gives false positives
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

Sphere* Sphere::mergedWithSphere(const Sphere* that) const {
  return mergedWithSphere(that, 0.000001);
}

Sphere* Sphere::mergedWithSphere(const Sphere* that,
                                 const double radiusDelta) const {
  // from Real-Time Collision Detection - Christer Ericson
  //   page 268
  const IMathUtils* mu = IMathUtils::instance();

  const Sphere* s0 = this;
  const Sphere* s1 = that;

  // Compute the squared distance between the sphere centers
  const Vector3D d = s1->_center.sub( s0->_center );
  const double dist2 = d.dot(d);

  if (mu->squared(s1->_radius - s0->_radius) >= dist2) {
    // The sphere with the larger radius encloses the other;
    // just set s to be the larger of the two spheres
    if (s1->_radius >= s0->_radius) {
      return new Sphere(*s1);
    }
    return new Sphere(*s0);
  }

  // Spheres partially overlapping or disjoint
  const double dist = mu->sqrt(dist2);

  const double   radius = (dist/2 + s0->_radius/2 + s1->_radius/2) + radiusDelta;
  const Vector3D center = ((dist > 0)
                           ? s0->_center.add( d.times( (radius - s0->_radius) / dist ) )
                           : s0->_center);

  return new Sphere(center, radius);
}

bool Sphere::contains(const Vector3D& point) const {
  return _center.squaredDistanceTo(point) <= _radiusSquared;
}

bool Sphere::contains(const MutableVector3D& point) const {
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
  if (_radius <= that->_radius) {
    const double distance    = _center.distanceTo(that->_center);
    const double deltaRadius = that->_radius - _radius;
    if (deltaRadius >= distance) {
      return true;
    }
  }

  return false;
}

Sphere* Sphere::createSphere() const {
  return new Sphere(*this);
}

Sphere* Sphere::copy() const {
  return new Sphere(*this);
}

const bool Sphere::touchesRay(const Ray* ray) const {
  // from Real-Time Collision Detection - Christer Ericson
  //   page 179

  const Vector3D m = ray->_origin.sub(_center);

  const double c = m.dot(m) - _radiusSquared;
  // If there is definitely at least one real root, there must be an intersection
  if (c <= 0) {
    return true;
  }

  const double b = m.dot(ray->_direction);
  // Early exit if ray origin outside sphere and ray pointing away from sphere
  if (b > 0) {
    return false;
  }

  const double discr = (b * b) - c;
  // A negative discriminant corresponds to ray missing sphere
  return (discr >= 0.0);
}
