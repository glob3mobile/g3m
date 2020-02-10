//
//  GPUAttributeValueVec3Float.cpp
//  G3M
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 1/4/19.
//

#include "GPUAttributeValueVec3Float.hpp"


GPUAttributeValueVec3Float::GPUAttributeValueVec3Float(const IFloatBuffer* buffer,
                                                       int arrayElementSize,
                                                       int index,
                                                       int stride,
                                                       bool normalized) :
GPUAttributeValueVecFloat(buffer,
                          3,
                          arrayElementSize,
                          index,
                          stride,
                          normalized)
{

}
