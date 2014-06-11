//
//  TransformableTextureMapping.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/9/14.
//
//

#ifndef __G3MiOSSDK__TransformableTextureMapping__
#define __G3MiOSSDK__TransformableTextureMapping__

#include "TextureMapping.hpp"

class TransformableTextureMapping : public TextureMapping {
protected:
  float _translationU;
  float _translationV;
  float _scaleU;
  float _scaleV;
  float _rotationInRadians;
  float _rotationCenterU;
  float _rotationCenterV;

  TransformableTextureMapping(float translationU,
                              float translationV,
                              float scaleU,
                              float scaleV,
                              float rotationAngleInRadians,
                              float rotationCenterU,
                              float rotationCenterV) :
  _translationU(translationU),
  _translationV(translationV),
  _scaleU(scaleU),
  _scaleV(scaleV),
  _rotationInRadians(rotationAngleInRadians),
  _rotationCenterU(rotationCenterU),
  _rotationCenterV(rotationCenterV)
  {
  }

public:
  void setTranslation(float translationU,
                      float translationV);

  void setScale(float scaleU,
                float scaleV);

  void setRotation(float rotationAngleInRadians,
                   float rotationCenterU,
                   float rotationCenterV);

};

#endif
