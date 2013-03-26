//
//  GEOMultiLine2DSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/26/13.
//
//

#include "GEOMultiLine2DSymbol.hpp"

#include "GEOLine2DStyle.hpp"

GEOMultiLine2DSymbol::GEOMultiLine2DSymbol(const std::vector<std::vector<Geodetic2D*>*>* coordinatesArray,
                                           const GEOLine2DStyle& style,
                                           double deltaHeight) :
_coordinatesArray(coordinatesArray),
_lineColor(style.getColor()),
_lineWidth(style.getWidth()),
_deltaHeight(deltaHeight)
{

}

Mesh* GEOMultiLine2DSymbol::createMesh(const G3MRenderContext* rc) {
  return createLines2DMesh(_coordinatesArray,
                           _lineColor,
                           _lineWidth,
                           _deltaHeight,
                           rc);
}
