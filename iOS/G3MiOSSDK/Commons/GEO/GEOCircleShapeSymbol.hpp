//
//  GEOCircleShapeSymbol.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/27/13.
//
//

#ifndef __G3MiOSSDK__GEOCircleShapeSymbol__
#define __G3MiOSSDK__GEOCircleShapeSymbol__

#include "GEOShapeSymbol.hpp"

class Geodetic3D;
class Color;
class GEOCircleShapeStyle;
#include "Geodetic3D.hpp"

class GEOCircleShapeSymbol : public GEOShapeSymbol {
private:
  const Geodetic3D _position;
  const float _radius;
  Color* _color;
  const int _steps;

protected:
  Shape* createShape(const G3MRenderContext* rc) const;

public:

  GEOCircleShapeSymbol(const Geodetic3D& position,
                       const GEOCircleShapeStyle& style);
  
};

#endif
