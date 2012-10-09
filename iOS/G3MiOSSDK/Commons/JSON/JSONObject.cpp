//
//  JSONObject.cpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 01/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "JSONObject.hpp"
#include <string>

JSONObject::~JSONObject(){
#ifdef C_CODE
  for (std::map<std::string, JSONBaseObject*>::iterator it=_entries.begin(); it!=_entries.end(); it++){
    delete it->second;
  }
  _entries.clear();
#endif
}

JSONBaseObject* JSONObject::getObjectForKey(const std::string key){
  std::map<std::string, JSONBaseObject*>::iterator it =_entries.find(key);
  if(it != _entries.end()){
    return _entries[key];
  }
  ILogger::instance()->logError("The JSONObject does not contain the key \""+key+"\"");
  return NULL;
}

void JSONObject::putObject(const std::string key, JSONBaseObject* object){
  _entries[key]=object;
}
