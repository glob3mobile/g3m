//
//  GPUAttributeVec4Float.cpp
//  G3MiOSSDK
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 1/4/19.
//

#include "GPUAttributeVec4Float.hpp"

#include "GLConstants.hpp"


GPUAttributeVec4Float::GPUAttributeVec4Float(const std::string& name,
                                             int id) :
GPUAttribute(name,
             id,
             GLType::glFloat(),
             4)
{

}
