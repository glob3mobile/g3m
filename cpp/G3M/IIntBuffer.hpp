//
//  IIntBuffer.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 05/09/12.
//
//

#ifndef __G3M__IIntBuffer__
#define __G3M__IIntBuffer__

#include "IBuffer.hpp"

class IIntBuffer : public IBuffer {
public:

  virtual ~IIntBuffer() {
  }

  virtual int get(const size_t i) const = 0;

  virtual void put(const size_t i,
                   int value) = 0;

  virtual void rawPut(const size_t i,
                      int value) = 0;

};

#endif
