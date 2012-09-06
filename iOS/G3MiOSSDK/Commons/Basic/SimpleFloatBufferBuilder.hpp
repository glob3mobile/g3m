//
//  SimpleFloatBufferBuilder.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 06/09/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_SimpleFloatBufferBuilder_hpp
#define G3MiOSSDK_SimpleFloatBufferBuilder_hpp

#include "FloatBufferBuilder.hpp"

class SimpleFloatBufferBuilder: public FloatBufferBuilder {
public:
  
  void add(float value) {
    _values.push_back(value);
  }
    
  void add(double value) {
      _values.push_back((float) value);
  }
};

#endif
