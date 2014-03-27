//
//  JSONBoolean.hpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 03/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_JSONBoolean
#define G3MiOSSDK_JSONBoolean

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

  JSONBoolean* deepCopy() const {
    return new JSONBoolean(_value);
  }
  
  void acceptVisitor(JSONVisitor* visitor) const;

};

#endif
