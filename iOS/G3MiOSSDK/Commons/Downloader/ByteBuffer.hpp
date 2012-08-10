//
//  ByteBuffer.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 29/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_ByteBuffer_hpp
#define G3MiOSSDK_ByteBuffer_hpp

class ByteBuffer{
  unsigned char* _data;
  const int      _length;
  
  ByteBuffer(const ByteBuffer& that);
  
  void operator=(const ByteBuffer& that);
  
public:
  ByteBuffer(unsigned char data[],
             int dataLength) :
  _data(data),
  _length(dataLength)
  {
  };
  
  ByteBuffer* copy() const {
    unsigned char* newData = new unsigned char[_length];
    memcpy(newData, _data, _length * sizeof(unsigned char));
    return new ByteBuffer(newData, _length);
  }

//  ByteBuffer(const ByteBuffer& that) :
//  _length(that._length),
//  _data(new unsigned char[that._length])
//  {
//    memcpy(_data, that._data, _length * sizeof(unsigned char));
//  };
  
  ~ByteBuffer(){
#ifdef C_CODE
    if (_data != NULL) {
      delete [] _data;
    }
#endif
  }
  
  unsigned char* getData() const{
    return _data;
  }
  
  int getLength() const{
    return _length;
  }
  
};

#endif
