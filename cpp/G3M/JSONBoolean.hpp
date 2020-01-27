//
//  JSONBoolean.hpp
//  G3M
//
//  Created by Oliver Koehler on 03/10/12.
//

#ifndef G3M_JSONBoolean
#define G3M_JSONBoolean

#include "JSONBaseObject.hpp"

class JSONBoolean : public JSONBaseObject {
private:
  const bool _value;

public:
  JSONBoolean(bool value) :
  _value(value)
  {
  }

  const bool value() const {
    return _value;
  }

  const JSONBoolean* asBoolean() const {
    return this;
  }

  const std::string description() const;

  const std::string toString() const;

  JSONBoolean* deepCopy() const {
    return new JSONBoolean(_value);
  }
  
  void acceptVisitor(JSONVisitor* visitor) const;

};

#endif
