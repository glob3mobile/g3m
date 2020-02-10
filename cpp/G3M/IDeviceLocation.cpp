//
//  IDeviceLocation.cpp
//  G3M
//
//  Created by Jose Miguel SN on 25/6/15.
//
//

#include "IDeviceLocation.hpp"


IDeviceLocation* IDeviceLocation::_instance = NULL;

void IDeviceLocation::setInstance(IDeviceLocation* loc) {
  if (_instance != NULL) {
//    ILogger::instance()->logWarning("IDeviceLocation instance already set!");
    delete _instance;
  }
  _instance = loc;
}

