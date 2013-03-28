//
//  GEOBoxShapeSymbol.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/28/13.
//
//

#ifndef __G3MiOSSDK__GEOBoxShapeSymbol__
#define __G3MiOSSDK__GEOBoxShapeSymbol__

#include "GEOShapeSymbol.hpp"

#include "Geodetic3D.hpp"
#include "Vector3D.hpp"
//#include "Color.hpp"
class Color;
class GEOBoxShapeStyle;
//#include "GEOBoxShapeStyle.hpp"

class GEOBoxShapeSymbol : public GEOShapeSymbol {
private:
  const Geodetic3D _position;
  const Vector3D   _extent;

  const float _borderWidth;
  Color*      _surfaceColor;
  Color*      _borderColor;

protected:
  Shape* createShape(const G3MRenderContext* rc) const;

public:
  GEOBoxShapeSymbol(const Geodetic3D& position,
                    const GEOBoxShapeStyle& style);

};

#endif
