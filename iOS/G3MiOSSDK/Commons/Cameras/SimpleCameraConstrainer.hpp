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
public:

  ~SimpleCameraConstrainer() {
#ifdef JAVA_CODE
  super.dispose();
#endif

  }

  virtual void onCameraChange(const Planet* planet,
                              const Camera* previousCamera,
                              Camera* nextCamera) const;

};

#endif
