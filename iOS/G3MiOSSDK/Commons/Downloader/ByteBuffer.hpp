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
  
public:
  
  ByteBuffer(  const unsigned char * const data, unsigned int dataLength) :_data(data), _dataLength(dataLength){};
  ~ByteBuffer(){ //ByteBuffer does not delete the data
  }
  
  const unsigned char * getData() const{ return _data;}
  unsigned int getDataLength() const{ return _dataLength;}
};

#endif
