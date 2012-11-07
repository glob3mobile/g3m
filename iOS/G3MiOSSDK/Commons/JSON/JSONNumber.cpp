//
//  JSONNumber.cpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 03/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "JSONNumber.hpp"
#include "IStringBuilder.hpp"

int JSONNumber::intValue() const {
  if (_type != int_type) {
    ILogger::instance()->logError("The value you are requesting is not of type int - returning 0!");
    return 0;
  }

  return _intValue;
}

float JSONNumber::floatValue() const {
  if (_type != float_type){
    ILogger::instance()->logError("The value you are requesting is not of type float - returning 0!");
    return 0;
  }

  return _floatValue;
}

double JSONNumber::doubleValue() const {
  if (_type != double_type){
    ILogger::instance()->logError("The value you are requesting is not of type double - returning 0!");
    return 0;
  }

  return _doubleValue;
}


const std::string JSONNumber::description() const {
  IStringBuilder *isb = IStringBuilder::newStringBuilder();

  switch (_type) {
    case int_type:
      isb->addString("int/");
      isb->addInt(_intValue);
      break;

    case float_type:
      isb->addString("float/");
      isb->addFloat(_floatValue);
      break;

    case double_type:
      isb->addString("double/");
      isb->addDouble(_doubleValue);
      break;

    default:
      isb->addString("[unknown number type]");
      break;
  }

  const std::string s = isb->getString();
  delete isb;
  return s;
}
