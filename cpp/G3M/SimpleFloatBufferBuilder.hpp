//
//  SimpleFloatBufferBuilder.hpp
//  G3M
//
//  Created by José Miguel S N on 06/09/12.
//

#ifndef G3M_SimpleFloatBufferBuilder
#define G3M_SimpleFloatBufferBuilder

#include "FloatBufferBuilder.hpp"

class SimpleFloatBufferBuilder: public FloatBufferBuilder {
public:
  
  void add(float value) {
    _values.push_back(value);
  }
  
  void add(double value) {
    _values.push_back((float) value);
  }
  
  void add(float value1, float value2) {
    _values.push_back(value1);
    _values.push_back(value2);
  }
  
  void add(float value1, float value2, float value3) {
    _values.push_back(value1);
    _values.push_back(value2);
    _values.push_back(value3);
  }
  
  void add(float value1, float value2, float value3, float value4) {
    _values.push_back(value1);
    _values.push_back(value2);
    _values.push_back(value3);
    _values.push_back(value4);
  }
  
};

#endif
