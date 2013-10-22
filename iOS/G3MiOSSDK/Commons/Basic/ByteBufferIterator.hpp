//
//  ByteBufferIterator.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/2/13.
//
//

#ifndef __G3MiOSSDK__ByteBufferIterator__
#define __G3MiOSSDK__ByteBufferIterator__

class IByteBuffer;

#include <string>

class ByteBufferIterator {
private:
  const IByteBuffer* _buffer;
  int                _cursor;
  int                _bufferTimestamp;
  mutable int        _bufferSize;

  ByteBufferIterator(const ByteBufferIterator& that);

public:
  ByteBufferIterator(const IByteBuffer* buffer);

  bool hasNext() const;

  unsigned char nextUInt8();
  short         nextInt16();
  int           nextInt32();
  long long     nextInt64();

  IByteBuffer* nextBufferUpTo(unsigned char sentinel);

  const std::string nextZeroTerminatedString();

  double nextDouble();
  
};

#endif
