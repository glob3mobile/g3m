//
//  IByteBuffer.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 10/09/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_IByteBuffer_h
#define G3MiOSSDK_IByteBuffer_h

class IByteBuffer {
public:
  
  virtual ~IByteBuffer() { }
  
  virtual int size() const;
  
  virtual int timestamp() const;
  
  virtual unsigned char get(int i) const;
  
  virtual void put(int i, unsigned char value);
  
  virtual float* getPointer() const;
  
};


#endif
