//
//  JSONFloat.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/5/13.
//
//

#ifndef __G3MiOSSDK__JSONFloat__
#define __G3MiOSSDK__JSONFloat__

#include "JSONNumber.hpp"

class JSONFloat : public JSONNumber {
private:
  const float _value;

public:
  JSONFloat(float value) :
  _value(value)
  {
  }

  JSONFloat* deepCopy() const {
    return new JSONFloat(_value);
  }

  double value() const {
    return _value;
  }

  float floatValue() const {
    return _value;
  }

  void acceptVisitor(JSONVisitor* visitor) const;

  const std::string description() const;
  
};

#endif
