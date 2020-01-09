
#ifndef FloatBuffer_Emscripten_hpp
#define FloatBuffer_Emscripten_hpp

#include "IFloatBuffer.hpp"

#include <emscripten/val.h>

class FloatBuffer_Emscripten : public IFloatBuffer {
private:

  emscripten::val _buffer;
  int             _timestamp;

  const long long _id;
  static long long _nextID;

public:

  FloatBuffer_Emscripten(size_t size);

  FloatBuffer_Emscripten(float f0,
                         float f1,
                         float f2,
                         float f3,
                         float f4,
                         float f5,
                         float f6,
                         float f7,
                         float f8,
                         float f9,
                         float f10,
                         float f11,
                         float f12,
                         float f13,
                         float f14,
                         float f15);


  long long getID() const;

  const size_t size() const;

  int timestamp() const;

  const std::string description() const;


  float get(const size_t i) const;

  void put(const size_t i,
           float value);

  void rawPut(const size_t i,
              float value);

  void rawAdd(const size_t i,
              float value);

  void rawPut(const size_t i,
              const IFloatBuffer* srcBuffer,
              const size_t srcFromIndex,
              const size_t count);

};

#endif
