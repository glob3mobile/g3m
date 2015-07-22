package org.glob3.mobile.generated; 
//
//  FloatBufferBuilderFromCartesian2D.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 06/09/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



public class FloatBufferBuilderFromCartesian2D extends FloatBufferBuilder
{
  public final void add(Vector2D vector)
  {
    _values.push_back((float) vector._x);
    _values.push_back((float) vector._y);
  }

  public final void add(Vector2F vector)
  {
    _values.push_back(vector._x);
    _values.push_back(vector._y);
  }

  public final void add(float x, float y)
  {
    _values.push_back(x);
    _values.push_back(y);
  }

}