//
//  GEOLine2DSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/25/13.
//
//

#include "GEOLine2DSymbol.hpp"

#include "GEOLine2DStyle.hpp"
#include "Context.hpp"
#include "Planet.hpp"

GEOLine2DSymbol::GEOLine2DSymbol(const std::vector<Geodetic2D*>* coordinates,
                                 const GEOLine2DStyle& style,
                                 double deltaHeight) :
_coordinates(coordinates),
_lineColor(style.getColor()),
_lineWidth(style.getWidth()),
_deltaHeight(deltaHeight)
{

}

Mesh* GEOLine2DSymbol::createMesh(const G3MRenderContext* rc) {
  return createLine2DMesh(_coordinates,
                          _lineColor,
                          _lineWidth,
                          _deltaHeight,
                          rc->getPlanet());
}
