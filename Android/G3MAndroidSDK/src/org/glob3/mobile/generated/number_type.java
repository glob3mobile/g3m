package org.glob3.mobile.generated; 
//
//  JSONNumber.cpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 03/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  JSONNumber.hpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 03/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



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

/*class Number_value{
private:
  
  
public:
  int _int_value;
  float _float_value;
  double _double_value;
  Number_value():_int_value(0), _float_value(0.0), _double_value(0.0){};
  Number_value(int n):_int_value(n), _float_value(0.0), _double_value(0.0){};
  Number_value(float f):_int_value(0), _float_value(f), _double_value(0.0){};
  Number_value(double d):_int_value(0), _float_value(0.0), _double_value(d){};
  
};*/

public enum number_type
{
  int_type,
  long_type,
  float_type,
  double_type;

	public int getValue()
	{
		return this.ordinal();
	}

	public static number_type forValue(int value)
	{
		return values()[value];
	}
}