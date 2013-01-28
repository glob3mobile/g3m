//
//  ShortBufferBuilder.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/19/13.
//
//

#include "ShortBufferBuilder.hpp"

#include "IFactory.hpp"
#include "IShortBuffer.hpp"

IShortBuffer* ShortBufferBuilder::create() const {
  const int size = _values.size();

  IShortBuffer* result = GFactory.createShortBuffer(size);

  for (int i = 0; i < size; i++) {
    result->rawPut(i, _values[i]);
  }

  return result;
}
