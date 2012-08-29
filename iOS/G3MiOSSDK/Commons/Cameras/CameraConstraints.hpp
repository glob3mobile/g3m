//
//  CameraConstraints.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 09/08/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#ifndef G3MiOSSDK_CameraConstraints_hpp
#define G3MiOSSDK_CameraConstraints_hpp

#include "Camera.hpp"


class ICameraConstrainer {
public:
#ifdef JAVA_CODE
  //NO DESTRUCTOR FOR INTERFACE
#endif
#ifdef C_CODE
  virtual ~ICameraConstrainer() {}
#endif

  virtual void onCameraChange(const Planet *planet,
                              const Camera* previousCamera,
                              Camera* nextCamera) const = 0;
};


class SimpleCameraConstrainer : public ICameraConstrainer {
public:
  void onCameraChange(const Planet *planet,
                      const Camera* previousCamera,
                      Camera* nextCamera) const {

    const double radii = planet->getRadii().maxAxis();
    
    const Geodetic3D cameraPosition3D = planet->toGeodetic3D(nextCamera->getCartesianPosition());
    const double cameraHeight = cameraPosition3D.height();
    
    if (cameraHeight > radii*9) {
      nextCamera->reset();
      nextCamera->setPosition(  planet->toGeodetic3D(previousCamera->getCartesianPosition())  );
    }
  }
  
  
  //  bool acceptsCamera(const Camera* camera,
  //                     const Planet *planet) const {
  //    const double distance = camera->getPosition().length();
  //    const double radii    = planet->getRadii().maxAxis();
  //    if (distance > radii*10) {
  //      // printf ("--- camera constraint!\n");
  //      return false;
  //    }
  //
  //    return true;
  //  }
};

#endif
