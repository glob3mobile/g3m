//
//  JSONArray.hpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 02/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_JSONArray_hpp
#define G3MiOSSDK_JSONArray_hpp

#include <vector>

#include "JSONBaseObject.hpp"

class JSONArray : public JSONBaseObject {
private:
  std::vector<JSONBaseObject*> _entries;

public:
  JSONArray* asArray() {
    return this;
  }

  ~JSONArray();

  JSONBaseObject* get(const int index) const;

  JSONObject*  getAsObject (const int index) const;
  JSONArray*   getAsArray  (const int index) const;
  JSONBoolean* getAsBoolean(const int index) const;
  JSONNumber*  getAsNumber (const int index) const;
  JSONString*  getAsString (const int index) const;

  int size() const;

  void add(JSONBaseObject* object);

  const std::string description() const;

};

#endif
