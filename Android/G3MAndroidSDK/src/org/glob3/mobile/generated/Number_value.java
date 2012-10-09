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

public class Number_value
{


  public int _int_value;
  public float _float_value;
  public double _double_value;
  public Number_value(int n)
  {
	  _int_value = n;
	  _float_value = new float();
	  _double_value = new double();
  }
  public Number_value(float f)
  {
	  _int_value = new int();
	  _float_value = f;
	  _double_value = new double();
  }
  public Number_value(double d)
  {
	  _int_value = new int();
	  _float_value = new float();
	  _double_value = d;
  }
  /*int getInt_value(){
	return _int_value;
  }
  float getFloat_value(){
	return _float_value;
  }
  double getDouble_value(){
	return _double_value;
  }*/

}