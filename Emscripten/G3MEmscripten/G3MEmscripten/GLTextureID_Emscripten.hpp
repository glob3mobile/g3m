//
//  GLTextureID_Emscripten.hpp
//  G3MEmscripten
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 1/9/20.
//  Copyright © 2020 DIEGO RAMIRO GOMEZ-DECK. All rights reserved.
//

#ifndef GLTextureID_Emscripten_hpp
#define GLTextureID_Emscripten_hpp

#include "G3M/IGLTextureID.hpp"

#include <emscripten/val.h>


class GLTextureID_Emscripten : public IGLTextureID {
private:
  const emscripten::val _webGLTexture;
  
public:
  GLTextureID_Emscripten(const emscripten::val& webGLTexture);

  bool isEquals(const IGLTextureID* that) const;

  const emscripten::val getWebGLTexture() const;

  const std::string description() const;
  
};

#endif
