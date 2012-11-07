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

JSONBaseObject* JSONObject::get(const std::string& key) const {
#ifdef C_CODE
  std::map<std::string, JSONBaseObject*>::const_iterator it = _entries.find(key);
  if(it != _entries.end()){
    return _entries.at(key);
  }
  ILogger::instance()->logError("The JSONObject does not contain the key \""+key+"\"");
  return NULL;
#endif
  
#ifdef JAVA_CODE
  return _entries.get(key);
#endif
}

void JSONObject::put(const std::string& key,
                     JSONBaseObject* object) {
  _entries[key]=object;
}

int JSONObject::size() const {
  return _entries.size();
}

JSONObject* JSONObject::getAsObject(const std::string& key) const {
  return get(key)->asObject();
}

JSONArray* JSONObject::getAsArray(const std::string& key) const {
  return get(key)->asArray();
}

JSONBoolean* JSONObject::getAsBoolean(const std::string& key) const {
  return get(key)->asBoolean();
}

JSONNumber* JSONObject::getAsNumber(const std::string& key) const {
  return get(key)->asNumber();
}

JSONString* JSONObject::getAsString(const std::string& key) const {
  return get(key)->asString();
}
