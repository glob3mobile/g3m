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
#ifdef C_CODE
  const int size = _values.size();

  IShortBuffer* result = IFactory::instance()->createShortBuffer(size);

  for (int i = 0; i < size; i++) {
    result->rawPut(i, _values.at(i));
  }

  return result;
#endif
#ifdef JAVA_CODE
  //return IFactory.instance().createShortBuffer( _values.toArray() );
  return IFactory.instance().createShortBuffer( _values._array, _values._size );
#endif
}
