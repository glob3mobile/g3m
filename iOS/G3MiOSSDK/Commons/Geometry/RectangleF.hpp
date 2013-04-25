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
  
  static bool isInRange(float x, float min, float max){
    if (min > max){
      float q = min;
      min = max;
      max = q;
    }
    return (x >= min) && (x <= max);
  }
  
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
  
  bool fullContains(const RectangleF& that) const{
    
    if (!isInRange(that._x, _x, _x + _width)) return false;
    if (!isInRange(that._x + that._width, _x, _x + _width)) return false;
    
    if (!isInRange(that._y, _y, _y + _height)) return false;
    if (!isInRange(that._y + that._height, _y, _y + _height)) return false;
    
    return true;
  }
  
};

#endif
