//
//  DeviceInfo_iOS.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/8/13.
//
//

#ifndef __G3MiOSSDK__DeviceInfo_iOS__
#define __G3MiOSSDK__DeviceInfo_iOS__

#include "IDeviceInfo.hpp"


class DeviceInfo_iOS : public IDeviceInfo {
private:
  float _dpi;

public:
  DeviceInfo_iOS();

  float getDPI() const {
    return _dpi;
  }

  DeviceInfo_Platform getPlatform() const {
    return DEVICE_iOS;
  }

  float getQualityFactor() const {
    return 1;
  }

};

#endif
