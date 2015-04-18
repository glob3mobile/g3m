//
//  IDeviceInfo.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/8/13.
//
//

#ifndef __G3MiOSSDK__IDeviceInfo__
#define __G3MiOSSDK__IDeviceInfo__

enum DeviceInfo_Platform {
  DEVICE_iOS,
  DEVICE_Android,
  DEVICE_GWT
};

class IDeviceInfo {
public:

  virtual ~IDeviceInfo() {

  }

  float getPixelsInMM(float millimeters) const;

  virtual float getDPI() const = 0;

  virtual DeviceInfo_Platform getPlatform() const = 0;

  virtual float getQualityFactor() const = 0;

};

#endif
