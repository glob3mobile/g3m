//
//  DynamicFrustumPolicy.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/13/17.
//
//

#include "DynamicFrustumPolicy.hpp"

#include "Vector2D.hpp"
#include "Camera.hpp"
#include "Geodetic3D.hpp"
#include "Planet.hpp"


DynamicFrustumPolicy::~DynamicFrustumPolicy() {
#ifdef JAVA_CODE
  super.dispose();
#endif
}

const Vector2D DynamicFrustumPolicy::calculateFrustumZNearAndZFar(const Camera& camera) const {
  const double height = camera.getGeodeticHeight();
  double zNear = height * 0.1;

  const double zFar = camera.getPlanet()->distanceToHorizon(camera.getCartesianPosition());

  const double goalRatio = 1000;
  const double ratio = zFar / zNear;
  if (ratio < goalRatio) {
    zNear = zFar / goalRatio;
  }

  return Vector2D(zNear, zFar);
}

const FrustumPolicy* DynamicFrustumPolicy::copy() const {
  return new DynamicFrustumPolicy();
}
