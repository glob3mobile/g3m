//
//  IFactory.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 06/09/12.
//
//

#include "IFactory.hpp"

IFactory* IFactory::_instance = NULL;

const IDeviceInfo* IFactory::getDeviceInfo() const {
  if (_deviceInfo == NULL) {
    _deviceInfo = createDeviceInfo();
  }
  return _deviceInfo;
}
