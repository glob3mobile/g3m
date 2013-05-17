//
//  Planet.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 17/05/13.
//
//

#include "Planet.hpp"

#include "Ellipsoid.hpp"

const Planet* Planet::createEarth() {
  return new Ellipsoid(Vector3D(6378137.0, 6378137.0, 6356752.314245));
}