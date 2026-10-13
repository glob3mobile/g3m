

#include "DeviceInfo_Emscripten.hpp"

#include <emscripten/val.h>
#include <cmath>

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

// CSS pixels of env(safe-area-inset-<side>), read through a hidden probe; 0 where the browser has no
// safe area (desktop) or the page lacks viewport-fit=cover
static float getSafeAreaInsetCSSPixels(const std::string& side) {
  val document = val::global("document");
  val probe = document.call<val>("createElement", std::string("div"));
  val style = probe["style"];
  style.set("position", std::string("fixed"));
  style.set("visibility", std::string("hidden"));
  style.set("paddingTop", "env(safe-area-inset-" + side + ")");
  document["body"].call<void>("appendChild", probe);
  const float value = val::global("parseFloat")(val::global("window").call<val>("getComputedStyle", probe)["paddingTop"]).as<float>();
  document["body"].call<void>("removeChild", probe);
  return std::isnan(value) ? 0 : value;
}

float DeviceInfo_Emscripten::getSafeAreaInsetTop() const {
  return getSafeAreaInsetCSSPixels("top") * _devicePixelRatio;
}

float DeviceInfo_Emscripten::getSafeAreaInsetRight() const {
  return getSafeAreaInsetCSSPixels("right") * _devicePixelRatio;
}

float DeviceInfo_Emscripten::getSafeAreaInsetBottom() const {
  return getSafeAreaInsetCSSPixels("bottom") * _devicePixelRatio;
}

float DeviceInfo_Emscripten::getSafeAreaInsetLeft() const {
  return getSafeAreaInsetCSSPixels("left") * _devicePixelRatio;
}
