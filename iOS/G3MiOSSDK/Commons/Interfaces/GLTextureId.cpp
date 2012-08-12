//
//  GLTextureID.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 09/08/12.
//
//

#include "GLTextureID.hpp"

#include <sstream>

const std::string GLTextureID::description() const {
  std::ostringstream buffer;
  buffer << "GLTextureID #";
  buffer << _textureId;
  return buffer.str();
}
