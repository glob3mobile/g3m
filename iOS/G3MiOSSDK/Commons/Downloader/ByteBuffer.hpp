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
  unsigned char * _data;
  unsigned int _dataLength;
public:
  
  //CopyData
  ByteBuffer(const ByteBuffer& bb): _dataLength(bb._dataLength){
    _data = new unsigned char[bb._dataLength];
    for(int i = 0; i < bb._dataLength; i++){ 
      _data[i] = bb._data[i];
    }
  };
  
  ~ByteBuffer(){
#ifdef C_CODE
    if (_data != NULL) delete [] _data;
#endif
  }
  
  
  
  
  ByteBuffer() :_data(NULL), _dataLength(0){};
  ByteBuffer( unsigned char * const data, unsigned int dataLength) :_data(data), _dataLength(dataLength){};
  
  unsigned char * getData() const{return _data;}
  unsigned int getDataLength() const{return _dataLength;}
  
};

#endif
