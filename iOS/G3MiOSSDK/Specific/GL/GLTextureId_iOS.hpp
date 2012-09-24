//
//  GLTextureId_iOS.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 20/09/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_GLTextureId_iOS_hpp
#define G3MiOSSDK_GLTextureId_iOS_hpp

#include "IStringBuilder.hpp"


#include "IGLTextureId.hpp"

class GLTextureId_iOS: public IGLTextureId{
private:
  int _textureId;
public:
  
  
  GLTextureId_iOS() {
    _textureId = -1;
  }
  
  GLTextureId_iOS(int x) {
    _textureId = x;
  }

  GLTextureId_iOS(const GLTextureId_iOS* that) :
  _textureId(that->_textureId)
  {
    
  }

  int getGLTextureId() const {
    return _textureId;
  }
  
  const std::string description() const{
    IStringBuilder *isb = IStringBuilder::newStringBuilder();
    isb->add("const GLTextureId*#")->add(_textureId);
    std::string s = isb->getString();
    delete isb;
    return s;
  }
  
  bool isEqualsTo(const IGLTextureId* that) const {
    return (_textureId == ((GLTextureId_iOS*)that)->_textureId);
  }
};


#endif
