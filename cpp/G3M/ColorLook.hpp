//
//  ColorLook.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/9/26.
//

#ifndef G3M_ColorLook
#define G3M_ColorLook

#include "Angle.hpp"
#include "Color.hpp"

class Matrix44D;


// The parameters of a tile color grade (PlanetRenderer::setColorMatrix).
// Interpolating the parameters, not the matrices, keeps every step of a transition a real look.
class ColorLook {
private:
  // releases both
  static Matrix44D* createSequence(Matrix44D* first,
                                   Matrix44D* second);

public:
  static ColorLook neutral();

  static ColorLook linearInterpolation(const ColorLook& from,
                                       const ColorLook& to,
                                       double alpha);

  const double _temperature; // kelvin, see ColorMatrix::createWhiteBalance
  const double _saturation;
  const Angle  _hue;
  const double _contrast;
  const double _brightness;
  const Color  _tint;

  ColorLook(const double temperature,
            const double saturation,
            const Angle& hue,
            const double contrast,
            const double brightness,
            const Color& tint) :
  _temperature(temperature),
  _saturation(saturation),
  _hue(hue),
  _contrast(contrast),
  _brightness(brightness),
  _tint(tint)
  {
  }

  ColorLook(const ColorLook& that) :
  _temperature(that._temperature),
  _saturation(that._saturation),
  _hue(that._hue),
  _contrast(that._contrast),
  _brightness(that._brightness),
  _tint(that._tint)
  {
  }

  virtual ~ColorLook() {
  }

  bool isEquals(const ColorLook& that) const;

  // applied in this order: white balance, saturation, hue rotation, contrast, brightness, tint
  Matrix44D* createColorMatrix() const;

};

#endif
