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
//  const double height = nextCamera->getHeight();
  const double height = position._height;

  const Geodetic3D center = nextCamera->getGeodeticCenterOfView();

  const bool invalidHeight   = (height > _maxHeight);
//  const bool invalidPosition = !_sector.contains(position._latitude, position._longitude);
  const bool invalidPosition = !_sector.contains(center._latitude, center._longitude);

  if (invalidHeight || invalidPosition) {
    nextCamera->copyFrom(*previousCamera);

//    const double newHeight = invalidHeight ? _maxHeight : height;
//    if (invalidPosition) {
//      nextCamera->setGeodeticPosition(_sector.clamp(g2), newHeight);
//    }
//    else {
//      nextCamera->setGeodeticPosition(g2, newHeight);
//    }

    return true;
  }
  
}

bool RenderedSectorCameraConstrainer::onCameraChange(const Planet* planet,
                                                      const Camera* previousCamera,
                                                      Camera* nextCamera) const{

  Sector sector = _planetRenderer->getRenderedSector();
//  if (!nextCamera->isCenterOfViewWithin(sector, _maxHeight)){
//    if (previousCamera->isCenterOfViewWithin(sector, _maxHeight)){
//      nextCamera->copyFrom(*previousCamera);
//      return true;
//    }
//  } else{
//    return false;
//  }

  const Geodetic3D position = nextCamera->getGeodeticPosition();
  const double height = position._height;

  const Geodetic3D center = nextCamera->getGeodeticCenterOfView();

  const bool invalidHeight   = (height > _maxHeight);
  const bool invalidPosition = !sector.contains(center._latitude, center._longitude);

  if (invalidHeight && !invalidPosition){
    Geodetic3D newPos(position._latitude, position._longitude, _maxHeight);
    nextCamera->setGeodeticPosition(newPos);
    return true;
  } else{
    if (invalidPosition){
      if (previousCamera->isCenterOfViewWithin(sector, _maxHeight)){
        nextCamera->copyFrom(*previousCamera);
        return true;
      } else{
        return false;
      }
    }
    return true;
  }

}
