//
//  SimpleCameraConstrainer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/30/13.
//
//

#include "SimpleCameraConstrainer.hpp"

#include "Camera.hpp"

bool SimpleCameraConstrainer::onCameraChange(const Planet *planet,
                                             const Camera* previousCamera,
                                             Camera* nextCamera) const {

  //  long long previousCameraTimestamp = previousCamera->getTimestamp();
  //  long long nextCameraTimestamp = nextCamera->getTimestamp();
  //  if (previousCameraTimestamp != _previousCameraTimestamp || nextCameraTimestamp != _nextCameraTimestamp) {
  //    _previousCameraTimestamp = previousCameraTimestamp;
  //    _nextCameraTimestamp = nextCameraTimestamp;
  //    ILogger::instance()->logInfo("Cameras Timestamp: Previous=%lld; Next=%lld\n",
  //                                 _previousCameraTimestamp, _nextCameraTimestamp);
  //  }

  const double radii = planet->getRadii().maxAxis();
  const double maxHeight = radii*9;
  const double minHeight = 10;

  const double height = nextCamera->getGeodeticPosition()._height;

  if ((height < minHeight) || (height > maxHeight)) {
    nextCamera->copyFrom(*previousCamera);
  }
  
  return true;
}
