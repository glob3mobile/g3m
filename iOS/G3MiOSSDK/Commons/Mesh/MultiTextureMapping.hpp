//
//  MultiTextureMapping.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 24/12/13.
//
//

#ifndef __G3MiOSSDK__MultiTextureMapping__
#define __G3MiOSSDK__MultiTextureMapping__

#include "TextureMapping.hpp"

class MultiTextureMapping : public TextureMapping {
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

  //TRANSFORMS FOR TEX 0
  float _translationU;
  float _translationV;
  float _scaleU;
  float _scaleV;
  float _rotationInRadians;
  float _rotationCenterU;
  float _rotationCenterV;

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
  _glTextureId(glTextureId),
  _texCoords(texCoords),
  _ownedTexCoords(ownedTexCoords),
  _transparent(transparent),
  _glTextureId2(glTextureId2),
  _texCoords2(texCoords2),
  _ownedTexCoords2(ownedTexCoords2),
  _transparent2(transparent2),
  _translationU(0),
  _translationV(0),
  _scaleU(1),
  _scaleV(1),
  _rotationInRadians(0),
  _rotationCenterU(0),
  _rotationCenterV(0)
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
  _glTextureId(glTextureId),
  _texCoords(texCoords),
  _ownedTexCoords(ownedTexCoords),
  _transparent(transparent),
  _glTextureId2(glTextureId2),
  _texCoords2(texCoords2),
  _ownedTexCoords2(ownedTexCoords2),
  _transparent2(transparent2),
  _translationU(translationU),
  _translationV(translationV),
  _scaleU(scaleU),
  _scaleV(scaleV),
  _rotationInRadians(rotationAngleInRadians),
  _rotationCenterU(rotationCenterU),
  _rotationCenterV(rotationCenterV)
  {
  }

  void setTranslation(float translationU,
                      float translationV);

  void setScale(float scaleU,
                float scaleV);

  void setRotation(float rotationAngleInRadians,
                   float rotationCenterU,
                   float rotationCenterV);

  virtual ~MultiTextureMapping();

  const IGLTextureId* getGLTextureId() const {
    return _glTextureId->getID();
  }

  IFloatBuffer* getTexCoords() const {
    return _texCoords;
  }

  void modifyGLState(GLState& state) const;
  
};

#endif
