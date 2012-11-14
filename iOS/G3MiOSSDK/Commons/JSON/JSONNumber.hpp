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

enum number_type{
  int_type,
  float_type,
  double_type
};

class JSONNumber : public JSONBaseObject {
private:
  int               _intValue;
  float             _floatValue;
  double            _doubleValue;
  const number_type _type;
  
public:

  JSONNumber(int value) :
  _intValue(value),
  _floatValue(0.0),
  _doubleValue(0.0),
  _type(int_type)
  {
  }

  JSONNumber(float value) :
  _intValue(0),
  _floatValue(value),
  _doubleValue(0.0),
  _type(float_type)
  {
  }
  
  JSONNumber(double value) :
  _intValue(0),
  _floatValue(0.0),
  _doubleValue(value),
  _type(double_type)
  {
  }

  int    intValue()    const;
  float  floatValue()  const;
  double doubleValue() const;

  double value() const;

  JSONNumber* asNumber(){
    return this;
  }
  
  const std::string description() const;

};



#endif
