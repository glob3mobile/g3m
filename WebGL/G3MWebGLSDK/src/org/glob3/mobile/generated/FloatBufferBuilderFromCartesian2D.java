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
	_values.add((float) vector.x());
	_values.add((float) vector.y());
  }

  public final void add(float x, float y)
  {
	_values.add(x);
	_values.add(y);
  }
}