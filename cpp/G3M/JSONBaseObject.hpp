//
//  JSONBaseObject.hpp
//  G3M
//
//  Created by Oliver Koehler on 17/09/12.
//

#ifndef G3M_JSONBaseObject
#define G3M_JSONBaseObject

#include <string>

class JSONObject;
class JSONArray;
class JSONBoolean;
class JSONNumber;
class JSONString;
class JSONNull;
class JSONVisitor;


class JSONBaseObject {

public:

  static JSONBaseObject* deepCopy(const JSONBaseObject* object) {
    return (object == NULL) ? NULL : object->deepCopy();
  }

  static const std::string toString(const JSONBaseObject* object) {
    return (object == NULL) ? "null" : object->toString();
  }

  virtual ~JSONBaseObject() {
  }

  virtual const JSONObject*  asObject()  const;
  virtual const JSONArray*   asArray()   const;
  virtual const JSONBoolean* asBoolean() const;
  virtual const JSONNumber*  asNumber()  const;
  virtual const JSONString*  asString()  const;
  virtual const JSONNull*    asNull()    const;

  virtual JSONBaseObject* deepCopy() const = 0;

  virtual const std::string description() const = 0;

  virtual void acceptVisitor(JSONVisitor* visitor) const = 0;

  virtual const std::string toString() const = 0;

};

#endif
