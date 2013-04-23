//
//  SimpleCameraConstrainer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/30/13.
//
//

#include "SimpleCameraConstrainer.hpp"

#include "Camera.hpp"

void SimpleCameraConstrainer::onCameraChange(const Planet *planet,
                                             const Camera* previousCamera,
                                             Camera* nextCamera) const {

  const double radii = planet->getRadii().maxAxis();

  const Geodetic3D cameraPosition3D = planet->toGeodetic3D(nextCamera->getCartesianPosition());
  const double cameraHeight = cameraPosition3D.height();

  if (cameraHeight > radii*9) {
    nextCamera->resetPosition();
    nextCamera->setPosition(  planet->toGeodetic3D(previousCamera->getCartesianPosition())  );
  }
}
