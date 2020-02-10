//
//  GPUAttributeVec1Float.cpp
//  G3M
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 1/4/19.
//

#include "GPUAttributeVec1Float.hpp"

#include "GLConstants.hpp"


GPUAttributeVec1Float::GPUAttributeVec1Float(const std::string& name,
                                             int id) :
GPUAttribute(name,
             id,
             GLType::glFloat(),
             1)
{

}
