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
  
  virtual float get(int i) const = 0;
  
  virtual void put(int i, float value) = 0;

  virtual void rawPut(int i, float value) = 0;

  virtual void rawAdd(int i, float value) = 0;

  virtual void rawPut(int i,
                      const IFloatBuffer* srcBuffer,
                      int srcFromIndex,
                      int count) = 0;

  void rawPut(int i,
              const IFloatBuffer* srcBuffer) {
    rawPut(i, srcBuffer, 0, srcBuffer->size());
  }
};

#endif
