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

  JSONObject* asObject() {
    return this;
  }
  
  JSONBaseObject* get(const std::string& key) const;
  
  JSONObject*  getAsObject (const std::string& key) const;
  JSONArray*   getAsArray  (const std::string& key) const;
  JSONBoolean* getAsBoolean(const std::string& key) const;
  JSONNumber*  getAsNumber (const std::string& key) const;
  JSONString*  getAsString (const std::string& key) const;

  void put(const std::string& key,
           JSONBaseObject* object);
  
  int size() const;
  
};

#endif
