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
  const unsigned char * const _data;
  const unsigned int _dataLength;
  
  bool _hasBeenReleased;
  
public:
  ByteBuffer(const ByteBuffer& bb) :_data(bb._data), _dataLength(bb._dataLength), _hasBeenReleased(bb._hasBeenReleased){};
  ByteBuffer() :_data(NULL), _dataLength(0), _hasBeenReleased(true){};
  ByteBuffer(  const unsigned char * const data, unsigned int dataLength) :_data(data), _dataLength(dataLength), _hasBeenReleased(false){};
  
  const unsigned char * getData() const{ if (!_hasBeenReleased) return _data; else return NULL;}
  unsigned int getDataLength() const{ if (!_hasBeenReleased) return _dataLength; else return 0;}
  
  void release(){ 
    if (!_hasBeenReleased){
#ifdef C_CODE
      delete [] _data;
#endif
      _hasBeenReleased = true;
    }
  }
};

#endif
