//
//  SimpleTextureMapping.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/9/14.
//
//

#ifndef __G3MiOSSDK__SimpleTextureMapping__
#define __G3MiOSSDK__SimpleTextureMapping__

#include "TransformableTextureMapping.hpp"

class TextureIDReference;
class IFloatBuffer;
class IGLTextureId;

class SimpleTextureMapping : public TransformableTextureMapping {
private:
#ifdef C_CODE
  const TextureIDReference* _glTextureId;
#endif
#ifdef JAVA_CODE
  private TextureIDReference _glTextureId;
#endif

  IFloatBuffer* _texCoords;
  const bool    _ownedTexCoords;

  const bool _transparent;

  void releaseGLTextureId();

public:

  SimpleTextureMapping(const TextureIDReference* glTextureId,
                       IFloatBuffer* texCoords,
                       bool ownedTexCoords,
                       bool transparent) :
  TransformableTextureMapping(0, 0,
                              1, 1,
                              0, 0, 0),
  _glTextureId(glTextureId),
  _texCoords(texCoords),
  _ownedTexCoords(ownedTexCoords),
  _transparent(transparent)
  {
  }

  SimpleTextureMapping(const TextureIDReference* glTextureId,
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
  _glTextureId(glTextureId),
  _texCoords(texCoords),
  _ownedTexCoords(ownedTexCoords),
  _transparent(transparent)
  {
  }

  virtual ~SimpleTextureMapping();

  const IGLTextureId* getGLTextureId() const;

  IFloatBuffer* getTexCoords() const {
    return _texCoords;
  }

  void modifyGLState(GLState& state) const;
  
};

#endif
