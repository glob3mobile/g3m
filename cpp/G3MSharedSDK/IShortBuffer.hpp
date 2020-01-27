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

  virtual short get(const size_t i) const = 0;

  virtual void put(const size_t i,
                   short value) = 0;

  virtual void rawPut(const size_t i,
                      short value) = 0;
  
};

#endif
