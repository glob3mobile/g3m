//
//  DeviceLocation_iOS.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 25/6/15.
//
//

#ifndef __G3MiOSSDK__DeviceLocation_iOS__
#define __G3MiOSSDK__DeviceLocation_iOS__

#include "G3MSharedSDK/IDeviceLocation.hpp"

#import <CoreLocation/CoreLocation.h>


@interface LocationDelegate: NSObject <CLLocationManagerDelegate>{
  @private
  
  CLLocationManager* _locationManager;
  CLLocation* _lastLocation;
  
  BOOL _requestingPermission;
}

@end


class DeviceLocation_iOS: public IDeviceLocation{
  
  LocationDelegate* _delegate;
  bool _isTracking;
  
public:
  DeviceLocation_iOS();
  ~DeviceLocation_iOS() {}
  
  bool startTrackingLocation();
  void stopTrackingLocation();
  bool isTrackingLocation();
  Geodetic3D getLocation();
  
};

#endif /* defined(__G3MiOSSDK__DeviceLocation_iOS__) */
