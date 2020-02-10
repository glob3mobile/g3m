//
//  JSONNumber.hpp
//  G3M
//
//  Created by Oliver Koehler on 03/10/12.
//

#ifndef G3M_JSONNumber
#define G3M_JSONNumber

#include "JSONBaseObject.hpp"


class JSONNumber : public JSONBaseObject {
public:
  virtual double value() const = 0;

  const JSONNumber* asNumber() const {
    return this;
  }

  const JSONBoolean* asBoolean() const;

};

#endif
