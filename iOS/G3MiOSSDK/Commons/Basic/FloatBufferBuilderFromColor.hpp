//
//  FloatBufferBuilderFromColor.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 07/09/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_FloatBufferBuilderFromColor
#define G3MiOSSDK_FloatBufferBuilderFromColor

#include "FloatBufferBuilder.hpp"
#include "Color.hpp"

class FloatBufferBuilderFromColor: public FloatBufferBuilder {
public:
  void add(float r, float g, float b, float a) {
    _values.push_back(r);
    _values.push_back(g);
    _values.push_back(b);
    _values.push_back(a);
  }

  void addBase255(int r, int g, int b, float a) {
    _values.push_back( r / 255.0f );
    _values.push_back( g / 255.0f );
    _values.push_back( b / 255.0f );
    _values.push_back( a );
  }

  void add(const Color& c) {
    _values.push_back(c._red);
    _values.push_back(c._green);
    _values.push_back(c._blue);
    _values.push_back(c._alpha);
  }
  
};

#endif
