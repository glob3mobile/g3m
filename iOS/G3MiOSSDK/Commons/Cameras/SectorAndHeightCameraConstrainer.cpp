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
        nextCamera->copyFrom(*previousCamera,
                             true);
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
            const Geodetic3D center = nextCamera->getGeodeticPosition();
//            const Sector extendedSector = sector->shrinkedByPercent(0.01f);
            
            Geodetic2D extension = Geodetic2D::fromDegrees(0.003, 0.003);
            const Sector extendedSector(sector->_lower.sub(extension),
                                        sector->_upper.add(extension));
            
//            printf("S: %s \n ES: %s\n", sector->description().c_str(), extendedSector.description().c_str());
            
            const bool isValidPosition = extendedSector.contains(center._latitude, center._longitude);
            
            if (isValidPosition) {
                if (!isValidHeight) {
                    nextCamera->setGeodeticPosition(Geodetic3D(position._latitude,
                                                               position._longitude,
                                                               _maxHeight));
                }
            }
            else {
                nextCamera->copyFrom(*previousCamera,
                                     true);
            }
        }
    }
    
    return true;
}
