//
//  SectorAndHeightCameraConstrainer.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 06/09/13.
//
//

#include "SectorAndHeightCameraConstrainer.hpp"

void SectorAndHeightCameraConstrainer::onCameraChange(const Planet* planet,
                            const Camera* previousCamera,
                    Camera* nextCamera) const{

  Geodetic3D g = nextCamera->getGeodeticPosition();
  double h = nextCamera->getHeight();

  if (h > _height || !_sector.contains(g)){
    nextCamera->copyFrom(*previousCamera);
  }
  
}