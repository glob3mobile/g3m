//
//  ShapeFullPositionEffect.cpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso Pérez on 03/05/13.
//
//

#include "ShapeFullPositionEffect.hpp"

#include "Shape.hpp"

void ShapeFullPositionEffect::doStep(const G3MRenderContext* rc,
                                     const TimeInterval& when) {
  const double alpha = getAlpha(when);
  
  const Geodetic3D pos = Geodetic3D::linearInterpolation(_fromPosition, _toPosition, alpha);
  _shape->setPosition(new Geodetic3D(pos));
  
  if (!_fromPitch.isNan() && !_toPitch.isNan()) {
    _shape->setPitch(Angle::linearInterpolation(_fromPitch, _toPitch, alpha));
  }
  
  if (!_fromHeading.isNan() && !_toHeading.isNan()) {
    _shape->setHeading(Angle::linearInterpolation(_fromHeading, _toHeading, alpha));
  }

  if (!_fromRoll.isNan() && !_toRoll.isNan()) {
    _shape->setRoll(Angle::linearInterpolation(_fromRoll, _toRoll, alpha));
  }
}

void ShapeFullPositionEffect::cancel(const TimeInterval& when) {
  _shape->setPosition( new Geodetic3D(_toPosition) );
  if (!_toPitch.isNan()) {
    _shape->setPitch(_toPitch);
  }
  
  if (!_toHeading.isNan()) {
    _shape->setHeading(_toHeading);
  }

  if (!_toRoll.isNan()) {
    _shape->setRoll(_toRoll);
  }
}

void ShapeFullPositionEffect::stop(const G3MRenderContext* rc,
                                   const TimeInterval& when) {
  _shape->setPosition( new Geodetic3D(_toPosition) );
  if (!_toPitch.isNan()) {
    _shape->setPitch(_toPitch);
  }
  
  if (!_toHeading.isNan()) {
    _shape->setHeading(_toHeading);
  }

  if (!_toRoll.isNan()) {
    _shape->setRoll(_toRoll);
  }
}
