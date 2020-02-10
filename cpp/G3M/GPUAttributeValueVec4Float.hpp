//
//  GPUAttributeValueVec4Float.hpp
//  G3M
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 1/4/19.
//

#ifndef GPUAttributeValueVec4Float_hpp
#define GPUAttributeValueVec4Float_hpp

#include "GPUAttributeValueVecFloat.hpp"


class GPUAttributeValueVec4Float: public GPUAttributeValueVecFloat {
private:
  ~GPUAttributeValueVec4Float() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:
  GPUAttributeValueVec4Float(const IFloatBuffer* buffer,
                             int arrayElementSize,
                             int index,
                             int stride,
                             bool normalized);

};

#endif
