//
//  DeviceOrientationCameraConstrainer.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 7/5/15.
//
//

#ifndef __G3MiOSSDK__DeviceOrientationCameraConstrainer__
#define __G3MiOSSDK__DeviceOrientationCameraConstrainer__

#include "ICameraConstrainer.hpp"


/**
 Class that applies the Rotation obtained with IDeviceAttitude and IInterfaceOrientation to the camera.
 
 It translate to the Global Coordinate System to the Local CS on the camera geodetic location.
 
 **/

class DeviceOrientationCameraConstrainer: public ICameraConstrainer{
public:
  
  DeviceOrientationCameraConstrainer(){
  };
  
  bool onCameraChange(const Planet* planet,
                      const Camera* previousCamera,
                      Camera* nextCamera) const;
};

#endif /* defined(__G3MiOSSDK__DeviceOrientationCameraConstrainer__) */
