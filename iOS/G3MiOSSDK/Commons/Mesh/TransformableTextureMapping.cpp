//
//  TransformableTextureMapping.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/9/14.
//
//

#include "TransformableTextureMapping.hpp"
void TransformableTextureMapping::setTranslation(float u,
                                                 float v) {
  _translationU = u;
  _translationV = v;
}

void TransformableTextureMapping::setScale(float u,
                                           float v) {
  _scaleU = u;
  _scaleV = v;
}

void TransformableTextureMapping::setRotation(float angleInRadians,
                                              float centerU, float centerV) {
  _rotationInRadians = angleInRadians;
  _rotationCenterU   = centerU;
  _rotationCenterV   = centerV;
}
