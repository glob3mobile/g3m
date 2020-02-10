
#ifndef ByteBuffer_Emscripten_hpp
#define ByteBuffer_Emscripten_hpp

#include "G3M/IByteBuffer.hpp"

#include <emscripten/val.h>


class ByteBuffer_Emscripten : public IByteBuffer {
private:
  emscripten::val _buffer;
  int             _timestamp;

public:

  ByteBuffer_Emscripten(const size_t size);

  ByteBuffer_Emscripten(const emscripten::val& buffer);

  size_t size() const;

  int timestamp() const;

  unsigned char get(size_t i) const;

  void put(size_t i, unsigned char value);

  void rawPut(size_t i, unsigned char value);

  const std::string description() const;

  const std::string getAsString() const;

};

#endif
