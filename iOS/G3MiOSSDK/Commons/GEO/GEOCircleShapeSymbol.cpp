//
//  GEOCircleShapeSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/27/13.
//
//

#include "GEOCircleShapeSymbol.hpp"

#include "CircleShape.hpp"

#include "GEOCircleShapeStyle.hpp"

GEOCircleShapeSymbol::GEOCircleShapeSymbol(const Geodetic3D& position,
                                           const GEOCircleShapeStyle& style) :
_position(position),
_radius(style.getRadius()),
_color(new Color(style.getColor())),
_steps(style.getSteps())
{

}


Shape* GEOCircleShapeSymbol::createShape(const G3MRenderContext* rc) const {
  return new CircleShape(new Geodetic3D(_position),
                         _radius,
                         _color,
                         _steps);
}
