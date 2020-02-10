//
//  SimpleTextureMapping.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 1/9/14.
//
//

#ifndef __G3M__SimpleTextureMapping__
#define __G3M__SimpleTextureMapping__

#include "TransformableTextureMapping.hpp"

class TextureIDReference;
class IFloatBuffer;
class IGLTextureID;

class SimpleTextureMapping : public TransformableTextureMapping {
private:
#ifdef C_CODE
  const TextureIDReference* _glTextureID;
#endif
#ifdef JAVA_CODE
  private TextureIDReference _glTextureID;
#endif

  IFloatBuffer* _texCoords;
  const bool    _ownedTexCoords;

  const bool _transparent;

  void releaseGLTextureID();

public:

  SimpleTextureMapping(const TextureIDReference* glTextureID,
                       IFloatBuffer* texCoords,
                       bool ownedTexCoords,
                       bool transparent) :
  TransformableTextureMapping(0, 0,
                              1, 1,
                              0, 0, 0),
  _glTextureID(glTextureID),
  _texCoords(texCoords),
  _ownedTexCoords(ownedTexCoords),
  _transparent(transparent)
  {
  }

  SimpleTextureMapping(const TextureIDReference* glTextureID,
                       IFloatBuffer* texCoords,
                       bool ownedTexCoords,
                       bool transparent,
                       float translationU,
                       float translationV,
                       float scaleU,
                       float scaleV,
                       float rotationAngleInRadians,
                       float rotationCenterU,
                       float rotationCenterV) :
  TransformableTextureMapping(translationU,
                              translationV,
                              scaleU,
                              scaleV,
                              rotationAngleInRadians,
                              rotationCenterU,
                              rotationCenterV),
  _glTextureID(glTextureID),
  _texCoords(texCoords),
  _ownedTexCoords(ownedTexCoords),
  _transparent(transparent)
  {
  }

  virtual ~SimpleTextureMapping();

  const IGLTextureID* getGLTextureID() const;

  IFloatBuffer* getTexCoords() const {
    return _texCoords;
  }

  void modifyGLState(GLState& state) const;
  
};

#endif
