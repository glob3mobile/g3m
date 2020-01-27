//
//  TextureMapping.hpp
//  G3M
//
//  Created by José Miguel S N on 12/07/12.
//

#ifndef G3M_TextureMapping
#define G3M_TextureMapping

class GLState;

class TextureMapping {
public:
  
  virtual ~TextureMapping() {
  }

  virtual void modifyGLState(GLState& state) const = 0;
};

#endif
