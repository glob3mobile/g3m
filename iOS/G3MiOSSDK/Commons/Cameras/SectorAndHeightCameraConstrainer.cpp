//
//  SectorAndHeightCameraConstrainer.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 06/09/13.
//
//

#include "SectorAndHeightCameraConstrainer.hpp"

#include "PlanetRenderer.hpp"
#include "Camera.hpp"

bool SectorAndHeightCameraConstrainer::onCameraChange(const Planet* planet,
                                                      const Camera* previousCamera,
                                                      Camera* nextCamera) const{

  const Geodetic3D position = nextCamera->getGeodeticPosition();
  const double height = position._height;

  const Geodetic3D center = nextCamera->getGeodeticCenterOfView();

  const bool invalidHeight   = (height > _maxHeight);
  const bool invalidPosition = !_sector.contains(center._latitude, center._longitude);

  if (invalidHeight || invalidPosition) {
    nextCamera->copyFrom(*previousCamera);

  }

  return true;
}

bool RenderedSectorCameraConstrainer::onCameraChange(const Planet* planet,
                                                     const Camera* previousCamera,
                                                     Camera* nextCamera) const{

  const Sector* sector = _planetRenderer->getRenderedSector();

  const Geodetic3D position = nextCamera->getGeodeticPosition();
  const double height = position._height;

  const Geodetic3D center = nextCamera->getGeodeticCenterOfView();

  const bool invalidHeight   = (height > _maxHeight);
  const bool invalidPosition = sector== NULL?
                                              false :
                                              !sector->contains(center._latitude, center._longitude);

  if (invalidHeight && !invalidPosition) {
    Geodetic3D newPos(position._latitude, position._longitude, _maxHeight);
    nextCamera->setGeodeticPosition(newPos);
    return true;
  }

  if (invalidPosition) {

    bool previousCameraWasValid = previousCamera->getHeight() < _maxHeight;
    if (previousCameraWasValid && sector != NULL) {
      const Geodetic3D centerPosition = previousCamera->getGeodeticCenterOfView();
      previousCameraWasValid = sector->contains(centerPosition._latitude, centerPosition._longitude);
    }

    if (previousCameraWasValid) {
      nextCamera->copyFrom(*previousCamera);
      return true;
    }
    return false;
  }

  return true;
  
}
