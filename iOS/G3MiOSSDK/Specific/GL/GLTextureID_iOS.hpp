//
//  GLTextureID_iOS.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 20/09/12.
//

#ifndef G3MiOSSDK_GLTextureID_iOS
#define G3MiOSSDK_GLTextureID_iOS

#include "G3M/IGLTextureID.hpp"

#include "G3M/IStringBuilder.hpp"


class GLTextureID_iOS: public IGLTextureID {
private:
  const unsigned int _textureID;

  GLTextureID_iOS(const GLTextureID_iOS* that);

public:
  GLTextureID_iOS(unsigned int textureID) :
  _textureID(textureID)
  {
  }

  unsigned int getGLTextureID() const {
    return _textureID;
  }

  const std::string description() const {
    IStringBuilder* isb = IStringBuilder::newStringBuilder();
    isb->addString("(GLTextureID_iOS #");
    isb->addInt(_textureID);
    isb->addString(")");
    const std::string s = isb->getString();
    delete isb;
    return s;
  }

  bool isEquals(const IGLTextureID* that) const {
    return (_textureID == ((GLTextureID_iOS*) that)->_textureID);
  }

};

#endif
