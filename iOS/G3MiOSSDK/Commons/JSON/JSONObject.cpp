//
//  JSONObject.cpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 01/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "JSONObject.hpp"
#include <string>

JSONBaseObject* JSONObject::getObjectForKey(const std::string key){
  std::map<std::string, JSONBaseObject*>::iterator it =_entries.find(key);
  if(it != _entries.end()){
    return _entries[key];
  }
  ILogger::instance()->logError("The JSONObject does not contain the key");
  return NULL;
}

void JSONObject::putObject(const std::string key, JSONBaseObject* object){
  _entries.insert(std::make_pair(key, object));
}
