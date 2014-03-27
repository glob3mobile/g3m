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

class ByteBuffer_iOS : public IByteBuffer {
private:
  const int            _size;
  unsigned char* const _values;
  int                  _timestamp;

public:
  ByteBuffer_iOS(int size) :
  _values(new unsigned char[size]),
  _size(size),
  _timestamp(-1)
  {
    if (_values == NULL) {
      ILogger::instance()->logError("Allocating error.");
    }
  }

  ByteBuffer_iOS(unsigned char* values,
                 int size) :
  _values(values),
  _size(size),
  _timestamp(-1)
  {

  }

  virtual ~ByteBuffer_iOS() {
    delete [] _values;
  }

  int size() const {
    return _size;
  }

  int timestamp() const {
    return _timestamp;
  }

  unsigned char get(int i) const {
    if (i < 0 || i > _size) {
      ILogger::instance()->logError("Buffer Get error.");
    }

    return _values[i];
  }

  void put(int i,
           unsigned char value) {
    if (i < 0 || i > _size) {
      ILogger::instance()->logError("Buffer Put error.");
    }

    if (_values[i] != value) {
      _values[i] = value;
      _timestamp++;
    }
  }

  void rawPut(int i,
              unsigned char value) {
    if (i < 0 || i > _size) {
      ILogger::instance()->logError("Buffer Put error.");
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
