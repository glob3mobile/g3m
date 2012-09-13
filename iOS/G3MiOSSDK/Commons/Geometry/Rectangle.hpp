//
//  Rectangle.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 24/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_Rectangle_hpp
#define G3MiOSSDK_Rectangle_hpp

class Rectangle
{
public:
  const double _x, _y, _width, _height;
  
  Rectangle(double x, double y, double width, double height): 
  _x(x), _y(y), _width(width), _height(height){}
  
  Rectangle(const Rectangle& rec):
  _x(rec._x), _y(rec._y), _width(rec._width), _height(rec._height){}
  
  bool equalTo(const Rectangle& r) const{
    return _x == r._x && _y == r._y && _width == r._width && _height == r._height;
  }
};

#endif
