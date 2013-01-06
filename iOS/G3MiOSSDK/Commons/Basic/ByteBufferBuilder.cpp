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
#ifdef C_CODE
  const int size = str.size();
  for (int i = 0; i < size; i++) {
    const char c = str.at(i);
    _values.push_back(c);
  }
  _values.push_back((unsigned char) 0);
#endif
#if JAVA_CODE
  try {
    final byte[] bytesArray = str.getBytes("UTF8");

    final int size = bytesArray.length;
    for (int i = 0; i < size; i++) {
      final byte c = bytesArray[i];
      _values.add(c);
    }
    _values.add((byte) 0);
  }
  catch (final java.io.UnsupportedEncodingException e) {
    e.printStackTrace();
  }
#endif
}

void ByteBufferBuilder::addDouble(double value) {
  addInt64( IMathUtils::instance()->doubleToRawLongBits(value) );
}
