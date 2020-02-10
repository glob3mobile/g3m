//
//  JSONNumber.cpp
//  G3M
//
//  Created by Oliver Koehler on 03/10/12.
//


#include "JSONNumber.hpp"
#include "JSONBoolean.hpp"


const JSONBoolean* JSONNumber::asBoolean() const {
  return new JSONBoolean( value() != 0 );
}
