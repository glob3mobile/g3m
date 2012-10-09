//
//  JSONNumber.cpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 03/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "JSONNumber.hpp"

const int JSONNumber::getIntValue()const{
  if (_type != int_type) {
    ILogger::instance()->logError("The value you are requesting is not of type int - returning 0!");
    return 0;
  }
  return _value._int_value;
}

/*const long JSONNumber::getLongValue()const{
  if (_type != long_type){
    ILogger::instance()->logError("The value you are requesting is not of type long - returning 0!");
    return 0;
  }
  return _value.long_value;
}*/

const float JSONNumber::getFloatValue()const{
  if (_type != float_type){
    ILogger::instance()->logError("The value you are requesting is not of type float - returning 0!");
    return 0;
  }
  return _value._float_value;
}

const double JSONNumber::getDoubleValue()const{
  if (_type != double_type){
    ILogger::instance()->logError("The value you are requesting is not of type double - returning 0!");
    return 0;
  }
  return _value._double_value;  
}
