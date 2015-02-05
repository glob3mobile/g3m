//
//  MultiTextureMapping.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 24/12/13.
//
//

#ifndef __G3MiOSSDK__MultiTextureMapping__
#define __G3MiOSSDK__MultiTextureMapping__

#include "TransformableTextureMapping.hpp"

class TextureIDReference;
class IFloatBuffer;
class IGLTextureId;

class MultiTextureMapping : public TransformableTextureMapping {
private:
#ifdef C_CODE
  const TextureIDReference* _glTextureId;
  const TextureIDReference* _glTextureId2;
#endif
#ifdef JAVA_CODE
  private TextureIDReference _glTextureId;
  private TextureIDReference _glTextureId2;
#endif

  IFloatBuffer* _texCoords;
  const bool    _ownedTexCoords;

  IFloatBuffer* _texCoords2;
  const bool    _ownedTexCoords2;

  const bool _transparent;
  const bool _transparent2;

  void releaseGLTextureId();

public:

  MultiTextureMapping(const TextureIDReference* glTextureId,
                      IFloatBuffer* texCoords,
                      bool ownedTexCoords,
                      bool transparent,
                      const TextureIDReference* glTextureId2,
                      IFloatBuffer* texCoords2,
                      bool ownedTexCoords2,
                      bool transparent2) :
  TransformableTextureMapping(0, 0,
                              1, 1,
                              0, 0, 0),
  _glTextureId(glTextureId),
  _texCoords(texCoords),
  _ownedTexCoords(ownedTexCoords),
  _transparent(transparent),
  _glTextureId2(glTextureId2),
  _texCoords2(texCoords2),
  _ownedTexCoords2(ownedTexCoords2),
  _transparent2(transparent2)
  {
  }

  MultiTextureMapping(const TextureIDReference* glTextureId,
                      IFloatBuffer* texCoords,
                      bool ownedTexCoords,
                      bool transparent,
                      const TextureIDReference* glTextureId2,
                      IFloatBuffer* texCoords2,
                      bool ownedTexCoords2,
                      bool transparent2,
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
  _transparent(transparent),
  _glTextureId2(glTextureId2),
  _texCoords2(texCoords2),
  _ownedTexCoords2(ownedTexCoords2),
  _transparent2(transparent2)
  {
  }

  virtual ~MultiTextureMapping();

  const IGLTextureId* getGLTextureId() const;

  IFloatBuffer* getTexCoords() const {
    return _texCoords;
  }

  void modifyGLState(GLState& state) const;
  
};

#endif
