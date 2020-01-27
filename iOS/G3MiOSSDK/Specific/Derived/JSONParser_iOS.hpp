//
//  JSONParser_iOS.hpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 02/10/12.
//

#ifndef G3MiOSSDK_JSONParser_iOS
#define G3MiOSSDK_JSONParser_iOS

#import <Foundation/Foundation.h>
#import <Foundation/NSJSONSerialization.h>

#include <string>

#include "G3MSharedSDK/IJSONParser.hpp"


class JSONParser_iOS : public IJSONParser {
private:
  JSONBaseObject* convert(NSObject *object,
                          bool nullAsObject);

  JSONBaseObject* parseNSData(NSData* jsonData,
                              bool nullAsObject);

public:
  JSONBaseObject* parse(const std::string& string,
                        bool nullAsObject);

  JSONBaseObject* parse(const IByteBuffer* buffer,
                        bool nullAsObject);

};

#endif
