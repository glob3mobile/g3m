package org.glob3.mobile.generated;
//
//  FloatBufferBuilderFromColor.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/9/16.
//
//

//
//  FloatBufferBuilderFromColor.hpp
//  G3M
//
//  Created by José Miguel S N on 07/09/12.
//



//class Color;

public class FloatBufferBuilderFromColor extends FloatBufferBuilder
{
  public final void add(float r, float g, float b, float a)
  {
    _values.push_back(r);
    _values.push_back(g);
    _values.push_back(b);
    _values.push_back(a);
  }

  public final void addBase255(int r, int g, int b, float a)
  {
    _values.push_back(r / 255.0f);
    _values.push_back(g / 255.0f);
    _values.push_back(b / 255.0f);
    _values.push_back(a);
  }

  public final void add(Color color)
  {
    _values.push_back(color._red);
    _values.push_back(color._green);
    _values.push_back(color._blue);
    _values.push_back(color._alpha);
  }

}