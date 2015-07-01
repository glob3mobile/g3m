//
//  IDeviceLocation.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 25/6/15.
//
//

#ifndef G3MiOSSDK_IDeviceLocation_hpp
#define G3MiOSSDK_IDeviceLocation_hpp

#include "Geodetic3D.hpp"

class IDeviceLocation{
  
  static IDeviceLocation* _instance;
  
public:
  
  static IDeviceLocation* instance(){
    return _instance;
  }
  
  static void setInstance(IDeviceLocation* loc);
  
  virtual ~IDeviceLocation(){}
  
  virtual bool startTrackingLocation() = 0;
  virtual void stopTrackingLocation() = 0;
  virtual bool isTrackingLocation() = 0;
  
  virtual Geodetic3D getLocation() = 0;
  
};


#endif
