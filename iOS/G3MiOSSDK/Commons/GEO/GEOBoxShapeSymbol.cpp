//
//  GEOBoxShapeSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/28/13.
//
//

#include "GEOBoxShapeSymbol.hpp"

#include "BoxShape.hpp"

#include "GEOBoxShapeStyle.hpp"

GEOBoxShapeSymbol::GEOBoxShapeSymbol(const Geodetic3D& position,
                                     const GEOBoxShapeStyle& style) :
_position(position),
_extent(style.getExtent()),
_borderWidth(style.getBorderWidth()),
_surfaceColor(style.getSurfaceColor()),
_borderColor(style.getBorderColor())
{

}

Shape* GEOBoxShapeSymbol::createShape(const G3MRenderContext* rc) const {
  return new BoxShape(new Geodetic3D(_position),
                      _extent,
                      _borderWidth,
                      _surfaceColor,
                      _borderColor);
}
