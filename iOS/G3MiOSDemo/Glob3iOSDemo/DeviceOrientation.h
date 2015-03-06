//
//  DeviceOrientation.h
//  G3MiOSDemo
//
//  Created by Jose Miguel SN on 5/3/15.
//
//

#import <Foundation/Foundation.h>


#import <CoreMotion/CoreMotion.h>

@interface DeviceOrientation : NSObject{
  CMMotionManager* _mm;
}

-(id) init;
-(CMAttitude*) getAttitude;
-(double) getPitchInRadians;

-(CMQuaternion) getQuaternion;

-(CMRotationMatrix) getRotationMatrix;

@end
