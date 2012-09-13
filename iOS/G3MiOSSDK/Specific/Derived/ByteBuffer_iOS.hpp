 //
//  ByteBuffer_iOS.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 10/09/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_ByteBuffer_iOS_hpp
#define G3MiOSSDK_ByteBuffer_iOS_hpp

#include "IByteBuffer.hpp"

class ByteBuffer_iOS : public IByteBuffer {
private:
  const int            _size;
  unsigned char* const _values;
  int                  _timestamp;
  
public:
  ByteBuffer_iOS(int size) :
  _values(new unsigned char[size]),
  _size(size),
  _timestamp(0) {
    
  }
  
  ByteBuffer_iOS(unsigned char*  values, int size) :
  _values(values),
  _size(size),
  _timestamp(0) {
    
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
    return _values[i];
  }
  
  void put(int i, unsigned char value) {
    if (_values[i] != value) {
      _values[i] = value;
      _timestamp++;
    }
  }
  
  unsigned char* getPointer() const {
    return _values;
  }
  
  const std::string description() const;

};

#endif
