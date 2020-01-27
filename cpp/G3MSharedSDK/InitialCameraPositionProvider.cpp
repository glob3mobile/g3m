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
  const Sector* sector = (planetRenderer == NULL) ? NULL : planetRenderer->getRenderedSector();
  return ((sector == NULL)
          ? planet->getDefaultCameraPosition(Sector::FULL_SPHERE)
          : planet->getDefaultCameraPosition(*sector));
}
