//
//  GPUAttributeValueVec2Float.cpp
//  G3M
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 1/4/19.
//

#include "GPUAttributeValueVec2Float.hpp"

GPUAttributeValueVec2Float::GPUAttributeValueVec2Float(IFloatBuffer* buffer,
                                                       int arrayElementSize,
                                                       int index,
                                                       int stride,
                                                       bool normalized):
GPUAttributeValueVecFloat(buffer,
                          2,
                          arrayElementSize,
                          index,
                          stride,
                          normalized)
{

}
