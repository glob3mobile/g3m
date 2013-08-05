//
//  IIntBuffer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 05/09/12.
//
//

#ifndef __G3MiOSSDK__IIntBuffer__
#define __G3MiOSSDK__IIntBuffer__

#include "IBuffer.hpp"

class IIntBuffer : public IBuffer {
public:

  virtual ~IIntBuffer() {
  }

  virtual int get(int i) const = 0;

  virtual void put(int i, int value) = 0;

  virtual void rawPut(int i, int value) = 0;

};

#endif
