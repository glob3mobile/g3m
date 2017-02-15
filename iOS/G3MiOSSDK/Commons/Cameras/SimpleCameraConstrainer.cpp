//
//  SimpleCameraConstrainer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/30/13.
//
//

#include "SimpleCameraConstrainer.hpp"

#include "Camera.hpp"
#include "Planet.hpp"
#include "Geodetic3D.hpp"


bool SimpleCameraConstrainer::onCameraChange(const Planet *planet,
                                             const Camera* previousCamera,
                                             Camera* nextCamera) const {
  const double radii = planet->getRadii().maxAxis();
  const double maxHeight = radii*9;
  const double minHeight = 10;

  const double height = nextCamera->getGeodeticPosition()._height;

  if ((height < minHeight) || (height > maxHeight)) {
    nextCamera->copyFrom(*previousCamera,
                         true);
  }

  return true;
}
