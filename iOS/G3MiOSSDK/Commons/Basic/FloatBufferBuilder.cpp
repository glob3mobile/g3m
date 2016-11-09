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
#include "Vector2D.hpp"
#include "Vector3D.hpp"

IFloatBuffer* FloatBufferBuilder::create() const {
#ifdef C_CODE
  const size_t size = _values.size();

  IFloatBuffer* result = IFactory::instance()->createFloatBuffer(size);

  for (size_t i = 0; i < size; i++) {
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

Vector2D FloatBufferBuilder::getVector2D(int i) const {
  int pos = i * 2;
#ifdef C_CODE
  return Vector2D(_values[pos], _values[pos + 1]);
#endif
#ifdef JAVA_CODE
  return new Vector2D(_values.get(pos), _values.get(pos + 1));
#endif
}

Vector3D FloatBufferBuilder::getVector3D(int i) const {
  int pos = i * 3;
#ifdef C_CODE
  return Vector3D(_values[pos], _values[pos + 1], _values[pos+2]);
#endif
#ifdef JAVA_CODE
  return new Vector3D(_values.get(pos), _values.get(pos + 1), _values.get(pos + 2));
#endif
}
