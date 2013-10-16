//
//  FloatBufferBuilderFromColor.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 07/09/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_FloatBufferBuilderFromColor_hpp
#define G3MiOSSDK_FloatBufferBuilderFromColor_hpp

#include "FloatBufferBuilder.hpp"
#include "Color.hpp"

class FloatBufferBuilderFromColor: public FloatBufferBuilder{
public:
  void add(float r, float g, float b, float a) { //RGBA
    _values.push_back(r);
    _values.push_back(g);
    _values.push_back(b);
    _values.push_back(a);
  }

  void addBase255(int r, int g, int b, float a) { //RGBA
    _values.push_back( r / 255.0f );
    _values.push_back( g / 255.0f );
    _values.push_back( b / 255.0f );
    _values.push_back( a );
  }

  void add(const Color& c) { //RGBA
    _values.push_back(c.getRed());
    _values.push_back(c.getGreen());
    _values.push_back(c.getBlue());
    _values.push_back(c.getAlpha());
  }
  
};

#endif
