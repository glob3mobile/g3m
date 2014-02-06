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

CoordinateSystem Planet::getCoordinateSystemAt(const Geodetic3D& geo) const{

  Vector3D origin = toCartesian(geo);
  Vector3D z = centricSurfaceNormal(origin);
  Vector3D y = getNorth().projectionInPlane(z);
  Vector3D x = y.cross(z);

  return CoordinateSystem(x,y,z, origin);
}
