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

const Planet* Planet::createEarth() {
  return new EllipsoidalPlanet(Vector3D(6378137.0, 6378137.0, 6356752.314245));
}

const Planet* Planet::createSphericalEarth(){
  return new SphericalPlanet(6378137.0);
}