
#ifndef ShortBuffer_Emscripten_hpp
#define ShortBuffer_Emscripten_hpp

#include "G3M/IShortBuffer.hpp"

#include <emscripten/val.h>

class NativeGL_Emscripten;


class ShortBuffer_Emscripten : public IShortBuffer {
private:
  emscripten::val _buffer;
  int             _timestamp;

  const long long _id;
  static long long _nextID;

  const NativeGL_Emscripten* _nativeGL;
  emscripten::val _webGLBuffer;

public:
  ShortBuffer_Emscripten(size_t size);

  ~ShortBuffer_Emscripten();


  long long getID() const;

  const size_t size() const;

  int timestamp() const;

  const std::string description() const;


  short get(const size_t i) const;

  void put(const size_t i,
           short value);

  void rawPut(const size_t i,
              short value);


  emscripten::val getWebGLBuffer(const NativeGL_Emscripten* nativeGL);

  emscripten::val getBuffer();

};

#endif
