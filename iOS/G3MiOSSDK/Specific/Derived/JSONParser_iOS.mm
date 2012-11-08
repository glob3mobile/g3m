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
#import "JSONNumber.hpp"
#import "JSONBoolean.hpp"
#import "JSONString.hpp"


JSONBaseObject* JSONParser_iOS::parse(IByteBuffer* buffer){
  return parse(buffer->getAsString());
}

JSONBaseObject* JSONParser_iOS::parse(const std::string& inputString) {

  NSString *string = [[NSString alloc] initWithUTF8String:inputString.c_str()];
  _jsonData = [string dataUsingEncoding:NSUTF8StringEncoding];

  NSError *e = nil;
  id nsJsonObject = [NSJSONSerialization JSONObjectWithData:_jsonData
                                                    options:kNilOptions
                                                      error:&e];

  if ([nsJsonObject isKindOfClass:[NSArray class]]) {
    NSArray *jsonArray = (NSArray *)nsJsonObject;
    JSONArray* topLevelArray = new JSONArray();
    for (NSObject *element in jsonArray){
      topLevelArray->add(makeJSONElement(element));
    }
    return topLevelArray;
  }
  else if ([nsJsonObject isKindOfClass:[NSDictionary class]]) {
    NSDictionary *jsonDictionary = (NSDictionary *)nsJsonObject;
    JSONObject* topLevelObject = new JSONObject();
    NSArray *keys = [jsonDictionary allKeys];
    const int count = [keys count];
    for (int i = 0; i<count; i++){
      const std::string key = std::string( [[keys objectAtIndex:i] UTF8String] );
      topLevelObject->put(key,
                          makeJSONElement([jsonDictionary objectForKey:[keys objectAtIndex:i]]));
    }
    return topLevelObject;
  }

  ILogger::instance()->logWarning("JSON Parser: Malformed JSON: The top-level object is neither an Object nor an Array");
  
  return NULL;
}

JSONBaseObject* JSONParser_iOS::makeJSONElement(NSObject* object){
  if ([object isKindOfClass:[NSArray class]]) {
    NSArray *jsonArray = (NSArray *)object;
    JSONArray* array = new JSONArray();
    for (NSObject *element in jsonArray){
      array->add(makeJSONElement(element));
    }
    return array;
  }
  else if ([object isKindOfClass:[NSDictionary class]]) {
    NSDictionary *jsonDict = (NSDictionary *) object;
    JSONObject* dictionary = new JSONObject();
    NSArray *keys = [jsonDict allKeys];
    const int count = [keys count];
    for (int i = 0; i<count; i++){
      NSUInteger objI = i;
      const std::string key = std::string( [[keys objectAtIndex:objI] UTF8String] );
      dictionary->put(key,
                      makeJSONElement([jsonDict objectForKey:[keys objectAtIndex:objI]]));
    }
    return dictionary;
  }
  else if ([object isKindOfClass:[NSNumber class]]){
    NSNumber *jsonNumber = (NSNumber *) object;

    //Booleans are also encoded as NSNumbers
    if (strcmp([jsonNumber objCType], @encode(BOOL))==0) {
      return new JSONBoolean([jsonNumber boolValue]);
    }
    else if (strcmp([jsonNumber objCType], @encode(int))==0) {
      return new JSONNumber([jsonNumber intValue]);
    }
    else if (strcmp([jsonNumber objCType], @encode(float))==0) {
      return new JSONNumber([jsonNumber floatValue]);
    }
    else {
      JSONNumber* numberObj = new JSONNumber([jsonNumber doubleValue]);
      return numberObj;
    }
  }
  else if ([object isKindOfClass:[NSString class]]){
    return new JSONString([(NSString *) object UTF8String]);
  }

  return NULL;
}
