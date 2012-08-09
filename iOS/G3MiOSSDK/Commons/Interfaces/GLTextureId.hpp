//
//  GLTextureId.h
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 09/08/12.
//
//

#ifndef __G3MiOSSDK__GLTextureId__
#define __G3MiOSSDK__GLTextureId__

#include <string>


class GLTextureId {
private:
  int _textureId;
  
public:
  static GLTextureId invalid() {
    return GLTextureId(-1);
  }
  
  explicit GLTextureId(int textureId) :
  _textureId(textureId)
  {
    
  }
  
  bool isValid() const {
    return (_textureId > 0);
  }
  
  int getGLTextureId() const {
    return _textureId;
  }
  
  std::string description() const;
  
  bool isEqualsTo(const GLTextureId& that) const {
    return (_textureId == that._textureId);
  }
};


#endif
