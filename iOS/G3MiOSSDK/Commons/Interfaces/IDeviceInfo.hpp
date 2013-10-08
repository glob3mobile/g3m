//
//  IDeviceInfo.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/8/13.
//
//

#ifndef __G3MiOSSDK__IDeviceInfo__
#define __G3MiOSSDK__IDeviceInfo__

class IDeviceInfo {
public:

  virtual ~IDeviceInfo() {

  }

  float getPixelsInMM(float millimeters);

  virtual float getDPI() const = 0;

};

#endif
