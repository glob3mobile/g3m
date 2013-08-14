//
//  IShortBuffer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/18/13.
//
//

#ifndef __G3MiOSSDK__IShortBuffer__
#define __G3MiOSSDK__IShortBuffer__

#include "IBuffer.hpp"

class IShortBuffer : public IBuffer {
public:

  virtual ~IShortBuffer() {
  }

  virtual short get(int i) const = 0;

  virtual void put(int i, short value) = 0;

  virtual void rawPut(int i, short value) = 0;
  
};

#endif
