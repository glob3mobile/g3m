//
//  ICameraConstrainer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/30/13.
//
//

#ifndef __G3MiOSSDK__ICameraConstrainer__
#define __G3MiOSSDK__ICameraConstrainer__

//class Planet;
//class Camera;
#include "Camera.hpp"

class ICameraConstrainer {
public:
#ifdef JAVA_CODE
  //NO DESTRUCTOR FOR INTERFACE
#endif
#ifdef C_CODE
  virtual ~ICameraConstrainer() {}
#endif

  virtual void onCameraChange(const Planet* planet,
                              const Camera* previousCamera,
                              Camera* nextCamera) const = 0;
};

#endif
