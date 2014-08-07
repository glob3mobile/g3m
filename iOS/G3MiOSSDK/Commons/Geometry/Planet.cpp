//
//  Planet.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 17/05/13.
//
//

#include "Planet.hpp"

#include "EllipsoidalPlanet.hpp"
#include "SphericalPlanet.hpp"
#include "FlatPlanet.hpp"


#include "CoordinateSystem.hpp"
#include "Vector3D.hpp"
#include "Geodetic3D.hpp"
#include "Plane.hpp"

const Planet* Planet::createEarth() {
  return new EllipsoidalPlanet(Ellipsoid(Vector3D::zero,
                                         Vector3D(6378137.0, 6378137.0, 6356752.314245)));
}

const Planet* Planet::createSphericalEarth() {
  return new SphericalPlanet(Sphere(Vector3D::zero,
                                    6378137.0));
}

const Planet* Planet::createFlatEarth() {
  return new FlatPlanet(Vector2D(4*6378137.0, 2*6378137.0));
}

CoordinateSystem Planet::getCoordinateSystemAt(const Geodetic3D& geo) const {

  Vector3D origin = toCartesian(geo);
  Vector3D z = centricSurfaceNormal(origin);
  Vector3D y = getNorth().projectionInPlane(z);
  Vector3D x = y.cross(z);

  return CoordinateSystem(x,y,z, origin);
}

std::vector<double> Planet::intersectionsDistances(const Vector3D& origin,
                                                   const Vector3D& direction) const {
  return intersectionsDistances(origin._x,
                                origin._y,
                                origin._z,
                                direction._x,
                                direction._y,
                                direction._z);
}

std::vector<double> Planet::intersectionsDistances(const Vector3D& origin,
                                                   const MutableVector3D& direction) const {
  return intersectionsDistances(origin._x,
                                origin._y,
                                origin._z,
                                direction.x(),
                                direction.y(),
                                direction.z());
}

Vector3D Planet::closestIntersection(const Vector3D& pos,
                                              const Vector3D& ray) const {
  if (pos.isNan() || ray.isNan()) {
    return Vector3D::nan();
  }
  std::vector<double> distances = intersectionsDistances(pos._x,
                                                         pos._y,
                                                         pos._z,
                                                         ray._x,
                                                         ray._y,
                                                         ray._z);
  if (distances.empty()) {
    return Vector3D::nan();
  }
  return pos.add(ray.times(distances[0]));
}

