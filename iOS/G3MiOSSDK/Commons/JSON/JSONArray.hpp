//
//  JSONArray.hpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 02/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_JSONArray
#define G3MiOSSDK_JSONArray

#include <vector>

#include "JSONBaseObject.hpp"

class JSONArray : public JSONBaseObject {
private:
  std::vector<JSONBaseObject*> _entries;

public:
  const JSONArray* asArray() const {
    return this;
  }

  ~JSONArray();

  const JSONBaseObject* get(const size_t index) const;

  const JSONObject*  getAsObject (const size_t index) const;
  const JSONArray*   getAsArray  (const size_t index) const;
  const JSONBoolean* getAsBoolean(const size_t index) const;
  const JSONNumber*  getAsNumber (const size_t index) const;
  const JSONString*  getAsString (const size_t index) const;

  std::vector<std::string> asStringVector() const;

  bool getAsBoolean(const size_t index,
                    bool defaultValue) const;

  double getAsNumber(const size_t index,
                     double defaultValue) const;

  const std::string getAsString(const size_t index,
                                const std::string& defaultValue) const;

  size_t size() const;

  void add(JSONBaseObject* object);
  void add(const std::string& value);
  void add(double value);
  void add(float value);
  void add(int value);
  void add(long long value);
  void add(bool value);

  const std::string description() const;
  const std::string toString() const;

  JSONArray* deepCopy() const;

  void acceptVisitor(JSONVisitor* visitor) const;
  
};

#endif
