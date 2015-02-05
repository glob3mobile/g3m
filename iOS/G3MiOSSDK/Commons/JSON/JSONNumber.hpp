//
//  JSONNumber.hpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 03/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_JSONNumber
#define G3MiOSSDK_JSONNumber

#include "JSONBaseObject.hpp"

class JSONNumber : public JSONBaseObject {
public:
  virtual double value() const = 0;

  const JSONNumber* asNumber() const {
    return this;
  }

};

#endif
