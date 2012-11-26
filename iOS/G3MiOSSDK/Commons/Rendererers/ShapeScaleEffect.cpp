//
//  ShapeScaleEffect.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/23/12.
//
//

#include "ShapeScaleEffect.hpp"

#include "IMathUtils.hpp"
#include "Shape.hpp"

void ShapeScaleEffect::doStep(const G3MRenderContext *rc,
                              const TimeInterval& when) {
  const double alpha = pace( percentDone(when) );

  const double scaleX = GMath.lerp(_fromScaleX, _toScaleX, alpha);
  const double scaleY = GMath.lerp(_fromScaleY, _toScaleY, alpha);
  const double scaleZ = GMath.lerp(_fromScaleZ, _toScaleZ, alpha);

  _shape->setScale(scaleX, scaleY, scaleZ);
}

void ShapeScaleEffect::cancel(const TimeInterval& when) {
  _shape->setScale(_toScaleX, _toScaleY, _toScaleZ);
}
