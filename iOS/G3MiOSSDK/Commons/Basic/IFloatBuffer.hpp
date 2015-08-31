//
//  IFloatBuffer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 05/09/12.
//
//

#ifndef __G3MiOSSDK__IFloatBuffer__
#define __G3MiOSSDK__IFloatBuffer__

#include "IBuffer.hpp"

class IFloatBuffer : public IBuffer {
public:

  IFloatBuffer(): IBuffer() {}
  
  virtual ~IFloatBuffer() {
  }
  
  virtual float get(size_t i) const = 0;
  
  virtual void put(size_t i, float value) = 0;

  virtual void rawPut(size_t i, float value) = 0;

  virtual void rawAdd(size_t i, float value) = 0;

  virtual void rawPut(size_t i,
                      const IFloatBuffer* srcBuffer,
                      size_t srcFromIndex,
                      size_t count) = 0;

  void rawPut(size_t i,
              const IFloatBuffer* srcBuffer) {
    rawPut(i, srcBuffer, 0, srcBuffer->size());
  }

};

#endif
