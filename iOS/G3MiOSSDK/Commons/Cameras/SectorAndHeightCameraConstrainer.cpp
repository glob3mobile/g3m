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
                                                      Camera* nextCamera) const {

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
                                                     Camera* nextCamera) const {
  if (_planetRenderer != NULL) {
    const Sector* sector = _planetRenderer->getRenderedSector();
    const Geodetic3D position = nextCamera->getGeodeticPosition();
    const bool isValidHeight = (position._height <= _maxHeight);

    if (sector == NULL) {
      if (!isValidHeight) {
        nextCamera->setGeodeticPosition(Geodetic3D(position._latitude,
                                                   position._longitude,
                                                   _maxHeight));
      }
    }
    else {
      const Geodetic3D center = nextCamera->getGeodeticCenterOfView();
      const bool isValidPosition = sector->contains(center._latitude, center._longitude);

      if (isValidPosition) {
        if (!isValidHeight) {
          nextCamera->setGeodeticPosition(Geodetic3D(position._latitude,
                                                     position._longitude,
                                                     _maxHeight));
        }
      }
      else {
        nextCamera->copyFrom(*previousCamera);
      }
    }
  }

  return true;
}
