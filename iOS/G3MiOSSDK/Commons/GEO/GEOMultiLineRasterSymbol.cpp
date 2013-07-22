//
//  GEOMultiLineRasterSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/10/13.
//
//

#include "GEOMultiLineRasterSymbol.hpp"

#include "GEOLine2DStyle.hpp"
#include "ICanvas.hpp"

GEOMultiLineRasterSymbol::GEOMultiLineRasterSymbol(const std::vector<std::vector<Geodetic2D*>*>* coordinatesArray,
                                                   const GEOLine2DStyle& style) :
GEORasterSymbol( calculateSectorFromCoordinatesArray(coordinatesArray) ),
_coordinatesArray( copyCoordinatesArray(coordinatesArray) ),
_lineColor( style.getColor() ),
_lineWidth( style.getWidth() )
{
}

GEOMultiLineRasterSymbol::~GEOMultiLineRasterSymbol() {
  if (_coordinatesArray != NULL) {
    const int coordinatesArrayCount = _coordinatesArray->size();
    for (int i = 0; i < coordinatesArrayCount; i++) {
      std::vector<Geodetic2D*>* coordinates = _coordinatesArray->at(i);
      const int coordinatesCount = coordinates->size();
      for (int j = 0; j < coordinatesCount; j++) {
        const Geodetic2D* coordinate = coordinates->at(j);
        delete coordinate;
      }
      delete coordinates;
    }
    delete _coordinatesArray;
  }
}

void GEOMultiLineRasterSymbol::rasterize(ICanvas*                   canvas,
                                         const GEORasterProjection* projection) const {
  canvas->setStrokeColor(_lineColor);
  canvas->setStrokeWidth(_lineWidth);

  const int coordinatesArrayCount = _coordinatesArray->size();
  for (int i = 0; i < coordinatesArrayCount; i++) {
    std::vector<Geodetic2D*>* coordinates = _coordinatesArray->at(i);
    rasterLine(coordinates,
               canvas,
               projection);
  }
}
