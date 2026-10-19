//
//  ShapeFullPositionEffect.cpp
//  G3M
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
  _shape->setPosition(pos);
  
  if (!_fromPitch.isNan() && !_toPitch.isNan()) {
    _shape->setPitch(Angle::linearInterpolation(_fromPitch, _toPitch, alpha));
  }
  
  if (!_fromHeading.isNan() && !_toHeading.isNan()) {
    // the short way: at most half a turn
    _shape->setHeading(Angle::linearInterpolationFromRadians(_fromHeading._radians,
                                                             Angle::smoothRadians(_fromHeading._radians, _toHeading._radians),
                                                             alpha));
  }

  if (!_fromRoll.isNan() && !_toRoll.isNan()) {
    // the short way: at most half a turn
    _shape->setRoll(Angle::linearInterpolationFromRadians(_fromRoll._radians,
                                                          Angle::smoothRadians(_fromRoll._radians, _toRoll._radians),
                                                          alpha));
  }
}

void ShapeFullPositionEffect::cancel(const TimeInterval& when) {
  if (_forceToPositionOnCancel) {
    _shape->setPosition(_toPosition);
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
}

void ShapeFullPositionEffect::stop(const G3MRenderContext* rc,
                                   const TimeInterval& when) {
  if (_forceToPositionOnStop) {
    _shape->setPosition(_toPosition);
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
}
