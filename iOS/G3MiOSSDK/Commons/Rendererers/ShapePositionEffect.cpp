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
#ifdef C_CODE
  _shape->setPosition(new Geodetic3D(pos));
#endif
#ifdef JAVA_CODE
  _shape.setPosition(pos);
#endif
}

void ShapePositionEffect::cancel(const TimeInterval& when) {
#ifdef C_CODE
  _shape->setPosition( new Geodetic3D(_toPosition) );
#endif
#ifdef JAVA_CODE
  _shape.setPosition(_toPosition);
#endif
}

void ShapePositionEffect::stop(const G3MRenderContext* rc,
                               const TimeInterval& when) {
#ifdef C_CODE
  _shape->setPosition( new Geodetic3D(_toPosition) );
#endif
#ifdef JAVA_CODE
  _shape.setPosition(_toPosition);
#endif
}
