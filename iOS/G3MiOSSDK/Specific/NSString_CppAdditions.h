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

- (NSString *)urlEncode;

- (std::string) toCppString;

+ (std::string) cppPercentEncodedUrlString: (const std::string&) url;

+ (NSString*) convertHTML: (const std::string&) string;

@end
