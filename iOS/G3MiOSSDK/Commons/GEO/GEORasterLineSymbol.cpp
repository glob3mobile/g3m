//
//  GEORasterLineSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/10/13.
//
//

#include "GEORasterLineSymbol.hpp"

#include "Tile.hpp"
#include "ICanvas.hpp"

GEORasterLineSymbol::~GEORasterLineSymbol() {
  if (_coordinates != NULL) {
    const int size = _coordinates->size();

    for (int i = 0; i < size; i++) {
      const Geodetic2D* coordinate = _coordinates->at(i);
      delete coordinate;
    }

    delete _coordinates;
  }
}

GEORasterLineSymbol* GEORasterLineSymbol::createSymbol() const {
  GEORasterLineSymbol* result = new GEORasterLineSymbol(_coordinates,
                                                        new Sector(*_sector),
                                                        _lineColor,
                                                        _lineWidth);
  _coordinates = NULL;
  return result;
}

void GEORasterLineSymbol::rasterize(ICanvas*                   canvas,
                                    const GEORasterProjection* projection) const {

  canvas->setStrokeColor(_lineColor);
  canvas->setStrokeWidth(_lineWidth);

  rasterLine(_coordinates,
             canvas,
             projection);
}
