//
//  FloatBufferBuilder.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 05/09/12.
//
//

#include "FloatBufferBuilder.hpp"

#include "IFloatBuffer.hpp"
#include "IFactory.hpp"

IFloatBuffer* FloatBufferBuilder::create() const {
  const int size = _values.size();
  
  IFloatBuffer* result = GFactory.createFloatBuffer(size);
  
  for (int i = 0; i < size; i++) {
    result->rawPut(i, _values[i]);
  }
  
  return result;
}
