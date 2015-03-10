//
//  DeviceOrientation.m
//  G3MiOSDemo
//
//  Created by Jose Miguel SN on 5/3/15.
//
//

#import "DeviceOrientation.h"
#import <G3MiOSSDK/Angle.hpp>

@implementation DeviceOrientation

-(id) init{
  self = [super init];
  if (self != nil){
    _mm = [[CMMotionManager alloc] init];
    
    // Tell CoreMotion to show the compass calibration HUD when required
    // to provide true north-referenced attitude
    _mm.showsDeviceMovementDisplay = YES;
    _mm.deviceMotionUpdateInterval = 1.0 / 60.0;
    
    // Attitude that is referenced to true north
    [_mm startDeviceMotionUpdatesUsingReferenceFrame:CMAttitudeReferenceFrameXTrueNorthZVertical];
    
  }
  return self;
}

-(CMAttitude*) getAttitude{
  
  CMAttitude* attitude = _mm.deviceMotion.attitude;
  
  double pitch = Angle::fromRadians(attitude.pitch).getNormalizedDegrees();
  double roll = Angle::fromRadians(attitude.roll).getNormalizedDegrees();
  double yaw = Angle::fromRadians(attitude.yaw).getNormalizedDegrees();
  
  printf("PITCH %f\n", pitch);
  printf("ROLL %f\n", roll);
  printf("YAW %f\n", yaw);
  printf("---------------\n");
  
  return attitude;
}

-(CMQuaternion) getQuaternion{
  CMQuaternion q = _mm.deviceMotion.attitude.quaternion;
  //printf("X: %f Y: %f Z: %f W: %f\n", q.x, q.y, q.z, q.w);
  return q;
}

-(CMRotationMatrix) getRotationMatrix{
  CMRotationMatrix m = _mm.deviceMotion.attitude.rotationMatrix;
  return m;
}

-(double) getPitchInRadians{
  
  CMAttitude* attitude = _mm.deviceMotion.attitude;
  double pitch = attitude.pitch;
  double roll = attitude.roll;
  double yaw = attitude.yaw;
  
  UIInterfaceOrientation orientation = [UIApplication sharedApplication].statusBarOrientation;
  
  //const double PI = 3.141592;
  const double pi2 = PI/2;
  const double pi23 = 2*PI/3;
  
  switch (orientation) {
    case UIInterfaceOrientationPortrait:
      if (roll < -pi23) {
        return pi2 - pitch;
      } else{
        return pitch - pi2;
      }
      break;
      
    case UIInterfaceOrientationPortraitUpsideDown:
      if (attitude.yaw > 0){
        return PI -( -attitude.pitch + (PI/2));
      } else{
        return (-(PI/2) - attitude.pitch);
      }
      break;
      
    case UIInterfaceOrientationLandscapeLeft:
      return attitude.roll;
      
    default:
      return NAN;
      break;
  }
  
//  switch (orientation) {
//    case UIInterfaceOrientationPortrait:
//      if (attitude.yaw > 0){
//        return attitude.pitch - (PI/2);
//      } else{
//        return ((PI/2) - attitude.pitch);
//      }
//      break;
//      
//    case UIInterfaceOrientationPortraitUpsideDown:
//      if (attitude.yaw > 0){
//        return PI -( -attitude.pitch + (PI/2));
//      } else{
//        return (-(PI/2) - attitude.pitch);
//      }
//      break;
//      
//    case UIInterfaceOrientationLandscapeLeft:
//      return attitude.roll;
//      
//    default:
//      return NAN;
//      break;
//  }
  
}

@end
