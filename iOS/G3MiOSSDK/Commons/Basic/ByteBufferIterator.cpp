//
//  ByteBufferIterator.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/2/13.
//
//

#include "ByteBufferIterator.hpp"

#include "IByteBuffer.hpp"
#include "ILogger.hpp"
#include "ByteBufferBuilder.hpp"
#include "IMathUtils.hpp"

ByteBufferIterator::ByteBufferIterator(const IByteBuffer* buffer) :
_buffer(buffer),
_cursor(0),
_bufferTimestamp( buffer->timestamp() ),
_bufferSize( buffer->size() )
{
}

bool ByteBufferIterator::hasNext() const {
  if (_bufferTimestamp != _buffer->timestamp()) {
    ILogger::instance()->logError("The buffer was changed after the iteration started");
    _bufferSize = _buffer->size();
  }

  return ( _cursor < _bufferSize );
}

unsigned char ByteBufferIterator::nextUInt8() {
  if (_bufferTimestamp != _buffer->timestamp()) {
    ILogger::instance()->logError("The buffer was changed after the iteration started");
    _bufferSize = _buffer->size();
  }

  if (_cursor >= _bufferSize) {
    ILogger::instance()->logError("Iteration overflow");
    return 0;
  }

  return _buffer->get(_cursor++);
}

short ByteBufferIterator::nextInt16() {
  // LittleEndian
#ifdef C_CODE
  const unsigned char b1 = nextUInt8();
  const unsigned char b2 = nextUInt8();
#endif
#ifdef JAVA_CODE
  final short b1 = (short) (nextUInt8() & 0xFF);
  final short b2 = (short) (nextUInt8() & 0xFF);
#endif

  const int iResult = (((int) b1) |
                       ((int) (b2 << 8)));
  //  const short result = (short) iResult;
  //  return result;
  return (short) iResult;
}

int ByteBufferIterator::nextInt32() {
  // LittleEndian
#ifdef C_CODE
  const unsigned char b1 = nextUInt8();
  const unsigned char b2 = nextUInt8();
  const unsigned char b3 = nextUInt8();
  const unsigned char b4 = nextUInt8();
#endif
#ifdef JAVA_CODE
  final int b1 = nextUInt8() & 0xFF;
  final int b2 = nextUInt8() & 0xFF;
  final int b3 = nextUInt8() & 0xFF;
  final int b4 = nextUInt8() & 0xFF;
#endif

  return (((int) b1      ) |
          ((int) b2 <<  8) |
          ((int) b3 << 16) |
          ((int) b4 << 24));
}

long long ByteBufferIterator::nextInt64() {
  // LittleEndian
#ifdef C_CODE
  const unsigned char b1 = nextUInt8();
  const unsigned char b2 = nextUInt8();
  const unsigned char b3 = nextUInt8();
  const unsigned char b4 = nextUInt8();
  const unsigned char b5 = nextUInt8();
  const unsigned char b6 = nextUInt8();
  const unsigned char b7 = nextUInt8();
  const unsigned char b8 = nextUInt8();
#endif
#ifdef JAVA_CODE
  final int b1 = nextUInt8() & 0xFF;
  final int b2 = nextUInt8() & 0xFF;
  final int b3 = nextUInt8() & 0xFF;
  final int b4 = nextUInt8() & 0xFF;
  final int b5 = nextUInt8() & 0xFF;
  final int b6 = nextUInt8() & 0xFF;
  final int b7 = nextUInt8() & 0xFF;
  final int b8 = nextUInt8() & 0xFF;
#endif

  return (((long long) b1      ) |
          ((long long) b2 <<  8) |
          ((long long) b3 << 16) |
          ((long long) b4 << 24) |
          ((long long) b5 << 32) |
          ((long long) b6 << 40) |
          ((long long) b7 << 48) |
          ((long long) b8 << 56));
}

IByteBuffer* ByteBufferIterator::nextBufferUpTo(unsigned char sentinel) {
  ByteBufferBuilder builder;

  unsigned char c;

  while ( (c = nextUInt8()) != sentinel ) {
    builder.add(c);
  }

  return builder.create();
}

const std::string ByteBufferIterator::nextZeroTerminatedString() {
  IByteBuffer* buffer = nextBufferUpTo((unsigned char) 0);
  const std::string result = buffer->getAsString();
  delete buffer;
  return result;
}

double ByteBufferIterator::nextDouble() {
  const long long l = nextInt64();
  return IMathUtils::instance()->rawLongBitsToDouble( l );
}
