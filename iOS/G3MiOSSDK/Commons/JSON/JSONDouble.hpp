//
//  JSONDouble.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/5/13.
//
//

#ifndef __G3MiOSSDK__JSONDouble__
#define __G3MiOSSDK__JSONDouble__

#include "JSONNumber.hpp"

class JSONDouble : public JSONNumber {
private:
  const double _value;

public:
  JSONDouble(double value) :
  _value(value)
  {
  }

  JSONDouble* deepCopy() const {
    return new JSONDouble(_value);
  }

  double value() const {
    return _value;
  }

  double doubleValue() const {
    return _value;
  }

  void acceptVisitor(JSONVisitor* visitor) const;

  const std::string description() const;

};

#endif
