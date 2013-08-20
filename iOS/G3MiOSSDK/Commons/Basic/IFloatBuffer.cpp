//
//  IFloatBuffer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 05/09/12.
//
//

#include "IFloatBuffer.hpp"

#include "GLFeature.hpp"

IFloatBuffer::~IFloatBuffer() {
  int c = 0;

  std::vector<TextureGLFeature*> feas;
  for (int i = 0; i < TextureGLFeature::texFEATURES.size(); i++) {
    if (TextureGLFeature::texFEATURES[i]->_texCoords == this) {
      TextureGLFeature::texFEATURES[i]->_valid = false;
      TextureGLFeature::texFEATURES[i]->_texCoords = NULL;
      c++;
      feas.push_back(TextureGLFeature::texFEATURES[i]);
    }
  }

  if (c > 1){
    printf("INVALIDATING %d !!!!!\n", c);
  }

}

IFloatBuffer::IFloatBuffer(){

  for (int i = 0; i < TextureGLFeature::texFEATURES.size(); i++) {
    if (TextureGLFeature::texFEATURES[i]->_texCoords == this) {
      printf("CREATING DUPLICATED");
    }
  }

}
