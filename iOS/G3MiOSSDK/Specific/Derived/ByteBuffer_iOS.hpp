//
//  ByteBuffer_iOS.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 10/09/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_ByteBuffer_iOS
#define G3MiOSSDK_ByteBuffer_iOS

#include "IByteBuffer.hpp"
#include "ILogger.hpp"
#include "ErrorHandling.hpp"

class ByteBuffer_iOS : public IByteBuffer {
private:
  const size_t         _size;
  unsigned char* const _values;
  int                  _timestamp;

public:
  ByteBuffer_iOS(size_t size) :
  _values(new unsigned char[size]),
  _size(size),
  _timestamp(-1)
  {
    if (_values == NULL) {
      ILogger::instance()->logError("Allocating error.");
    }
  }

  ByteBuffer_iOS(unsigned char* values,
                 size_t size) :
  _values(values),
  _size(size),
  _timestamp(-1)
  {

  }

  virtual ~ByteBuffer_iOS() {
    delete [] _values;
  }

  size_t size() const {
    return _size;
  }

  int timestamp() const {
    return _timestamp;
  }

  unsigned char get(size_t i) const {
    if (i >= _size) {
      THROW_EXCEPTION("Buffer Overflow");
    }

    return _values[i];
  }

  void put(size_t i,
           unsigned char value) {
    if (i >= _size) {
      THROW_EXCEPTION("Buffer Overflow");
    }

    if (_values[i] != value) {
      _values[i] = value;
      _timestamp++;
    }
  }

  void rawPut(size_t i,
              unsigned char value) {
    if (i >= _size) {
      THROW_EXCEPTION("Buffer Overflow");
    }

    _values[i] = value;
  }

  unsigned char* getPointer() const {
    return _values;
  }

  const std::string description() const;
#ifdef JAVA_CODE
  @Override
  public String toString() {
    return description();
  }
#endif

  const std::string getAsString() const;
  
};

#endif
