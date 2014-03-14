//
//  RectangleF.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 19/04/13.
//
//

#ifndef G3MiOSSDK_RectangleF
#define G3MiOSSDK_RectangleF


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
    const IMathUtils* mu = IMathUtils::instance();

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

  static bool fullContains(float outerX, float outerY, float outerWidth, float outerHeight,
                           float innerX, float innerY, float innerWidth, float innerHeight) {
    const IMathUtils* mu = IMathUtils::instance();

    if (!mu->isBetween(innerX, outerX, outerX + outerWidth)) {
      return false;
    }

    if (!mu->isBetween(innerX + innerWidth, outerX, outerX + outerWidth)) {
      return false;
    }

    if (!mu->isBetween(innerY, outerY, outerY + outerHeight)) {
      return false;
    }

    if (!mu->isBetween(innerY + innerHeight, outerY, outerY + outerHeight)) {
      return false;
    }
    
    return true;
  }

  bool contains(float x, float y) const {
    return (x >= _x) && (y >= _y) && (x <= (_x + _width)) && (y <= (_y + _height));
  }


};

#endif
