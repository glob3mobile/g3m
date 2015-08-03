//
//  FloatBuffer_iOS.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 06/09/12.
//
//

#ifndef __G3MiOSSDK__FloatBuffer_iOS__
#define __G3MiOSSDK__FloatBuffer_iOS__

#include "IFloatBuffer.hpp"
//#include "ILogger.hpp"
#include "ErrorHandling.hpp"
#include <OpenGLES/ES2/gl.h>

class FloatBuffer_iOS : public IFloatBuffer {
private:

  static long long _newCounter;
  static long long _deleteCounter;
  static long long _genBufferCounter;
  static long long _deleteBufferCounter;

  static void showStatistics();

  const size_t _size;
  float*       _values;
  int          _timestamp;

  //ID
  const long long _id;
  static long long _nextID;

  //VBO
  static GLuint     _boundVertexBuffer;
  mutable bool      _vertexBufferCreated;
  mutable GLuint    _vertexBuffer; //VBO
  mutable int       _vertexBufferTimestamp;

public:
  FloatBuffer_iOS(size_t size) :
  _size(size),
  _timestamp(0),
  _values(new float[size]),
  _vertexBuffer(-1),
  _vertexBufferTimestamp(-1),
  _vertexBufferCreated(false),
  _id(_nextID++)
  {
    if (_values == NULL) {
      THROW_EXCEPTION("Allocating error.");
    }

    _newCounter++;
    showStatistics();
  }

  long long getID() const {
    return _id;
  }

  FloatBuffer_iOS(float f0,
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
                  float f15) :
  _size(16),
  _timestamp(0),
  _vertexBuffer(-1),
  _vertexBufferTimestamp(-1),
  _vertexBufferCreated(false),
  _id(_nextID)
  {
    _values = new float[16];
    _values[ 0] = f0;
    _values[ 1] = f1;
    _values[ 2] = f2;
    _values[ 3] = f3;
    _values[ 4] = f4;
    _values[ 5] = f5;
    _values[ 6] = f6;
    _values[ 7] = f7;
    _values[ 8] = f8;
    _values[ 9] = f9;
    _values[10] = f10;
    _values[11] = f11;
    _values[12] = f12;
    _values[13] = f13;
    _values[14] = f14;
    _values[15] = f15;

    _newCounter++;
    showStatistics();
  }

  virtual ~FloatBuffer_iOS();

  size_t size() const {
    return _size;
  }

  int timestamp() const {
    return _timestamp;
  }

  float get(size_t i) const {
    if (i >= _size) {
      THROW_EXCEPTION("Buffer Overflow");
    }
    return _values[i];
  }

  void put(size_t i,
           float value) {
    if (i >= _size) {
      THROW_EXCEPTION("Buffer Overflow");
    }

    if (_values[i] != value) {
      _values[i] = value;
      _timestamp++;
    }
  }

  void rawPut(size_t i,
              float value) {
    if (i >= _size) {
      THROW_EXCEPTION("Buffer Overflow");
    }
    _values[i] = value;
  }

  void rawAdd(size_t i, float value) {
    if (i >= _size) {
      THROW_EXCEPTION("Buffer Overflow");
    }
    _values[i] = _values[i] + value;
  }


  float* getPointer() const {
    return _values;
  }

  const std::string description() const;
  
  void bindAsVBOToGPU() const;

  void rawPut(size_t i,
              const IFloatBuffer* srcBuffer,
              size_t srcFromIndex,
              size_t srcToIndex);

};

#endif
