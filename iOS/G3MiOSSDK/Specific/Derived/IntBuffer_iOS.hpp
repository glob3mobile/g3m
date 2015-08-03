//
//  IntBuffer_iOS.h
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 06/09/12.
//
//

#ifndef __G3MiOSSDK__IntBuffer_iOS__
#define __G3MiOSSDK__IntBuffer_iOS__

#include "IIntBuffer.hpp"
#include "ILogger.hpp"
#include "ErrorHandling.hpp"

class IntBuffer_iOS : public IIntBuffer {
private:
  const size_t _size;
  int*      _values;
  int       _timestamp;

  //ID
  const long long _id;
  static long long _nextID;

public:
  IntBuffer_iOS(size_t size) :
  _size(size),
  _timestamp(0),
  _id(_nextID++)
  {
    _values = new int[size];
    
    if (_values == NULL) {
      ILogger::instance()->logError("Allocating error.");
    }
  }

  long long getID() const {
    return _id;
  }

  virtual ~IntBuffer_iOS() {
    delete [] _values;
  }
  
  size_t size() const {
    return _size;
  }
  
  int timestamp() const {
    return _timestamp;
  }
  
  int get(size_t i) const {
    if (i >= _size) {
      THROW_EXCEPTION("Buffer Overflow");
    }
    
    return _values[i];
  }
  
  void put(size_t i, int value) {
    if (i >= _size) {
      THROW_EXCEPTION("Buffer Overflow");
    }
    
    if (_values[i] != value) {
      _values[i] = value;
      _timestamp++;
    }
  }
  
  void rawPut(size_t i, int value) {
    if (i >= _size) {
      THROW_EXCEPTION("Buffer Overflow");
    }
    
    _values[i] = value;
  }
  
  int* getPointer() const {
    return _values;
  }

  const std::string description() const;

};

#endif

