//
//  JSONString.hpp
//  G3M
//
//  Created by Oliver Koehler on 03/10/12.
//

#ifndef G3M_JSONString
#define G3M_JSONString

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

  const std::string toString() const;

  JSONString* deepCopy() const {
    return new JSONString(_value);
  }

  void acceptVisitor(JSONVisitor* visitor) const;

};

#endif
