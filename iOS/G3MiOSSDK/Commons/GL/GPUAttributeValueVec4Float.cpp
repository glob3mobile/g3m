//
//  GPUAttributeValueVec4Float.cpp
//  G3MiOSSDK
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 1/4/19.
//

#include "GPUAttributeValueVec4Float.hpp"


GPUAttributeValueVec4Float::GPUAttributeValueVec4Float(const IFloatBuffer* buffer,
                                                       int arrayElementSize,
                                                       int index,
                                                       int stride,
                                                       bool normalized) :
GPUAttributeValueVecFloat(buffer,
                          4,
                          arrayElementSize,
                          index,
                          stride,
                          normalized)
{

}
