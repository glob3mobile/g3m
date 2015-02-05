//
//  TextureMapping.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 12/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_TextureMapping
#define G3MiOSSDK_TextureMapping

class GLState;

class TextureMapping {
public:
  
  virtual ~TextureMapping() {
  }

  virtual void modifyGLState(GLState& state) const = 0;
};

#endif
