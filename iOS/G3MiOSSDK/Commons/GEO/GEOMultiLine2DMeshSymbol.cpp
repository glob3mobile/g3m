//
//  GEOMultiLine2DMeshSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/26/13.
//
//

#include "GEOMultiLine2DMeshSymbol.hpp"

#include "GEOLine2DStyle.hpp"
#include "Context.hpp"
#include "Planet.hpp"

GEOMultiLine2DMeshSymbol::GEOMultiLine2DMeshSymbol(const std::vector<std::vector<Geodetic2D*>*>* coordinatesArray,
                                                   const GEOLine2DStyle& style,
                                                   double deltaHeight) :
_coordinatesArray(coordinatesArray),
_lineColor(style.getColor()),
_lineWidth(style.getWidth()),
_deltaHeight(deltaHeight)
{

}

Mesh* GEOMultiLine2DMeshSymbol::createMesh(const G3MRenderContext* rc) const {
  return createLines2DMesh(_coordinatesArray,
                           _lineColor,
                           _lineWidth,
                           _deltaHeight,
                           rc->getPlanet());
}
