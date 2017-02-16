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
#include "IMathUtils.hpp"


SimpleCameraConstrainer* SimpleCameraConstrainer::create(const double minHeight,
                                                         const double maxHeight,
                                                         const double minHeightPlanetRadiiFactor,
                                                         const double maxHeightPlanetRadiiFactor) {
  return new SimpleCameraConstrainer(minHeight,
                                     maxHeight,
                                     minHeightPlanetRadiiFactor,
                                     maxHeightPlanetRadiiFactor);
}


SimpleCameraConstrainer* SimpleCameraConstrainer::createDefault() {
  const double minHeight                  = 10;
  const double minHeightPlanetRadiiFactor = NAND;

  const double maxHeight                  = NAND;
  const double maxHeightPlanetRadiiFactor = 9;

  return create(minHeight,
                maxHeight,
                minHeightPlanetRadiiFactor,
                maxHeightPlanetRadiiFactor);
}

SimpleCameraConstrainer* SimpleCameraConstrainer::createFixed(const double minHeight,
                                                              const double maxHeight) {
  const double minHeightPlanetRadiiFactor = NAND;
  const double maxHeightPlanetRadiiFactor = NAND;

  return create(minHeight,
                maxHeight,
                minHeightPlanetRadiiFactor,
                maxHeightPlanetRadiiFactor);
}

SimpleCameraConstrainer* SimpleCameraConstrainer::createPlanetRadiiFactor(const double minHeightPlanetRadiiFactor,
                                                                          const double maxHeightPlanetRadiiFactor) {
  const double minHeight = NAND;
  const double maxHeight = NAND;

  return create(minHeight,
                maxHeight,
                minHeightPlanetRadiiFactor,
                maxHeightPlanetRadiiFactor);
}

\
bool SimpleCameraConstrainer::onCameraChange(const Planet* planet,
                                             const Camera* previousCamera,
                                             Camera* nextCamera) const {
  const double radii = planet->getRadii().maxAxis();
  const double minHeight = ISNAN(_minHeight) ? (radii * _minHeightPlanetRadiiFactor) : _minHeight;
  const double maxHeight = ISNAN(_maxHeight) ? (radii * _maxHeightPlanetRadiiFactor) : _maxHeight;

  const Geodetic3D cameraPosition = nextCamera->getGeodeticPosition();

  if (cameraPosition._height < minHeight) {
    nextCamera->setGeodeticPosition(cameraPosition._latitude,
                                    cameraPosition._longitude,
                                    minHeight);
  }
  else if (cameraPosition._height > maxHeight) {
    nextCamera->setGeodeticPosition(cameraPosition._latitude,
                                    cameraPosition._longitude,
                                    maxHeight);
  }

  return true;
}
