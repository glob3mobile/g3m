//
//  GPUAttributeVec3Float.cpp
//  G3MiOSSDK
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 1/4/19.
//

#include "GPUAttributeVec3Float.hpp"

#include "GLConstants.hpp"


GPUAttributeVec3Float::GPUAttributeVec3Float(const std::string& name,
                                             int id) :
GPUAttribute(name,
             id,
             GLType::glFloat(),
             3)
{

}
