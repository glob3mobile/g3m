//
//  FloatBufferBuilderFromColor.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/9/16.
//
//

#include "FloatBufferBuilderFromColor.hpp"

#include "Color.hpp"

void FloatBufferBuilderFromColor::add(const Color& color) {
  _values.push_back(color._red);
  _values.push_back(color._green);
  _values.push_back(color._blue);
  _values.push_back(color._alpha);
}
