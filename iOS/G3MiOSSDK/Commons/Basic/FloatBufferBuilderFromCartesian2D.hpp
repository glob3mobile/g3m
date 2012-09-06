//
//  FloatBufferBuilderFromCartesian2D.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 06/09/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_FloatBufferBuilderFromCartesian2D_hpp
#define G3MiOSSDK_FloatBufferBuilderFromCartesian2D_hpp

#include "Vector2D.hpp"
#include "FloatBufferBuilder.hpp"

class FloatBufferBuilderFromCartesian2D: public FloatBufferBuilder {
public:
  void add(const Vector2D& vector) {
    float x = (float) vector.x(), y = (float) vector.y();
    _values.push_back(x);
    _values.push_back(y);
  }
};

#endif
