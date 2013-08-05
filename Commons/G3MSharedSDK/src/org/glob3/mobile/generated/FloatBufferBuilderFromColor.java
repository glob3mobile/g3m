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
    _values.push_back(r);
    _values.push_back(g);
    _values.push_back(b);
    _values.push_back(a);
  }

  public final void add(Color c) //RGBA
  {
    _values.push_back(c.getRed());
    _values.push_back(c.getGreen());
    _values.push_back(c.getBlue());
    _values.push_back(c.getAlpha());
  }

}