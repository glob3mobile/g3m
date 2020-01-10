//
//  GLUniformID_Emscripten.cpp
//  G3MEmscriptenSDK
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 1/9/20.
//  Copyright © 2020 DIEGO RAMIRO GOMEZ-DECK. All rights reserved.
//

#include "GLUniformID_Emscripten.hpp"

GLUniformID_Emscripten::GLUniformID_Emscripten(const emscripten::val& id) :
_id(id)
{

}

const emscripten::val GLUniformID_Emscripten::getId() const {
  return _id;
}

bool GLUniformID_Emscripten::isValid() const {
  return !_id.isNull();
}

GLUniformID_Emscripten::~GLUniformID_Emscripten() {

}
