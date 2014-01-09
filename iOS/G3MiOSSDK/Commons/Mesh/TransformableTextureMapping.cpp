//
//  TransformableTextureMapping.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/9/14.
//
//

#include "TransformableTextureMapping.hpp"
void TransformableTextureMapping::setTranslation(float translationU,
                                                 float translationV) {
  _translationU = translationU;
  _translationV = translationV;
#warning    updateState();
}

void TransformableTextureMapping::setScale(float scaleU,
                                           float scaleV) {
  _scaleU = scaleU;
  _scaleV = scaleV;
#warning    updateState();
}

void TransformableTextureMapping::setRotation(float rotationAngleInRadians,
                                              float rotationCenterU,
                                              float rotationCenterV) {
  _rotationInRadians = rotationAngleInRadians;
  _rotationCenterU = rotationCenterU;
  _rotationCenterV = rotationCenterV;
#warning    updateState();
}
