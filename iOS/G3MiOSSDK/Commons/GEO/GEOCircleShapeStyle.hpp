//
//  GEOCircleShapeStyle.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/27/13.
//
//

#ifndef __G3MiOSSDK__GEOCircleShapeStyle__
#define __G3MiOSSDK__GEOCircleShapeStyle__

#include "Color.hpp"

#include "GEOShapeStyle.hpp"

class GEOCircleShapeStyle : public GEOShapeStyle {
private:
  const float _radius;
  const Color _color;
  const int   _steps;

public:
  GEOCircleShapeStyle(const float radius,
                      const Color& color,
                      const int steps) :
  _radius(radius),
  _color(color),
  _steps(steps)
  {

  }

  const float getRadius() const {
    return _radius;
  }

  const Color getColor() const {
    return _color;
  }

  const int getSteps() const {
    return _steps;
  }

};

#endif
