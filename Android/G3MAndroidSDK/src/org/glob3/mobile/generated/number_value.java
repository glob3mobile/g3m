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



//C++ TO JAVA CONVERTER TODO TASK: Unions are not supported in Java:
//ORIGINAL LINE: union number_value
public final class number_value
{
  public int int_value;
  public int long_value;
  public float float_value;
  public double double_value;
  public number_value(int n)
  {
	  int_value = n;
  }
  public number_value(int l)
  {
	  long_value = l;
  }
  public number_value(float f)
  {
	  float_value = f;
  }
  public number_value(double d)
  {
	  double_value = d;
  }
}