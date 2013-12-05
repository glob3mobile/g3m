//
//  GEOMultiLineRasterSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/10/13.
//
//

#include "GEOMultiLineRasterSymbol.hpp"

#include "GEO2DLineRasterStyle.hpp"
#include "ICanvas.hpp"

GEOMultiLineRasterSymbol::GEOMultiLineRasterSymbol(const std::vector<std::vector<Geodetic2D*>*>* coordinatesArray,
                                                   const GEO2DLineRasterStyle& style,
                                                   const int minTileLevel,
                                                   const int maxTileLevel) :
GEORasterSymbol( calculateSectorFromCoordinatesArray(coordinatesArray), minTileLevel, maxTileLevel ),
_coordinatesArray( copyCoordinatesArray(coordinatesArray) ),
_style(style)
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

#ifdef JAVA_CODE
  super.dispose();
#endif

}

void GEOMultiLineRasterSymbol::rawRasterize(ICanvas*                   canvas,
                                            const GEORasterProjection* projection) const {
  if (_style.apply(canvas)) {
    const int coordinatesArrayCount = _coordinatesArray->size();
    for (int i = 0; i < coordinatesArrayCount; i++) {
      std::vector<Geodetic2D*>* coordinates = _coordinatesArray->at(i);
      rasterLine(coordinates,
                 canvas,
                 projection);
    }
  }
}
