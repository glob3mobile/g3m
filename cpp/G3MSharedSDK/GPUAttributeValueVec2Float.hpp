//
//  GPUAttributeValueVec2Float.hpp
//  G3MiOSSDK
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 1/4/19.
//

#ifndef GPUAttributeValueVec2Float_hpp
#define GPUAttributeValueVec2Float_hpp

#include "GPUAttributeValueVecFloat.hpp"


class GPUAttributeValueVec2Float: public GPUAttributeValueVecFloat {
private:
  ~GPUAttributeValueVec2Float() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }
  
public:
  GPUAttributeValueVec2Float(IFloatBuffer* buffer,
                             int arrayElementSize,
                             int index,
                             int stride,
                             bool normalized);
  
};

#endif
