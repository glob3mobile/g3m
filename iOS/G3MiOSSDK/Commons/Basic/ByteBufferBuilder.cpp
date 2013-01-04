//
//  ByteBufferBuilder.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/3/13.
//
//

#include "ByteBufferBuilder.hpp"

#include "IFactory.hpp"
#include "IByteBuffer.hpp"
#include "IMathUtils.hpp"

IByteBuffer* ByteBufferBuilder::create() const {
  const int size = _values.size();

  IByteBuffer* result = GFactory.createByteBuffer(size);

  for (int i = 0; i < size; i++) {
    result->rawPut(i, _values[i]);
  }

  return result;
}

void ByteBufferBuilder::addStringZeroTerminated(const std::string& str) {
  const int size = str.size();
  for (int i = 0; i < size; i++) {
    const char c = str.at(i);
    _values.push_back(c);
  }
  _values.push_back((unsigned char) 0);
}

void ByteBufferBuilder::addDouble(double value) {
  addInt64( IMathUtils::instance()->doubleToRawLongBits(value) );
}
