//
//  ByteArrayWrapper.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 29/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_ByteArrayWrapper_hpp
#define G3MiOSSDK_ByteArrayWrapper_hpp

#include <string>

class OLDByteArrayWrapper {
private:
#ifdef C_CODE
  unsigned char* _data;
#endif
  
#ifdef JAVA_CODE
  byte[] _data;
#endif
  
  const int _length;
  
  OLDByteArrayWrapper(const OLDByteArrayWrapper& that);
  
  OLDByteArrayWrapper& operator=(const OLDByteArrayWrapper& that);
  
public:
  OLDByteArrayWrapper(unsigned char data[],
                      int dataLength) :
  _data(data),
  _length(dataLength)
  {
    int ___REMOVE_this;
  };
  
  OLDByteArrayWrapper* copy() const {
    unsigned char* newData = new unsigned char[_length];
#ifdef C_CODE
    memcpy(newData, _data, _length * sizeof(unsigned char));
#endif
#ifdef JAVA_CODE
    System.arraycopy(_data, 0, newData, 0, _length);
#endif
    return new OLDByteArrayWrapper(newData, _length);
  }
  
  ~OLDByteArrayWrapper(){
#ifdef C_CODE
    if (_data != NULL) {
      delete [] _data;
    }
#endif
  }
  
#ifdef C_CODE
  unsigned char* getData() const {
    return _data;
  }
#endif
  
#ifdef JAVA_CODE
  public byte[] getData() {
    return _data;
  }
#endif
  
  std::string getDataAsString() const;
  
  int getLength() const{
    return _length;
  }
  
  const std::string description() const;
  
};

#endif
