//
//  ColorLook.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/9/26.
//

#include "ColorLook.hpp"

#include "ColorMatrix.hpp"
#include "Matrix44D.hpp"
#include "IMathUtils.hpp"


ColorLook ColorLook::neutral() {
  return ColorLook(6500,           // temperature, neutral white balance
                   1,              // saturation
                   Angle::zero(),  // hue
                   1,              // contrast
                   1,              // brightness
                   Color::white()  // tint
                   );
}

ColorLook ColorLook::linearInterpolation(const ColorLook& from,
                                         const ColorLook& to,
                                         double alpha) {
  const IMathUtils* mu = IMathUtils::instance();
  const float alphaF = (float) alpha;
  return ColorLook(mu->linearInterpolation(from._temperature, to._temperature, alpha),
                   mu->linearInterpolation(from._saturation,  to._saturation,  alpha),
                   Angle::linearInterpolation(from._hue, to._hue, alpha),
                   mu->linearInterpolation(from._contrast,    to._contrast,    alpha),
                   mu->linearInterpolation(from._brightness,  to._brightness,  alpha),
                   Color::fromRGBA(mu->linearInterpolation(from._tint._red,   to._tint._red,   alphaF),
                                   mu->linearInterpolation(from._tint._green, to._tint._green, alphaF),
                                   mu->linearInterpolation(from._tint._blue,  to._tint._blue,  alphaF),
                                   1));
}

bool ColorLook::isEquals(const ColorLook& that) const {
  return ((_temperature == that._temperature) &&
          (_saturation  == that._saturation)  &&
          _hue.isEquals(that._hue)            &&
          (_contrast    == that._contrast)    &&
          (_brightness  == that._brightness)  &&
          _tint.isEquals(that._tint));
}

Matrix44D* ColorLook::createSequence(Matrix44D* first,
                                     Matrix44D* second) {
  Matrix44D* sequence = ColorMatrix::createSequence(first, second);
  first->_release();
  second->_release();
  return sequence;
}

Matrix44D* ColorLook::createColorMatrix() const {
  Matrix44D* result = ColorMatrix::createWhiteBalance(_temperature);
  result = createSequence(result, ColorMatrix::createSaturation(_saturation));
  result = createSequence(result, ColorMatrix::createHueRotation(_hue));
  result = createSequence(result, ColorMatrix::createContrast(_contrast));
  result = createSequence(result, ColorMatrix::createBrightness(_brightness));
  result = createSequence(result, ColorMatrix::createTint(_tint));
  return result;
}
