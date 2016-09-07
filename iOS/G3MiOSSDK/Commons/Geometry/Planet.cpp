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



MutableMatrix44D Planet::createGeodeticTransformMatrix(const Geodetic3D& position) const {
  return createGeodeticTransformMatrix(position._latitude,
                                       position._longitude,
                                       position._height);
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
  return intersectionsDistances(origin._x,    origin._y,    origin._z,
                                direction._x, direction._y, direction._z);
}

std::vector<double> Planet::intersectionsDistances(const Vector3D& origin,
                                                   const MutableVector3D& direction) const {
  return intersectionsDistances(origin._x,     origin._y,     origin._z,
                                direction.x(), direction.y(), direction.z());
}

Vector3D Planet::closestIntersection(const Vector3D& pos,
                                     const Vector3D& ray) const {
  if (pos.isNan() || ray.isNan()) {
    return Vector3D::nan();
  }
  std::vector<double> distances = intersectionsDistances(pos._x, pos._y, pos._z,
                                                         ray._x, ray._y, ray._z);
  if (distances.empty()) {
    return Vector3D::nan();
  }
  return pos.add(ray.times(distances[0]));
}
