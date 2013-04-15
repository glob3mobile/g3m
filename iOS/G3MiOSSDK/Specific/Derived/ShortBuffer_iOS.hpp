//
//  ShortBuffer_iOS.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/18/13.
//
//

#ifndef __G3MiOSSDK__ShortBuffer_iOS__
#define __G3MiOSSDK__ShortBuffer_iOS__

#include "IShortBuffer.hpp"
#include "ILogger.hpp"

class ShortBuffer_iOS : public IShortBuffer {
private:
  const int _size;
  short*    _values;
  int       _timestamp;

public:
  ShortBuffer_iOS(int size) :
  _size(size),
  _timestamp(0)
  {
    _values = new short[size];
    if (_values == NULL){
      ILogger::instance()->logError("Allocating error.");
    }
  }

  virtual ~ShortBuffer_iOS() {
    delete [] _values;
  }

  int size() const {
    return _size;
  }

  int timestamp() const {
    return _timestamp;
  }

  short get(int i) const {
    
    if (i < 0 || i > _size){
      ILogger::instance()->logError("Buffer Put error.");
    }
    
    return _values[i];
  }

  void put(int i, short value) {
    
    if (i < 0 || i > _size){
      ILogger::instance()->logError("Buffer Put error.");
    }
    
    if (_values[i] != value) {
      _values[i] = value;
      _timestamp++;
    }
  }

  void rawPut(int i, short value) {
    if (i < 0 || i > _size){
      ILogger::instance()->logError("Buffer Put error.");
    }
    _values[i] = value;
  }

  short* getPointer() const {
    return _values;
  }

  const std::string description() const;
  
};


#endif
