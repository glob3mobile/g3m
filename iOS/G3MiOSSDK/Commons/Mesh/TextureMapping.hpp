//
//  TextureMapping.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 12/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_TextureMapping_hpp
#define G3MiOSSDK_TextureMapping_hpp

//#include <vector>
//#include "MutableVector2D.hpp"
#include "TexturesHandler.hpp"

class IGLTextureId;

class G3MRenderContext;
class IFloatBuffer;
class GLGlobalState;
class GPUProgramState;
class GLState;
class TexturesHandler;

class TextureMapping {
public:
  
  virtual ~TextureMapping() {
  }

  virtual void modifyGLState(GLState& state) const = 0;
};



class SimpleTextureMapping : public TextureMapping {
private:
#ifdef C_CODE
  const TextureIDReference* _glTextureId;
#endif
#ifdef JAVA_CODE
  private TextureIDReference _glTextureId;
#endif

  IFloatBuffer* _texCoords;
  const bool    _ownedTexCoords;

  float _translationU;
  float _translationV;
  float _scaleU;
  float _scaleV;
  float _rotationInRadians;
  float _rotationCenterU;
  float _rotationCenterV;

  const bool _transparent;

  void releaseGLTextureId();

public:
  
  SimpleTextureMapping(const TextureIDReference* glTextureId,
                       IFloatBuffer* texCoords,
                       bool ownedTexCoords,
                       bool transparent) :
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
  _rotationCenterV(0)
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
  
  virtual ~SimpleTextureMapping();
  
  const IGLTextureId* getGLTextureId() const {
    return _glTextureId->getID();
  }

  IFloatBuffer* getTexCoords() const {
    return _texCoords;
  }
    
  void modifyGLState(GLState& state) const;

};
#endif
