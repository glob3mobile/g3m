//
//  DeviceAttitudeCameraConstrainer.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 7/5/15.
//
//

#ifndef __G3MiOSSDK__DeviceAttitudeCameraConstrainer__
#define __G3MiOSSDK__DeviceAttitudeCameraConstrainer__

#include "ICameraConstrainer.hpp"


/**
 Class that applies the Rotation obtained with IDeviceAttitude and IInterfaceOrientation to the camera.
 
 It translate to the Global Coordinate System to the Local CS on the camera geodetic location.
 
 **/

class DeviceAttitudeCameraConstrainer: public ICameraConstrainer{
  
private:
  mutable MutableMatrix44D _localRM;
  mutable MutableMatrix44D _attitudeMatrix;
  mutable MutableMatrix44D _camRM;
  
public:
  
  DeviceAttitudeCameraConstrainer(){
  };
  
  ~DeviceAttitudeCameraConstrainer();
  
  bool onCameraChange(const Planet* planet,
                      const Camera* previousCamera,
                      Camera* nextCamera) const;
};

#endif /* defined(__G3MiOSSDK__DeviceAttitudeCameraConstrainer__) */
