//
//  IntBufferBuilder.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 05/09/12.
//
//

#include "IntBufferBuilder.hpp"

#include "IFactory.hpp"
#include "IIntBuffer.hpp"

IIntBuffer* IntBufferBuilder::create() const {
  const int size = _values.size();
  
  IIntBuffer* result = IFactory::instance()->createIntBuffer(size);
  
  for (int i = 0; i < size; i++) {
    result->rawPut(i, _values[i]);
  }
  
  return result;
}
