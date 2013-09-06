//
//  SectorAndHeightCameraConstrainer.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 06/09/13.
//
//

#ifndef __G3MiOSSDK__SectorAndHeightCameraConstrainer__
#define __G3MiOSSDK__SectorAndHeightCameraConstrainer__

#include <iostream>

#include "ICameraConstrainer.hpp"
#include "Sector.hpp"

class SectorAndHeightCameraConstrainer: public ICameraConstrainer {

  const Sector _sector;
  const double _height;

public:

  SectorAndHeightCameraConstrainer(const Sector& sector, double height): _sector(sector), _height(height){}

  ~SectorAndHeightCameraConstrainer() {
  }

  virtual void onCameraChange(const Planet* planet,
                              const Camera* previousCamera,
                              Camera* nextCamera) const;
  
};

#endif /* defined(__G3MiOSSDK__SectorAndHeightCameraConstrainer__) */
