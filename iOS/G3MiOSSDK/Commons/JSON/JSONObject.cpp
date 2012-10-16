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
#endif
  _entries.clear();

}

JSONBaseObject* JSONObject::getObjectForKey(const std::string key){
#ifdef C_CODE
  std::map<std::string, JSONBaseObject*>::iterator it =_entries.find(key);
  if(it != _entries.end()){
    return _entries[key];
  }
  ILogger::instance()->logError("The JSONObject does not contain the key \""+key+"\"");
  return NULL;
#endif
  
#ifdef JAVA_CODE
  return _entries.get(key);
#endif
}

void JSONObject::putObject(const std::string key, JSONBaseObject* object){
  _entries[key]=object;
}

int JSONObject::getSize(){
  return _entries.size();
}
