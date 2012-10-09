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

/*union number_value{
  int int_value;
  long long_value;
  float float_value;
  double double_value;
  number_value(int n):int_value(n){}
  number_value(long l):long_value(l){}
  number_value(float f):float_value(f){}
  number_value(double d):double_value(d){}
};*/

class Number_value{
private:
  
  
public:
  int _int_value;
  float _float_value;
  double _double_value;
  Number_value(int n):_int_value(n), _float_value(), _double_value(){};
  Number_value(float f):_int_value(), _float_value(f), _double_value(){};
  Number_value(double d):_int_value(), _float_value(), _double_value(d){};
  /*int getInt_value(){
    return _int_value;
  }
  float getFloat_value(){
    return _float_value;
  }
  double getDouble_value(){
    return _double_value;
  }*/
  
};

enum number_type{
  int_type, long_type, float_type, double_type
};

class JSONNumber : public JSONBaseObject{
private:
  const Number_value _value;
  const number_type _type;
  
public:
  JSONNumber(int value):_value(Number_value(value)), _type(int_type){}
  //JSONNumber(long value):_value(Number_value(value)), _type(long_type){}
  JSONNumber(float value):_value(Number_value(value)), _type(float_type){}
  JSONNumber(double value):_value(Number_value(value)), _type(double_type){}
  const int getIntValue()const;
  //const long getLongValue()const;
  const float getFloatValue()const;
  const double getDoubleValue()const;
  JSONNumber* getNumber(){
    return this;
  }
  
};



#endif
