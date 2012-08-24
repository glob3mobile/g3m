//
//  GLTextureId.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 09/08/12.
//
//

#include "GLTextureId.hpp"

#include <sstream>

const std::string GLTextureId::description() const {
  std::ostringstream buffer;
  buffer << "GLTextureId #";
  buffer << _textureId;
  return buffer.str();
}
