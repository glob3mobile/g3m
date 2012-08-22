//
//  GLTextureID.h
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 09/08/12.
//
//

#ifndef __G3MiOSSDK__GLTextureID__
#define __G3MiOSSDK__GLTextureID__

#include <string>


class GLTextureID {
private:
  int _textureId;
  
public:
  static GLTextureID invalid() {
    return GLTextureID(-1);
  }
  
  GLTextureID(const GLTextureID& that) :
  _textureId(that._textureId)
  {
    
  }
  
  explicit GLTextureID(int textureId) :
  _textureId(textureId)
  {
    
  }
  
  bool isValid() const {
    return (_textureId > 0);
  }
  
  int getGLTextureID() const {
    return _textureId;
  }
  
  const std::string description() const;
  
  bool isEqualsTo(const GLTextureID& that) const {
    return (_textureId == that._textureId);
  }
};


#endif
