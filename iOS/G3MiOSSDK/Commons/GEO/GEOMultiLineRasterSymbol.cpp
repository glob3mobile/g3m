//
//  GEOMultiLineRasterSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/10/13.
//
//

#include "GEOMultiLineRasterSymbol.hpp"

#include "ICanvas.hpp"

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

GEOMultiLineRasterSymbol* GEOMultiLineRasterSymbol::createSymbol() const {
  GEOMultiLineRasterSymbol* result = new GEOMultiLineRasterSymbol(_coordinatesArray,
                                                                  new Sector(*_sector),
                                                                  _lineColor,
                                                                  _lineWidth);
  _coordinatesArray = NULL;
  return result;
}


void GEOMultiLineRasterSymbol::rasterize(ICanvas*                   canvas,
                                         const GEORasterProjection* projection) const {
//  int __REMOVE_DEBUG_CODE;
//  canvas->setStrokeColor(Color::green());
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
