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
