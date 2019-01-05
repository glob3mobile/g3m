//
//  GPUAttributeVec2Float.cpp
//  G3MiOSSDK
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 1/4/19.
//

#include "GPUAttributeVec2Float.hpp"

#include "GLConstants.hpp"


GPUAttributeVec2Float::GPUAttributeVec2Float(const std::string& name,
                                             int id) :
GPUAttribute(name,
             id,
             GLType::glFloat(),
             2)
{

}
