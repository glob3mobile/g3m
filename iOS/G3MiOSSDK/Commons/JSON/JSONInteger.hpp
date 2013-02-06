//
//  JSONInteger.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/5/13.
//
//

#ifndef __G3MiOSSDK__JSONInteger__
#define __G3MiOSSDK__JSONInteger__

#include "JSONNumber.hpp"

class JSONInteger : public JSONNumber {
private:
  const int _value;

public:
  JSONInteger(int value) :
  _value(value)
  {
  }

  JSONInteger* deepCopy() const {
    return new JSONInteger(_value);
  }

  double value() const {
    return _value;
  }

  int intValue() const {
    return _value;
  }

  void acceptVisitor(JSONVisitor* visitor) const;

  const std::string description() const;
  
};


#endif
