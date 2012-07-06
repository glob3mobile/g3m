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
  const unsigned char * const _data;
  const unsigned int _dataLength;
  
  bool _hasBeenReleased;
  
public:
  
  ByteBuffer(  const unsigned char * const data, unsigned int dataLength) :_data(data), _dataLength(dataLength), _hasBeenReleased(false){};
  
  const unsigned char * getData() const{ if (!_hasBeenReleased) return _data; else return NULL;}
  unsigned int getDataLength() const{ if (!_hasBeenReleased) return _dataLength; else return 0;}
  
  void release(){ delete [] _data; _hasBeenReleased = true;}
};

#endif
