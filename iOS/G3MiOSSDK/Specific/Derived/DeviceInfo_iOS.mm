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
  //  UIScreen* mainScreen = [UIScreen mainScreen];
  //
  //  const float scale = [mainScreen respondsToSelector:@selector(scale)]
  //  /*                          */ ? [mainScreen scale]
  //  /*                          */ : 1;
  const float scale = 1; // doesn't consider the retina factor as the opengl-view doesn't change it size based on retina resolution

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
      _dpi = 163 * scale;
    }
    else {
      // REGULAR IPAD
      _dpi = 132 * scale;
    }
  }
  else if (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone) {
    _dpi = 163 * scale;
  }
  else {
    _dpi = 160 * scale;
  }
  
}
