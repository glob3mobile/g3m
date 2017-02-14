//
//  RectangleF.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 19/04/13.
//
//

#ifndef G3MiOSSDK_RectangleF
#define G3MiOSSDK_RectangleF


#include <string>

class Sector;


class RectangleF {
private:

public:
  const float _x;
  const float _y;
  const float _width;
  const float _height;

  RectangleF(float x, float y,
             float width, float height);

  RectangleF(const RectangleF& that) :
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

  bool fullContains(const RectangleF& that) const;

  static bool fullContains(float outerX, float outerY, float outerWidth, float outerHeight,
                           float innerX, float innerY, float innerWidth, float innerHeight);

  bool contains(float x, float y) const {
    return (x >= _x) && (y >= _y) && (x <= (_x + _width)) && (y <= (_y + _height));
  }

  const std::string description() const;

  const std::string id() const;

  static RectangleF* calculateInnerRectangleFromSector(int wholeSectorWidth,
                                                       int wholeSectorHeight,
                                                       const Sector& wholeSector,
                                                       const Sector& innerSector);

};

#endif
