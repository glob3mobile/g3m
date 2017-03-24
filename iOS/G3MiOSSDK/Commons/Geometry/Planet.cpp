//
//  Planet.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 17/05/13.
//
//

#include "Planet.hpp"

#include "CoordinateSystem.hpp"
#include "Vector3D.hpp"
#include "Geodetic3D.hpp"
#include "Plane.hpp"
#include "MutableVector3D.hpp"
#include "MutableMatrix44D.hpp"


MutableMatrix44D Planet::createGeodeticTransformMatrix(const Geodetic3D& position) const {
  return createGeodeticTransformMatrix(position._latitude,
                                       position._longitude,
                                       position._height);
}

CoordinateSystem Planet::getCoordinateSystemAt(const Geodetic3D& position) const {

  const Vector3D origin = toCartesian(position);
  const Vector3D z = centricSurfaceNormal(origin);
  const Vector3D y = getNorth().projectionInPlane(z);
  const Vector3D x = y.cross(z);

  return CoordinateSystem(x, y, z, origin);
}

std::vector<double> Planet::intersectionsDistances(const Vector3D& origin,
                                                   const Vector3D& direction) const {
  return intersectionsDistances(origin._x,    origin._y,    origin._z,
                                direction._x, direction._y, direction._z);
}

std::vector<double> Planet::intersectionsDistances(const Vector3D& origin,
                                                   const MutableVector3D& direction) const {
  return intersectionsDistances(origin._x,     origin._y,     origin._z,
                                direction.x(), direction.y(), direction.z());
}

const Vector3D Planet::closestIntersection(const Vector3D& origin,
                                           const Vector3D& direction) const {
  if (origin.isNan() || direction.isNan()) {
    return Vector3D::NANV;
  }
  std::vector<double> distances = intersectionsDistances(origin._x,    origin._y,    origin._z,
                                                         direction._x, direction._y, direction._z);
  if (distances.empty()) {
    return Vector3D::NANV;
  }
  return origin.add(direction.times(distances[0]));
}
