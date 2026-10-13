//
//  IDeviceInfo.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/8/13.
//
//

#ifndef __G3M__IDeviceInfo__
#define __G3M__IDeviceInfo__

enum DeviceInfo_Platform {
  DEVICE_iOS,
  DEVICE_Android,
  DEVICE_GWT,
  DEVICE_Emscripten
};

class IDeviceInfo {
public:

  virtual ~IDeviceInfo() {
  }

  float getPixelsInMM(float millimeters) const;

  virtual float getDPI() const = 0;

  virtual DeviceInfo_Platform getPlatform() const = 0;

  virtual float getDevicePixelRatio() const = 0;

  // in the widget's pixels: what the notch, island, rounded corners and system bars cover on each
  // edge; 0 where the platform reports nothing
  virtual float getSafeAreaInsetTop() const = 0;
  virtual float getSafeAreaInsetRight() const = 0;
  virtual float getSafeAreaInsetBottom() const = 0;
  virtual float getSafeAreaInsetLeft() const = 0;
  
};

#endif
