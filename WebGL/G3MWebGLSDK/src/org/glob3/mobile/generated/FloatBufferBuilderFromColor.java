package org.glob3.mobile.generated; 
//
//  FloatBufferBuilderFromColor.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 07/09/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



public class FloatBufferBuilderFromColor extends FloatBufferBuilder
{
  public final void add(float r, float g, float b, float a) //RGBA
  {
	_values.add(r);
	_values.add(g);
	_values.add(b);
	_values.add(a);
  }

  public final void add(Color c) //RGBA
  {
	_values.add(c.getRed());
	_values.add(c.getGreen());
	_values.add(c.getBlue());
	_values.add(c.getAlpha());
  }

}