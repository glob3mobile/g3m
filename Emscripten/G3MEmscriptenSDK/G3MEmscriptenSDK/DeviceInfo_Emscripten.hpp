
#ifndef DeviceInfo_Emscripten_hpp
#define DeviceInfo_Emscripten_hpp

#include "IDeviceInfo.hpp"


class DeviceInfo_Emscripten : public IDeviceInfo {
private:
  float _devicePixelRatio;

public:
  DeviceInfo_Emscripten();

  float getDPI() const;

  DeviceInfo_Platform getPlatform() const;

  float getDevicePixelRatio() const;

};

#endif
