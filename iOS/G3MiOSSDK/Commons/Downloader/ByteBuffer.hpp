//
//  ByteBuffer.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 29/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_ByteBuffer_hpp
#define G3MiOSSDK_ByteBuffer_hpp

//THIS CLASS RECEIVES A REFERENCE TO A BYTE ARRAY PREVIOUSLY ALLOCATED
//TO DELETE THE ARRAY CALL release()
class ByteBuffer{
  unsigned char*    _data;
  const int _dataLength;
  
public:
  ByteBuffer(unsigned char* const data,
             int dataLength) :
  _data(data),
  _dataLength(dataLength)
  {
  };

  ByteBuffer(const ByteBuffer& bb) :
  _dataLength(bb._dataLength),
  _data(new unsigned char[bb._dataLength])
  {
    //    for(int i = 0; i < l; i++) {
    //      _data[i] = bb._data[i];
    //    }
    memcpy(_data, bb._data, _dataLength * sizeof(unsigned char));
  };
  
  ~ByteBuffer(){
#ifdef C_CODE
    if (_data != NULL) delete [] _data;
#endif
  }
  
  unsigned char* getData() const{
    return _data;
  }
  
  int getDataLength() const{
    return _dataLength;
  }
  
};

#endif
