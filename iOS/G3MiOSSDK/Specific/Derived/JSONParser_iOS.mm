//
//  JSONParser_iOS.mm
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 02/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "JSONParser_iOS.hpp"
#import "JSONArray.hpp"
#import "JSONObject.hpp"
#import "JSONDouble.hpp"
#import "JSONFloat.hpp"
#import "JSONInteger.hpp"
#import "JSONLong.hpp"
#import "JSONBoolean.hpp"
#import "JSONString.hpp"
#import "ByteBuffer_iOS.hpp"
#import "JSONNull.hpp"

JSONBaseObject* JSONParser_iOS::parse(NSData* jsonData,
                                      bool nullAsObject) {
  NSError *e = nil;
  id nsJsonObject = [NSJSONSerialization JSONObjectWithData: jsonData
                                                    options: NSJSONReadingAllowFragments
                                                      error: &e];

  if (e) {
    ILogger::instance()->logError("JSON Parser: Error=%s",
                                    [[e localizedDescription] UTF8String]);
    if ([e userInfo]) {
      ILogger::instance()->logError("JSON Parser: UserInfo=%s",
                                      [[[e userInfo] description] UTF8String]);
    }

    return NULL;
  }

  return convert(nsJsonObject, nullAsObject);
}

JSONBaseObject* JSONParser_iOS::parse(IByteBuffer* buffer,
                                      bool nullAsObject) {
  ByteBuffer_iOS* buffer_iOS = (ByteBuffer_iOS*) buffer;

  NSData* data = [NSData dataWithBytes: buffer_iOS->getPointer()
                                length: buffer_iOS->size()];

  return parse(data, nullAsObject);
}

JSONBaseObject* JSONParser_iOS::parse(const std::string& inputString,
                                      bool nullAsObject) {
  NSString *string = [[NSString alloc] initWithUTF8String: inputString.c_str()];
  NSData* data = [string dataUsingEncoding: NSUTF8StringEncoding];

  return parse(data, nullAsObject);
}

JSONBaseObject* JSONParser_iOS::convert(NSObject* object,
                                        bool nullAsObject) {
  if ([object isKindOfClass:[NSArray class]]) {
    NSArray *jsonArray = (NSArray *)object;
    JSONArray* array = new JSONArray();
    for (NSObject *element in jsonArray) {
      array->add(convert(element, nullAsObject));
    }
    return array;
  }
  else if ([object isKindOfClass:[NSDictionary class]]) {
    NSDictionary *jsonDict = (NSDictionary *) object;
    JSONObject* dictionary = new JSONObject();
    NSArray *keys = [jsonDict allKeys];
    const int count = [keys count];
    for (int i = 0; i<count; i++) {
      NSUInteger objI = i;
      const std::string key = std::string( [[keys objectAtIndex:objI] UTF8String] );
      dictionary->put(key,
                      convert( [jsonDict objectForKey:[keys objectAtIndex:objI]], nullAsObject) );
    }
    return dictionary;
  }
  else if ([object isKindOfClass:[NSNumber class]]) {
    NSNumber *jsonNumber = (NSNumber *) object;

    //Booleans are also encoded as NSNumbers
    if (strcmp([jsonNumber objCType], @encode(BOOL))==0) {
      return new JSONBoolean([jsonNumber boolValue]);
    }
    else if (strcmp([jsonNumber objCType], @encode(int))==0) {
      return new JSONInteger([jsonNumber intValue]);
    }
    else if (strcmp([jsonNumber objCType], @encode(long long))==0) {
      const long long longLongValue = [jsonNumber longLongValue];
      const int intValue = (int) longLongValue;
      if (longLongValue == intValue) {
        return new JSONInteger(intValue);
      }
      else {
        return new JSONLong(longLongValue);
      }
    }
    else if (strcmp([jsonNumber objCType], @encode(float))==0) {
      return new JSONFloat([jsonNumber floatValue]);
    }
    else if (strcmp([jsonNumber objCType], @encode(double))==0) {
      const double doubleValue = [jsonNumber doubleValue];
      const float floatValue = (float) doubleValue;
      if (doubleValue == floatValue) {
        return new JSONFloat(floatValue);
      }
      else {
        return new JSONDouble(doubleValue);
      }
    }
    else {
      JSONNumber* numberObj = new JSONDouble([jsonNumber doubleValue]);
      return numberObj;
    }
  }
  else if ([object isKindOfClass:[NSString class]]) {
    return new JSONString([(NSString *) object UTF8String]);
  }
  else if ( [object isKindOfClass:[NSNull class]] ) {
    return nullAsObject ? new JSONNull() : NULL;
  }

  return NULL;
}
