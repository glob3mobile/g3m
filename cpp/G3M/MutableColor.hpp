//
//  MutableColor.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 2/10/21.
//

#ifndef MutableColor_hpp
#define MutableColor_hpp

#include "Color.hpp"


class MutableColor {
public:
  float _red;
  float _green;
  float _blue;
  float _alpha;

  MutableColor(const MutableColor& that):
  _red(that._red),
  _green(that._green),
  _blue(that._blue),
  _alpha(that._alpha)
  {
  }

  MutableColor():
  _red(0),
  _green(0),
  _blue(0),
  _alpha(0)
  {
  }

  MutableColor(float red,
               float green,
               float blue,
               float alpha):
  _red(red),
  _green(green),
  _blue(blue),
  _alpha(alpha)
  {
  }

  ~MutableColor() {

  }

  void set(const Color& color) {
    _red   = color._red;
    _green = color._green;
    _blue  = color._blue;
    _alpha = color._alpha;
  }

  void set(float red,
           float green,
           float blue,
           float alpha) {
    _red   = red;
    _green = green;
    _blue  = blue;
    _alpha = alpha;
  }

};

#endif
