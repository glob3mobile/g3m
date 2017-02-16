//
//  SimpleCameraConstrainer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/30/13.
//
//

#ifndef __G3MiOSSDK__SimpleCameraConstrainer__
#define __G3MiOSSDK__SimpleCameraConstrainer__

#include "ICameraConstrainer.hpp"


class SimpleCameraConstrainer : public ICameraConstrainer {
private:
  const double _minHeight;
  const double _maxHeight;
  const double _minHeightPlanetRadiiFactor;
  const double _maxHeightPlanetRadiiFactor;

  SimpleCameraConstrainer(const double minHeight,
                          const double maxHeight,
                          const double minHeightPlanetRadiiFactor,
                          const double maxHeightPlanetRadiiFactor) :
  _minHeight(minHeight),
  _maxHeight(maxHeight),
  _minHeightPlanetRadiiFactor(minHeightPlanetRadiiFactor),
  _maxHeightPlanetRadiiFactor(maxHeightPlanetRadiiFactor)
  {
  }

public:

  static SimpleCameraConstrainer* create(const double minHeight,
                                         const double maxHeight,
                                         const double minHeightPlanetRadiiFactor,
                                         const double maxHeightPlanetRadiiFactor);

  static SimpleCameraConstrainer* createDefault();

  static SimpleCameraConstrainer* createFixed(const double minHeight,
                                              const double maxHeight);

  static SimpleCameraConstrainer* createPlanetRadiiFactor(const double minHeightPlanetRadiiFactor,
                                                          const double maxHeightPlanetRadiiFactor);


  ~SimpleCameraConstrainer() {
  }

  bool onCameraChange(const Planet* planet,
                      const Camera* previousCamera,
                      Camera* nextCamera) const;
  
};

#endif
