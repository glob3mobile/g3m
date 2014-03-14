//
//  Rectangle.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 24/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_Rectangle
#define G3MiOSSDK_Rectangle

class Rectangle {
public:
  const double _x;
  const double _y;
  const double _width;
  const double _height;

  Rectangle(double x, double y,
            double width, double height):
  _x(x),
  _y(y),
  _width(width),
  _height(height)
  {
  }

  Rectangle(const Rectangle& that):
  _x(that._x),
  _y(that._y),
  _width(that._width),
  _height(that._height)
  {
  }

  bool equalTo(const Rectangle& that) const{
    return ((_x == that._x) && (_y == that._y) &&
            (_width == that._width) && (_height == that._height));
  }

};

#endif
