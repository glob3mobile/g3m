//
//  GPUAttributeValueVec3Float.hpp
//  G3MiOSSDK
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 1/4/19.
//

#ifndef GPUAttributeValueVec3Float_hpp
#define GPUAttributeValueVec3Float_hpp

#include "GPUAttributeValueVecFloat.hpp"


class GPUAttributeValueVec3Float: public GPUAttributeValueVecFloat {
private:
  ~GPUAttributeValueVec3Float() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:
  GPUAttributeValueVec3Float(const IFloatBuffer* buffer,
                             int arrayElementSize,
                             int index,
                             int stride,
                             bool normalized);

};

#endif
