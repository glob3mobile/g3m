//
//  IDeviceAttitude.cpp
//  G3MiOSDemo
//
//  Created by Jose Miguel SN on 30/4/15.
//
//

#include "IDeviceAttitude.hpp"

void IDeviceAttitude::setInstance(IDeviceAttitude* deviceAttitude) {
  if (_instance != NULL) {
    ILogger::instance()->logWarning("ILooger instance already set!");
    delete _instance;
  }
  _instance = deviceAttitude;
}

IDeviceAttitude* IDeviceAttitude::instance(){
  return _instance;
}


bool IDeviceAttitude::isTracking() const{
  return _isTracking;
}
