//
//  RectangleI.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/21/12.
//
//

#ifndef __G3MiOSSDK__RectangleI__
#define __G3MiOSSDK__RectangleI__


class RectangleI {
public:
  const int _x;
  const int _y;
  const int _width;
  const int _height;

  RectangleI(int x, int y,
             int width, int height):
  _x(x),
  _y(y),
  _width(width),
  _height(height)
  {
  }

  RectangleI(const RectangleI& that):
  _x(that._x),
  _y(that._y),
  _width(that._width),
  _height(that._height)
  {
  }

  ~RectangleI() {
  }

  bool equalTo(const RectangleI& that) const{
    return (_x == that._x) && (_y == that._y) && (_width == that._width) && (_height == that._height);
  }

  bool contains(int x, int y) const {
    return (x >= _x) && (y >= _y) && (x <= (_x + _width)) && (y <= (_y + _height));
  }

};

#endif
