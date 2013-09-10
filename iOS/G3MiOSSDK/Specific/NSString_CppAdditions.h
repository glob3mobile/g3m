//
//  NSString_CppAdditions.h
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 8/19/13.
//
//

#include <string>

@interface NSString (CppAdditions)

+ (NSString*) stringWithCppString: (const std::string&) string;

- (std::string) toCppString;

@end
