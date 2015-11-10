//
//  ShortBuffer_iOS.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/18/13.
//
//

#ifndef __G3MiOSSDK__ShortBuffer_iOS__
#define __G3MiOSSDK__ShortBuffer_iOS__

#include <OpenGLES/ES2/gl.h>
#include "IShortBuffer.hpp"
#include "ILogger.hpp"
#include "ErrorHandling.hpp"

class ShortBuffer_iOS : public IShortBuffer {
private:
//  static long long _newCounter;
//  static long long _deleteCounter;
//  static long long _genBufferCounter;
//  static long long _deleteBufferCounter;
//
//  static void showStatistics();

  const size_t _size;
  short*       _values;
  int          _timestamp;

  //ID
  const long long _id;
  static long long _nextID;


  //IBO
  mutable bool   _indexBufferCreated;
  mutable GLuint _indexBuffer; //IBO
  mutable int    _indexBufferTimestamp;
  static GLuint  _boundIBO;

public:
  ShortBuffer_iOS(size_t size) :
  _size(size),
  _timestamp(0),
  _indexBuffer(-1),
  _indexBufferTimestamp(-1),
  _indexBufferCreated(false),
  _id(_nextID++)
  {
    _values = new short[size];
    if (_values == NULL) {
      ILogger::instance()->logError("Allocating error.");
    }

//    _newCounter++;
//    showStatistics();
  }

  long long getID() const {
    return _id;
  }

  ~ShortBuffer_iOS();

  size_t size() const {
    return _size;
  }

  int timestamp() const {
    return _timestamp;
  }

  short get(size_t i) const {
    if (i >= _size) {
      THROW_EXCEPTION("Buffer Overflow");
    }

    return _values[i];
  }

  void put(size_t i, short value) {
    if (i >= _size) {
      THROW_EXCEPTION("Buffer Overflow");
    }

    if (_values[i] != value) {
      _values[i] = value;
      _timestamp++;
    }
  }

  void rawPut(size_t i, short value) {
    if (i >= _size) {
      THROW_EXCEPTION("Buffer Overflow");
    }
    _values[i] = value;
  }

  short* getPointer() const {
    return _values;
  }

  const std::string description() const;

  void bindAsIBOToGPU();

};


#endif
