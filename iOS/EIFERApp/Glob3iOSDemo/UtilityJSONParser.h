//
//  UtilityJSONParser.h
//  EIFER App
//
//  Created by Chano on 21/6/18.
//
//

#import <Foundation/Foundation.h>

#include <G3MiOSSDK/ElevationData.hpp>
#include <G3MiOSSDK/MeshRenderer.hpp>

@interface UtilityJSONParser : NSObject

- (void) loadJSONWithPath:(NSString *)path elevData:(const ElevationData *)elevData planet:(const Planet *)planet mr:(MeshRenderer *)mr;

@end
