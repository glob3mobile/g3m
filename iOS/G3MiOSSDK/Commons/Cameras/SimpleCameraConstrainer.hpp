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

class Camera;


class SimpleCameraConstrainer : public ICameraConstrainer {
public:

  SimpleCameraConstrainer():
  _lastValidCamera(NULL)
  {}
  
  ~SimpleCameraConstrainer() {

  }

  virtual void onCameraChange(const Planet* planet,
                              const Camera* previousCamera,
                              Camera* nextCamera) const;

private:
  Camera* _lastValidCamera;
};

#endif
