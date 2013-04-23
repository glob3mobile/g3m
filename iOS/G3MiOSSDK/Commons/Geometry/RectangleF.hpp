//
//  RectangleF.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 19/04/13.
//
//

#ifndef G3MiOSSDK_RectangleF_h
#define G3MiOSSDK_RectangleF_h


class RectangleF {
public:
  const float _x;
  const float _y;
  const float _width;
  const float _height;

  RectangleF(float x, float y,
             float width, float height):
  _x(x),
  _y(y),
  _width(width),
  _height(height)
  {
  }

  RectangleF(const RectangleF& that):
  _x(that._x),
  _y(that._y),
  _width(that._width),
  _height(that._height)
  {
  }

  ~RectangleF() {

  }

  bool equalTo(const RectangleF& that) const{
    return ((_x == that._x)         && (_y == that._y) &&
            (_width == that._width) && (_height == that._height));
  }
  
};

#endif
