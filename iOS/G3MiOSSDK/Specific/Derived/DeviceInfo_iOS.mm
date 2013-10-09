//
//  DeviceInfo_iOS.mm
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/8/13.
//
//

#include "DeviceInfo_iOS.hpp"

#import <UIKit/UIKit.h>


DeviceInfo_iOS::DeviceInfo_iOS() {
  UIScreen* mainScreen = [UIScreen mainScreen];

  const float scale = [mainScreen respondsToSelector:@selector(scale)]
  /*                          */ ? [mainScreen scale]
  /*                          */ : 1;

  if (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPad) {
    _dpi = 132 * scale;
  }
  else if (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone) {
    _dpi = 163 * scale;
  }
  else {
    _dpi = 160 * scale;
  }

}
