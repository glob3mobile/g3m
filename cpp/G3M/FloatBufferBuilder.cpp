//
//  FloatBufferBuilder.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 05/09/12.
//
//

#include "FloatBufferBuilder.hpp"

#include "IFloatBuffer.hpp"
#include "IFactory.hpp"
#include "Vector2D.hpp"
#include "Vector3D.hpp"


FloatBufferBuilder::FloatBufferBuilder() {
}

FloatBufferBuilder::~FloatBufferBuilder() {
}

const size_t FloatBufferBuilder::size() const {
  return _values.size();
}

IFloatBuffer* FloatBufferBuilder::create() const {
#ifdef C_CODE
  const size_t size = _values.size();
  IFloatBuffer* result = IFactory::instance()->createFloatBuffer(size);
  for (size_t i = 0; i < size; i++) {
    result->rawPut(i, _values.at(i));
  }
  return result;
#endif
#ifdef JAVA_CODE
  return IFactory.instance().createFloatBuffer( _values._array, _values._size );
#endif
}

Vector2D FloatBufferBuilder::getVector2D(int i) const {
  const int i2 = i * 2;
#ifdef C_CODE
  return Vector2D(_values[i2], _values[i2 + 1]);
#endif
#ifdef JAVA_CODE
  return new Vector2D(_values.get(i2), _values.get(i2 + 1));
#endif
}

Vector3D FloatBufferBuilder::getVector3D(int i) const {
  const int i3 = i * 3;
#ifdef C_CODE
  return Vector3D(_values[i3], _values[i3 + 1], _values[i3 + 2]);
#endif
#ifdef JAVA_CODE
  return new Vector3D(_values.get(i3), _values.get(i3 + 1), _values.get(i3 + 2));
#endif
}
