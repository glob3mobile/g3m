//
//  NSString_CppAdditions.mm
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 8/19/13.
//
//

#import <Foundation/Foundation.h>

#import "NSString_CppAdditions.h"


@implementation NSString (CppAdditions)

+ (NSString*) stringWithCppString: (const std::string&) string
{
  return [NSString stringWithCString: string.c_str()
                            encoding: NSUTF8StringEncoding];
}

- (std::string) toCppString
{
  return [self cStringUsingEncoding: NSUTF8StringEncoding];
}

@end
