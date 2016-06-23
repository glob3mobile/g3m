//
//  DeviceInfo_iOS.mm
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/8/13.
//
//

#include "DeviceInfo_iOS.hpp"

#import <UIKit/UIKit.h>

#include <sys/sysctl.h>


DeviceInfo_iOS::DeviceInfo_iOS() {
  UIScreen* mainScreen = [UIScreen mainScreen];

  _devicePixelRatio = [mainScreen respondsToSelector:@selector(scale)] ? mainScreen.scale : 1;

#warning we need more members of iOS device family http://stackoverflow.com/questions/18414032/how-to-identify-a-hw-machine-identifier-reliable

  if (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPad) {
    size_t size;
    sysctlbyname("hw.machine", NULL, &size, NULL, 0);
    char* machine = new char[size+1];
    sysctlbyname("hw.machine", machine, &size, NULL, 0);
    machine[size] = 0;

    //TODO: NEW MODELS OF IPAD MINI MAY HAVE NEW IDENTIFIERS
    bool iPadMini = strcmp(machine, "iPad2,5") == 0;
    delete [] machine;

    if (iPadMini) {
      _dpi = 163 * _devicePixelRatio;
    }
    else {
      // REGULAR IPAD
      _dpi = 132 * _devicePixelRatio;
    }
  }
  else if (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone) {
    _dpi = 163 * _devicePixelRatio;
  }
  else {
    _dpi = 160 * _devicePixelRatio;
  }

}

float DeviceInfo_iOS::getDevicePixelRatio() const {
  return _devicePixelRatio;
}
