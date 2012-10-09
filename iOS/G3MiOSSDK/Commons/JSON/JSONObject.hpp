//
//  JSONObject.hpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 01/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_JSONObject_hpp
#define G3MiOSSDK_JSONObject_hpp

#include <map>
#include "JSONBaseObject.hpp"
#include "ILogger.hpp"


class JSONObject : public JSONBaseObject{
private:
  std::map<std::string, JSONBaseObject*> _entries;
  
public:
  ~JSONObject();
  JSONObject* getObject(){
    return this;
  }
  
  JSONBaseObject* getObjectForKey(const std::string key);
  
  void putObject(const std::string key, JSONBaseObject* object);
  
  int getSize();
  
};



#endif
