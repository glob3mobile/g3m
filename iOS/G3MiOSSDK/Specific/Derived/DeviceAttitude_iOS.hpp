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
  
  mutable MutableMatrix44D _reorientationMatrix;

  
public:
  
  DeviceAttitude_iOS();
  
  ~DeviceAttitude_iOS(){}
  
  /**
   Must be called before any other operation
   **/
  
  void startTrackingDeviceOrientation() const;
  
  /**
   Must be called to stop operations
   **/
  
  void stopTrackingDeviceOrientation() const;
  
  bool isTracking() const;
  
  void copyValueOfRotationMatrix(MutableMatrix44D& rotationMatrix) const;
  
  InterfaceOrientation getCurrentInterfaceOrientation() const;
};
