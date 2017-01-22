//
//  DeviceAttitude_iOS.m
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 7/5/15.
//
//

#import "DeviceAttitude_iOS.hpp"
#import <UIKit/UIKit.h>


DeviceAttitude_iOS::DeviceAttitude_iOS(bool showsDeviceMovementDisplay):
_showsDeviceMovementDisplay(showsDeviceMovementDisplay)
{
  _mm = [[CMMotionManager alloc] init];
}

void DeviceAttitude_iOS::startTrackingDeviceOrientation() const{
  // Tell CoreMotion to show the compass calibration HUD when required
  // to provide true north-referenced attitude
  _mm.showsDeviceMovementDisplay = _showsDeviceMovementDisplay;
  _mm.deviceMotionUpdateInterval = 1.0 / 60.0;
  
  // Attitude that is referenced to true north
  [_mm startDeviceMotionUpdatesUsingReferenceFrame:CMAttitudeReferenceFrameXTrueNorthZVertical];
  
  _reorientationMatrix.copyValue(MutableMatrix44D::createGeneralRotationMatrix(Angle::halfPi(),
                                                                               Vector3D::UP_Z,
                                                                               Vector3D::ZERO));
}

void DeviceAttitude_iOS::stopTrackingDeviceOrientation() const{
  [_mm stopDeviceMotionUpdates];
}


bool DeviceAttitude_iOS::isTracking() const{
  return _mm.deviceMotionActive;
}

void DeviceAttitude_iOS::copyValueOfRotationMatrix(MutableMatrix44D& rotationMatrix) const{
  
  if (isTracking()) {
    
    CMRotationMatrix m = _mm.deviceMotion.attitude.rotationMatrix;
    
    rotationMatrix.setValue(m.m11, m.m12, m.m13, 0,
                            m.m21, m.m22, m.m23, 0,
                            m.m31, m.m32, m.m33, 0,
                            0, 0, 0, 1);
    
    //Reorienting as Heading = 0 is East on iOS
    rotationMatrix.copyValueOfMultiplication(_reorientationMatrix, rotationMatrix);
  }
  else {
    rotationMatrix.setValid(false);
  }
}

InterfaceOrientation DeviceAttitude_iOS::getCurrentInterfaceOrientation() const{
  
  UIInterfaceOrientation orientation = [UIApplication sharedApplication].statusBarOrientation;
  
  switch (orientation) {
    case UIInterfaceOrientationPortrait:
    {
      return PORTRAIT;
    }
    case UIInterfaceOrientationPortraitUpsideDown:
    {
      return PORTRAIT_UPSIDEDOWN;
    }
      
    case UIInterfaceOrientationLandscapeLeft:
    {
      return LANDSCAPE_LEFT;
    }
      
    case UIInterfaceOrientationLandscapeRight:
    {
      return LANDSCAPE_RIGHT;
    }
    default:{
      return PORTRAIT;
    }
  }
  
}


