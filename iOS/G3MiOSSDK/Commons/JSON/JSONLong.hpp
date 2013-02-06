//
//  JSONLong.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/5/13.
//
//

#ifndef __G3MiOSSDK__JSONLong__
#define __G3MiOSSDK__JSONLong__

#include "JSONNumber.hpp"

class JSONLong : public JSONNumber {
private:
  const long long _value;

public:
  JSONLong(long long value) :
  _value(value)
  {
  }

  JSONLong* deepCopy() const {
    return new JSONLong(_value);
  }

  double value() const {
    return _value;
  }

  long long longValue() const {
    return _value;
  }

  void acceptVisitor(JSONVisitor* visitor) const;

  const std::string description() const;
  
};

#endif
