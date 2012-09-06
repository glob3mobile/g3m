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

class FloatBuffer_iOS : public IFloatBuffer {
private:
  const int _size;
  float*    _values;
  
  int _timestamp;
  
public:
  FloatBuffer_iOS(int size) :
  _size(size),
  _timestamp(0)
  {
    _values = new float[size];
  }

  virtual ~FloatBuffer_iOS() {
    delete [] _values;
  }
  
  
  int size() const {
    return _size;
  }
  
  long long timestamp() const {
    return _timestamp;
  }
  
  float get(int i) const {
    return _values[i];
  }
  
  void put(int i, float value) {
    if (_values[i] != value) {
      _values[i] = value;
      _timestamp++;
    }
  }
  
  
};

#endif
