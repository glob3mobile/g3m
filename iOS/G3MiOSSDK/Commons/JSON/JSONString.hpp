//
//  JSONString.hpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 03/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_JSONString
#define G3MiOSSDK_JSONString

#include "JSONBaseObject.hpp"

class JSONString : public JSONBaseObject {
private:
  const std::string _value;

public:
  ~JSONString() {
#ifdef JAVA_CODE
  super.dispose();
#endif

  }

  JSONString(const std::string& value) :
  _value(value)
  {
  }

  const std::string value() const {
    return _value;
  }

  const JSONString* asString() const {
    return this;
  }

  const std::string description() const;

  JSONString* deepCopy() const {
    return new JSONString(_value);
  }

  void acceptVisitor(JSONVisitor* visitor) const;

};

#endif
