//
//  GEOLine2DMeshSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/25/13.
//
//

#include "GEOLine2DMeshSymbol.hpp"

#include "GEOLine2DStyle.hpp"
#include "Context.hpp"
#include "Planet.hpp"

GEOLine2DMeshSymbol::GEOLine2DMeshSymbol(const std::vector<Geodetic2D*>* coordinates,
                                         const GEOLine2DStyle& style,
                                         double deltaHeight) :
_coordinates(coordinates),
_lineColor(style.getColor()),
_lineWidth(style.getWidth()),
_deltaHeight(deltaHeight)
{

}

Mesh* GEOLine2DMeshSymbol::createMesh(const G3MRenderContext* rc) const {
  return createLine2DMesh(_coordinates,
                          _lineColor,
                          _lineWidth,
                          _deltaHeight,
                          rc->getPlanet());
}
