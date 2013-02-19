//
//  ShapePositionEffect.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/1/13.
//
//

#include "ShapePositionEffect.hpp"

#include "Shape.hpp"

void ShapePositionEffect::doStep(const G3MRenderContext *rc,
                                 const TimeInterval& when) {
  const double percent = percentDone(when);
  const double alpha = _linearInterpolation ? percent : pace( percent );

  const Geodetic3D pos = Geodetic3D::interpolation(_fromPosition, _toPosition, alpha);
  _shape->setPosition(new Geodetic3D(pos));
}

void ShapePositionEffect::cancel(const TimeInterval& when) {
  _shape->setPosition( new Geodetic3D(_toPosition) );
}

void ShapePositionEffect::stop(const G3MRenderContext *rc,
                               const TimeInterval& when) {
  _shape->setPosition( new Geodetic3D(_toPosition) );
}
