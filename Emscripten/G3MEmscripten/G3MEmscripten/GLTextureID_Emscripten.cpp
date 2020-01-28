//
//  GLTextureID_Emscripten.cpp
//  G3MEmscripten
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 1/9/20.
//  Copyright © 2020 DIEGO RAMIRO GOMEZ-DECK. All rights reserved.
//

#include "GLTextureID_Emscripten.hpp"


using namespace emscripten;

GLTextureID_Emscripten::GLTextureID_Emscripten(const val& webGLTexture) :
_webGLTexture(webGLTexture)
{
}

bool GLTextureID_Emscripten::isEquals(const IGLTextureID* that) const {
  GLTextureID_Emscripten* thatEM = (GLTextureID_Emscripten*) that;
  return _webGLTexture.equals(thatEM->getWebGLTexture());
}

const val GLTextureID_Emscripten::getWebGLTexture() const {
  return _webGLTexture;
}

const std::string GLTextureID_Emscripten::description() const {
  return "GLTextureID_Emscripten " + _webGLTexture.call<std::string>("toString");
}
