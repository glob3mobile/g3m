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

class PlanetRenderer;

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

  virtual bool onCameraChange(const Planet* planet,
                              const Camera* previousCamera,
                              Camera* nextCamera) const;

};

class RenderedSectorCameraConstrainer: public ICameraConstrainer {
private:
  const double _maxHeight;
  const double _margin;
  const PlanetRenderer* _planetRenderer;

public:

  RenderedSectorCameraConstrainer(PlanetRenderer* planetRenderer,
                                  double maxHeight,
                                  double margin) :
  _planetRenderer(planetRenderer),
  _maxHeight(maxHeight),
  _margin(margin)
  {
  }

  ~RenderedSectorCameraConstrainer() {
  }

  virtual bool onCameraChange(const Planet* planet,
                              const Camera* previousCamera,
                              Camera* nextCamera) const;
  
};

#endif
