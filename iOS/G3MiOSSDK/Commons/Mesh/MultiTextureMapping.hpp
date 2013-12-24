//
//  MultiTextureMapping.h
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
#endif
#ifdef JAVA_CODE
  private TextureIDReference _glTextureId;
#endif

#ifdef C_CODE
  const TextureIDReference* _glTextureId2;
#endif
#ifdef JAVA_CODE
  private TextureIDReference _glTextureId2;
#endif

  IFloatBuffer* _texCoords;
  const bool    _ownedTexCoords;

  IFloatBuffer* _texCoords2;
  const bool    _ownedTexCoords2;

#warning SHADER NOT READY YET
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
  _translationU(0),
  _translationV(0),
  _scaleU(1),
  _scaleV(1),
  _ownedTexCoords(ownedTexCoords),
  _transparent(transparent),
  _rotationInRadians(0),
  _rotationCenterU(0),
  _rotationCenterV(0),
  _glTextureId2(glTextureId2),
  _texCoords2(texCoords2),
  _ownedTexCoords2(ownedTexCoords2),
  _transparent2(transparent2)
  {
  }

  void setTranslation(float u, float v) {
    _translationU = u;
    _translationV = v;
#warning    updateState();
  }

  void setScale(float u, float v) {
    _scaleU = u;
    _scaleV = v;
#warning    updateState();
  }

  void setRotation(float angleInRadians,
                   float centerU,
                   float centerV) {
    _rotationInRadians = angleInRadians;
    _rotationCenterU = centerU;
    _rotationCenterV = centerV;
#warning    updateState();
  }

  virtual ~MultiTextureMapping();

  const IGLTextureId* getGLTextureId() const {
    return _glTextureId->getID();
  }

  IFloatBuffer* getTexCoords() const {
    return _texCoords;
  }

  void modifyGLState(GLState& state) const;
  
};


#endif /* defined(__G3MiOSSDK__MultiTextureMapping__) */
