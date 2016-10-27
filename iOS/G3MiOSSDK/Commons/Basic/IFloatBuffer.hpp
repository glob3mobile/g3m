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
#include "Vector3D.hpp"

class IFloatBuffer : public IBuffer {
public:

  IFloatBuffer(): IBuffer() {}

  virtual ~IFloatBuffer() {
  }

  virtual float get(const size_t i) const = 0;

  virtual void put(const size_t i,
                   float value) = 0;

  void putVector3D(const size_t i,
                   const Vector3D& v){
    const size_t i3 = i*3;
    put(i3    , (float) v._x);
    put(i3 + 1, (float) v._y);
    put(i3 + 2, (float) v._z);
  }

  virtual void rawPut(const size_t i,
                      float value) = 0;

  virtual void rawAdd(const size_t i,
                      float value) = 0;

  virtual void rawPut(const size_t i,
                      const IFloatBuffer* srcBuffer,
                      const size_t srcFromIndex,
                      const size_t count) = 0;

  void rawPut(const size_t i,
              const IFloatBuffer* srcBuffer) {
    rawPut(i, srcBuffer, 0, srcBuffer->size());
  }

};

#endif
