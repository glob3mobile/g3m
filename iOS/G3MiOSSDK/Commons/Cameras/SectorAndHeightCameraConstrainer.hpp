//
//  SectorAndHeightCameraConstrainer.hpp
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
private:
  const Sector _sector;
  const double _maxHeight;

public:

  SectorAndHeightCameraConstrainer(const Sector& sector,
                                   double maxHeight) :
  _sector(sector),
  _maxHeight(maxHeight)
  {
  }

  ~SectorAndHeightCameraConstrainer() {
  }

  virtual void onCameraChange(const Planet* planet,
                              const Camera* previousCamera,
                              Camera* nextCamera) const;

};

#endif
