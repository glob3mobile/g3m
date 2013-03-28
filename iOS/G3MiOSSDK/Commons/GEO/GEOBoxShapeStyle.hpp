//
//  GEOBoxShapeStyle.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/28/13.
//
//

#ifndef __G3MiOSSDK__GEOBoxShapeStyle__
#define __G3MiOSSDK__GEOBoxShapeStyle__

#include "GEOShapeStyle.hpp"

#include "Vector3D.hpp"
class Color;

class GEOBoxShapeStyle : public GEOShapeStyle {
private:
  const Vector3D _extent;
  const float    _borderWidth;
  Color*         _surfaceColor;
  Color*         _borderColor;

public:
  GEOBoxShapeStyle(const Vector3D& extent,
                   float borderWidth,
                   Color* surfaceColor,
                   Color* borderColor) :
  _extent(extent),
  _borderWidth(borderWidth),
  _surfaceColor(surfaceColor),
  _borderColor(borderColor)
  {

  }

  const Vector3D getExtent() const {
    return _extent;
  }

  float getBorderWidth() const {
    return _borderWidth;
  }

  Color* getSurfaceColor() const {
    return _surfaceColor;
  }

  Color* getBorderColor() const {
    return _borderColor;
  }
};

#endif

