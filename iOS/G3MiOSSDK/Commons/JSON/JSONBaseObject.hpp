//
//  JSONBaseObject.hpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 17/09/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_JSONBaseObject_hpp
#define G3MiOSSDK_JSONBaseObject_hpp

class JSONObject;
class JSONArray;
class JSONBoolean;
class JSONNumber;
class JSONString;

#include <string>

class JSONBaseObject {
  
public:
  virtual ~JSONBaseObject() {
  };
  
  virtual JSONObject*  asObject();
  virtual JSONArray*   asArray();
  virtual JSONBoolean* asBoolean();
  virtual JSONNumber*  asNumber();
  virtual JSONString*  asString();

  virtual const std::string description() const = 0;

};



#endif
