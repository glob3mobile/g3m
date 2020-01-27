//
//  GPUAttributeValueVec1Float.cpp
//  G3MiOSSDK
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 1/4/19.
//

#include "GPUAttributeValueVec1Float.hpp"

GPUAttributeValueVec1Float::GPUAttributeValueVec1Float(IFloatBuffer* buffer,
                                                       int arrayElementSize,
                                                       int index,
                                                       int stride,
                                                       bool normalized) :
GPUAttributeValueVecFloat(buffer,
                          1,
                          arrayElementSize,
                          index,
                          stride,
                          normalized)
{

}
