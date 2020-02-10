//
//  GPUAttributeValueVec1Float.hpp
//  G3M
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 1/4/19.
//

#ifndef GPUAttributeValueVec1Float_hpp
#define GPUAttributeValueVec1Float_hpp

#include "GPUAttributeValueVecFloat.hpp"


class GPUAttributeValueVec1Float: public GPUAttributeValueVecFloat {
private:
  ~GPUAttributeValueVec1Float() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:
  GPUAttributeValueVec1Float(IFloatBuffer* buffer,
                             int arrayElementSize,
                             int index,
                             int stride,
                             bool normalized);
};

#endif
