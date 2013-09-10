package org.glob3.mobile.generated; 
//
//  SimpleFloatBufferBuilder.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 06/09/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



public class SimpleFloatBufferBuilder extends FloatBufferBuilder
{

  public final void add(float value)
  {
    _values.push_back(value);
  }

  public final void add(double value)
  {
    _values.push_back((float) value);
  }

  public final void add(float value1, float value2)
  {
    _values.push_back(value1);
    _values.push_back(value2);
  }

  public final void add(float value1, float value2, float value3)
  {
    _values.push_back(value1);
    _values.push_back(value2);
    _values.push_back(value3);
  }

  public final void add(float value1, float value2, float value3, float value4)
  {
    _values.push_back(value1);
    _values.push_back(value2);
    _values.push_back(value3);
    _values.push_back(value4);
  }

}