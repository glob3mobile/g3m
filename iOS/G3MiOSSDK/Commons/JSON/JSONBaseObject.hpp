//
//  JSONBaseObject.hpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 17/09/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_JSONBaseObject
#define G3MiOSSDK_JSONBaseObject

class JSONObject;
class JSONArray;
class JSONBoolean;
class JSONNumber;
class JSONString;
class JSONNull;
class JSONVisitor;

#include <string>


class JSONBaseObject {

public:

  static JSONBaseObject* deepCopy(const JSONBaseObject* object) {
    return (object == NULL) ? NULL : object->deepCopy();
  }

  virtual ~JSONBaseObject() {
  }

  virtual const JSONObject*  asObject()  const;
  virtual const JSONArray*   asArray()   const;
  virtual const JSONBoolean* asBoolean() const;
  virtual const JSONNumber*  asNumber()  const;
  virtual const JSONString*  asString()  const;

  virtual const JSONNull*    asNull() const;


  virtual JSONBaseObject* deepCopy() const = 0;

  virtual const std::string description() const = 0;
#ifdef JAVA_CODE
  @Override
  public String toString() {
    return description();
  }
#endif

  virtual void acceptVisitor(JSONVisitor* visitor) const = 0;

};

#endif
