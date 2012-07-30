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
  
public:
  ByteBuffer(unsigned char* const data,
             int dataLength) :
  _data(data),
  _length(dataLength)
  {
  };

  ByteBuffer(const ByteBuffer& bb) :
  _length(bb._length),
  _data(new unsigned char[bb._length])
  {
    memcpy(_data, bb._data, _length * sizeof(unsigned char));
  };
  
  ~ByteBuffer(){
#ifdef C_CODE
    if (_data != NULL) delete [] _data;
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
