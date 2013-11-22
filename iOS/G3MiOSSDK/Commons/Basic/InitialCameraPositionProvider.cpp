//
//  Geodetic3DProvider.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 12/09/13.
//
//

#include "InitialCameraPositionProvider.hpp"

#include "Planet.hpp"
#include "PlanetRenderer.hpp"

Geodetic3D SimpleInitialCameraPositionProvider::getCameraPosition(const Planet* planet,
                                                                  const PlanetRenderer* planetRenderer) const {
  const Sector* sector = planetRenderer->getRenderedSector();
  if (sector == NULL) {
    return planet->getDefaultCameraPosition(Sector::fullSphere());
  }

  return planet->getDefaultCameraPosition(*sector);
}
