package org.glob3.mobile.generated;
//
//  FloatBufferBuilderFromCartesian2D.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/9/16.
//
//

//
//  FloatBufferBuilderFromCartesian2D.hpp
//  G3M
//
//  Created by José Miguel S N on 06/09/12.
//



//class Vector2F;


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
