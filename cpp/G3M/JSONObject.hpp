//
//  JSONObject.hpp
//  G3M
//
//  Created by Oliver Koehler on 01/10/12.
//

#ifndef G3M_JSONObject
#define G3M_JSONObject

#include "JSONBaseObject.hpp"
#include <map>
#include <vector>

class IStringBuilder;

class JSONObject : public JSONBaseObject {
private:
  std::map<std::string, JSONBaseObject*> _entries;


  void putKeyAndValueDescription(const std::string& key,
                                 IStringBuilder* isb) const;
  void putKeyAndValueToString(const std::string& key,
                              IStringBuilder* isb) const;

public:
  ~JSONObject();

  const JSONObject* asObject() const {
    return this;
  }

  const JSONBaseObject* get(const std::string& key) const;

  const JSONObject*  getAsObject (const std::string& key) const;
  const JSONArray*   getAsArray  (const std::string& key) const;
  const JSONBoolean* getAsBoolean(const std::string& key) const;
  const JSONNumber*  getAsNumber (const std::string& key) const;
  const JSONString*  getAsString (const std::string& key) const;

  bool getAsBoolean(const std::string& key,
                    bool defaultValue) const;

  double getAsNumber(const std::string& key,
                     double defaultValue) const;

  const std::string getAsString(const std::string& key,
                                const std::string& defaultValue) const;

  void put(const std::string& key,
           JSONBaseObject* object);

  void put(const std::string& key,
           const std::string& value);

  void put(const std::string& key,
           double value);

  void put(const std::string& key,
           float value);

  void put(const std::string& key,
           int value);

  void put(const std::string& key,
           long long value);

  void put(const std::string& key,
           bool value);

  size_t size() const;

  const std::vector<std::string> keys() const;

  const std::string description() const;
  const std::string toString() const;

  JSONObject* deepCopy() const;

  void acceptVisitor(JSONVisitor* visitor) const;
  
};

#endif
