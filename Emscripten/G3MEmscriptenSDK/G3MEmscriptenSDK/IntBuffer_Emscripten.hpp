
#ifndef IntBuffer_Emscripten_hpp
#define IntBuffer_Emscripten_hpp

#include "IIntBuffer.hpp"


class IntBuffer_Emscripten : public IIntBuffer {
private:
  const size_t _size;
  int*         _values;
  int          _timestamp;

  const long long _id;
  static long long _nextID;

public:
  IntBuffer_Emscripten(const size_t size);

  long long getID() const;

  virtual ~IntBuffer_Emscripten();

  const size_t size() const;

  int timestamp() const;

  int get(const size_t i) const;

  void put(const size_t i, int value);

  void rawPut(const size_t i, int value);

  const std::string description() const;

};

#endif
