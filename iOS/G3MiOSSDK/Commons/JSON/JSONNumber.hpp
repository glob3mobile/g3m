//
//  JSONNumber.hpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 03/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_JSONNumber_hpp
#define G3MiOSSDK_JSONNumber_hpp

#include "JSONBaseObject.hpp"
#include "ILogger.hpp"

union number_value{
  int int_value;
  long long_value;
  float float_value;
  double double_value;
  number_value(int n):int_value(n){}
  number_value(long l):long_value(l){}
  number_value(float f):float_value(f){}
  number_value(double d):double_value(d){}
};

enum number_type{
  int_type, long_type, float_type, double_type
};

class JSONNumber : public JSONBaseObject{
private:
  const number_value _value;
  const number_type _type;
  
public:
  JSONNumber(int value):_value(number_value(value)), _type(int_type){}
  JSONNumber(long value):_value(number_value(value)), _type(long_type){}
  JSONNumber(float value):_value(number_value(value)), _type(float_type){}
  JSONNumber(double value):_value(number_value(value)), _type(double_type){}
  const int getIntValue()const;
  const long getLongValue()const;
  const float getFloatValue()const;
  const double getDoubleValue()const;
  JSONNumber* getNumber(){
    return this;
  }
  
};



#endif
