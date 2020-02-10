//
//  MultiTextureMapping.hpp
//  G3M
//
//  Created by Jose Miguel SN on 24/12/13.
//
//

#ifndef __G3M__MultiTextureMapping__
#define __G3M__MultiTextureMapping__

#include "TransformableTextureMapping.hpp"

class TextureIDReference;
class IFloatBuffer;
class IGLTextureID;

class MultiTextureMapping : public TransformableTextureMapping {
private:
#ifdef C_CODE
  const TextureIDReference* _glTextureID1;
  const TextureIDReference* _glTextureID2;
#endif
#ifdef JAVA_CODE
  private TextureIDReference _glTextureID1;
  private TextureIDReference _glTextureID2;
#endif

  IFloatBuffer* _texCoords1;
  const bool    _ownedTexCoords1;

  IFloatBuffer* _texCoords2;
  const bool    _ownedTexCoords2;

  const bool _transparent1;
  const bool _transparent2;

  void releaseGLTextureID();

public:

  MultiTextureMapping(const TextureIDReference* glTextureID1,
                      IFloatBuffer* texCoords1,
                      bool ownedTexCoords1,
                      bool transparent1,
                      const TextureIDReference* glTextureID2,
                      IFloatBuffer* texCoords2,
                      bool ownedTexCoords2,
                      bool transparent2) :
  TransformableTextureMapping(0, 0,
                              1, 1,
                              0, 0, 0),
  _glTextureID1(glTextureID1),
  _texCoords1(texCoords1),
  _ownedTexCoords1(ownedTexCoords1),
  _transparent1(transparent1),
  _glTextureID2(glTextureID2),
  _texCoords2(texCoords2),
  _ownedTexCoords2(ownedTexCoords2),
  _transparent2(transparent2)
  {
  }

  MultiTextureMapping(const TextureIDReference* glTextureID1,
                      IFloatBuffer* texCoords1,
                      bool ownedTexCoords1,
                      bool transparent1,
                      const TextureIDReference* glTextureID2,
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
  _glTextureID1(glTextureID1),
  _texCoords1(texCoords1),
  _ownedTexCoords1(ownedTexCoords1),
  _transparent1(transparent1),
  _glTextureID2(glTextureID2),
  _texCoords2(texCoords2),
  _ownedTexCoords2(ownedTexCoords2),
  _transparent2(transparent2)
  {
  }

  virtual ~MultiTextureMapping();

  const IGLTextureID* getGLTextureID() const;

  IFloatBuffer* getTexCoords() const {
    return _texCoords1;
  }

  void modifyGLState(GLState& state) const;
  
};

#endif
