//
//  GLTextureId_iOS.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 20/09/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_GLTextureId_iOS
#define G3MiOSSDK_GLTextureId_iOS

#include "IStringBuilder.hpp"

#include "IGLTextureId.hpp"

class GLTextureId_iOS: public IGLTextureId {
private:
  const unsigned int _textureId;

  GLTextureId_iOS(const GLTextureId_iOS* that);

public:
  GLTextureId_iOS(unsigned int textureId) :
  _textureId(textureId)
  {
  }

  unsigned int getGLTextureId() const {
    return _textureId;
  }

  const std::string description() const {
    IStringBuilder* isb = IStringBuilder::newStringBuilder();
    isb->addString("(GLTextureId_iOS #");
    isb->addInt(_textureId);
    isb->addString(")");
    const std::string s = isb->getString();
    delete isb;
    return s;
  }

  bool isEquals(const IGLTextureId* that) const {
    return (_textureId == ((GLTextureId_iOS*) that)->_textureId);
  }

};

#endif
