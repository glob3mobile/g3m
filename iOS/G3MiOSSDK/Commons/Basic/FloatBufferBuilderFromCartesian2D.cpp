//
//  FloatBufferBuilderFromCartesian2D.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/9/16.
//
//

#include "FloatBufferBuilderFromCartesian2D.hpp"

#include "Vector2D.hpp"
#include "Vector2F.hpp"


void FloatBufferBuilderFromCartesian2D::add(const Vector2D& vector) {
  _values.push_back( (float) vector._x );
  _values.push_back( (float) vector._y );
}

void FloatBufferBuilderFromCartesian2D::add(const Vector2F& vector) {
  _values.push_back( vector._x );
  _values.push_back( vector._y );
}

void FloatBufferBuilderFromCartesian2D::add(float x, float y) {
  _values.push_back(x);
  _values.push_back(y);
}
