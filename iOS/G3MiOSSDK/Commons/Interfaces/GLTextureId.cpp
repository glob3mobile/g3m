//
//  GLTextureId.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 09/08/12.
//
//

#include "GLTextureId.hpp"

#include "IStringBuilder.hpp"

const std::string GLTextureID::description() const {
  IStringBuilder *isb = IStringBuilder::newStringBuilder();
  isb->add("GLTextureID #")->add(_textureId);
  std::string s = isb->getString();
  delete isb;
  return s;
}
