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

emscripten::val GLUniformID_Emscripten::getId() {
  return _id;
}

bool GLUniformID_Emscripten::isValid() {
  return !_id.isNull();
}

GLUniformID_Emscripten::~GLUniformID_Emscripten() {

}
