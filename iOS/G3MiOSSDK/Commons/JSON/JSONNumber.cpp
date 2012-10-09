//
//  JSONNumber.cpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 03/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "JSONNumber.hpp"

int JSONNumber::getIntValue(){
  if (_type != int_type) {
    ILogger::instance()->logError("The value you are requesting is not of type int - returning 0!");
    return 0;
  }
  return _int_value;
}

/*const long JSONNumber::getLongValue()const{
  if (_type != long_type){
    ILogger::instance()->logError("The value you are requesting is not of type long - returning 0!");
    return 0;
  }
  return _value.long_value;
}*/

float JSONNumber::getFloatValue(){
  if (_type != float_type){
    ILogger::instance()->logError("The value you are requesting is not of type float - returning 0!");
    return 0;
  }
  return _float_value;
}

double JSONNumber::getDoubleValue(){
  if (_type != double_type){
    ILogger::instance()->logError("The value you are requesting is not of type double - returning 0!");
    return 0;
  }
  return _double_value;  
}
