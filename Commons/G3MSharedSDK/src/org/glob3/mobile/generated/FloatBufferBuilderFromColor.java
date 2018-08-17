package org.glob3.mobile.generated;//
//  FloatBufferBuilderFromColor.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 07/09/12.
//



public class FloatBufferBuilderFromColor extends FloatBufferBuilder
{
  public final void add(float r, float g, float b, float a)
  {
	_values.add(r);
	_values.add(g);
	_values.add(b);
	_values.add(a);
  }

  public final void addBase255(int r, int g, int b, float a)
  {
	_values.add(r / 255.0f);
	_values.add(g / 255.0f);
	_values.add(b / 255.0f);
	_values.add(a);
  }

  public final void add(Color c)
  {
	_values.add(c._red);
	_values.add(c._green);
	_values.add(c._blue);
	_values.add(c._alpha);
  }

}
