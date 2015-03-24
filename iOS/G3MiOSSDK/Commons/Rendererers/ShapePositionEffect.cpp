//
//  ShapePositionEffect.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/1/13.
//
//

#include "ShapePositionEffect.hpp"

#include "Shape.hpp"

void ShapePositionEffect::doStep(const G3MRenderContext* rc,
                                 const TimeInterval& when) {
  const double alpha = getAlpha(when);

  const Geodetic3D pos = Geodetic3D::linearInterpolation(_fromPosition, _toPosition, alpha);
  _shape->setPosition(pos);
}

void ShapePositionEffect::cancel(const TimeInterval& when) {
  _shape->setPosition(_toPosition);
}

void ShapePositionEffect::stop(const G3MRenderContext* rc,
                               const TimeInterval& when) {
  _shape->setPosition(_toPosition);
}
