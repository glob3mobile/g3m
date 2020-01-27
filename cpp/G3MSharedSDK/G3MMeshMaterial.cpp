//
//  G3MMeshMaterial.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/29/14.
//
//

#include "G3MMeshMaterial.hpp"

#include "Color.hpp"
#include "URL.hpp"


G3MMeshMaterial::~G3MMeshMaterial() {
  delete _color;
  delete _textureURL;
}
