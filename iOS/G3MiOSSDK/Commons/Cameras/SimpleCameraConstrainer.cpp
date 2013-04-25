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
  const double maxHeight = radii*9;
  const double minHeight = 10;

  const Geodetic3D cameraPosition = nextCamera->getGeodeticPosition();
  const double cameraHeight = cameraPosition.height();

  if (cameraHeight > maxHeight) {
    nextCamera->setGeodeticPosition(cameraPosition.latitude(),
                                    cameraPosition.longitude(),
                                    maxHeight);
  }
  else if (cameraHeight < minHeight) {
    nextCamera->setGeodeticPosition(cameraPosition.latitude(),
                                    cameraPosition.longitude(),
                                    minHeight);
  }
  
}
