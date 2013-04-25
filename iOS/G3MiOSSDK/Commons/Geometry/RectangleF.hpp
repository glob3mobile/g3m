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
private:
  
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
    if (_width < 0 || _height < 0) {
      ILogger::instance()->logError("Invalid rectangle extent");
    }
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
  
  bool equalTo(const RectangleF& that) const {
    return ((_x     == that._x)     && (_y      == that._y) &&
            (_width == that._width) && (_height == that._height));
  }
  
  bool fullContains(const RectangleF& that) const {
    IMathUtils* mu = IMathUtils::instance();
    if (!mu->isBetween(that._x, _x, _x + _width)) {
      return false;
    }
    
    if (!mu->isBetween(that._x + that._width, _x, _x + _width)) {
      return false;
    }
    
    if (!mu->isBetween(that._y, _y, _y + _height)) {
      return false;
    }
    
    if (!mu->isBetween(that._y + that._height, _y, _y + _height)) {
      return false;
    }
    
    return true;
  }
  
  static bool fullContains(float outterX, float outterY, float outterWidth, float outterHeight,
                           float innerX, float innerY, float innerWidth, float innerHeight) {
    IMathUtils* mu = IMathUtils::instance();
    if (!mu->isBetween(innerX, outterX, outterX + outterWidth)) {
      return false;
    }
    
    if (!mu->isBetween(innerX + innerWidth, outterX, outterX + outterWidth)) {
      return false;
    }
    
    if (!mu->isBetween(innerY, outterY, outterY + outterHeight)) {
      return false;
    }
    
    if (!mu->isBetween(innerY + innerHeight, outterY, outterY + outterHeight)) {
      return false;
    }
    
    return true;
  }
  
};

#endif
