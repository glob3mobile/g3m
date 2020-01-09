

#include "DeviceInfo_Emscripten.hpp"

#include "emscripten/val.h"

using namespace emscripten;


float DeviceInfo_Emscripten::getDPI() const {
  return 96 * _devicePixelRatio;
}

DeviceInfo_Platform DeviceInfo_Emscripten::getPlatform() const {
  return DEVICE_Emscripten;
}

DeviceInfo_Emscripten::DeviceInfo_Emscripten() {
  val devicePixelRatio = val::global("devicePixelRatio");
  if (devicePixelRatio.as<bool>()) {
    _devicePixelRatio = devicePixelRatio.as<float>();
  }
  else {
    _devicePixelRatio = 1;
  }
}

float DeviceInfo_Emscripten::getDevicePixelRatio() const {
  return _devicePixelRatio;
}
