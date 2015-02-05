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
#ifdef C_CODE
  const int size = _values.size();

  IFloatBuffer* result = IFactory::instance()->createFloatBuffer(size);

  for (int i = 0; i < size; i++) {
    result->rawPut(i, _values.at(i));
  }

//  ILogger::instance()->logInfo("Created FloatBuffer with %d floats", result->size());
  return result;
#endif
#ifdef JAVA_CODE
  //return IFactory.instance().createFloatBuffer( _values.toArray() );
  return IFactory.instance().createFloatBuffer( _values._array, _values._size );
#endif
}
