//
//  IByteBuffer.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 10/09/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_IByteBuffer_h
#define G3MiOSSDK_IByteBuffer_h

#include <string>
#include "Disposable.hpp"

class IByteBuffer : public Disposable {
public:

  virtual ~IByteBuffer() {
#ifdef JAVA_CODE
  super.dispose();
#endif

  }

  virtual int size() const = 0;

  virtual int timestamp() const = 0;

  virtual unsigned char get(int i) const = 0;

  virtual void put(int i, unsigned char value) = 0;

  virtual void rawPut(int i, unsigned char value) = 0;

  virtual const std::string description() const = 0;

  virtual const std::string getAsString() const = 0;

};

#endif
