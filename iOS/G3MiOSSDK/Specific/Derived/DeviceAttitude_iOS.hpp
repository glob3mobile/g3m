//
//  DeviceAttitude_iOS.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 7/5/15.
//
//

#import "IDeviceAttitude.hpp"


#import <CoreMotion/CoreMotion.h>

class DeviceAttitude_iOS: public IDeviceAttitude{
private:
  CMMotionManager* _mm;
public:
  
  DeviceAttitude_iOS();
  
  /**
   Must be called before any other operation
   **/
  
  void startTrackingDeviceOrientation() const = 0;
  
  /**
   Must be called to stop operations
   **/
  
  void stopTrackingDeviceOrientation() const = 0;
  
  bool isTracking() const = 0;
  
  void copyValueOfRotationMatrix(MutableMatrix44D& rotationMatrix) const = 0;
  
  InterfaceOrientation getCurrentInterfaceOrientation() const;
  
  CoordinateSystem getCameraCoordinateSystemForInterfaceOrientation(InterfaceOrientation orientation) const;
  
};
