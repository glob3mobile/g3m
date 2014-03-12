//
//  GEOPolygonRasterSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/23/13.
//
//

#include "GEOPolygonRasterSymbol.hpp"

#include "GEO2DPolygonData.hpp"


GEOPolygonRasterSymbol::GEOPolygonRasterSymbol(const GEO2DPolygonData*        polygonData,
                                               const GEO2DLineRasterStyle&    lineStyle,
                                               const GEO2DSurfaceRasterStyle& surfaceStyle,
                                               const int minTileLevel,
                                               const int maxTileLevel) :
GEORasterSymbol( calculateSectorFromCoordinates(polygonData->getCoordinates()), minTileLevel, maxTileLevel ),
_coordinates( copyCoordinates(polygonData->getCoordinates()) ),
_holesCoordinatesArray( copyCoordinatesArray(polygonData->getHolesCoordinatesArray()) ),
_lineStyle(lineStyle),
_surfaceStyle(surfaceStyle)
{

}

GEOPolygonRasterSymbol::~GEOPolygonRasterSymbol() {
#ifdef C_CODE
  if (_coordinates != NULL) {
    const int coordinatesSize = _coordinates->size();
    for (int i = 0; i < coordinatesSize; i++) {
      Geodetic2D* coordinate = _coordinates->at(i);
      delete coordinate;
    }
    delete _coordinates;
  }

  if (_holesCoordinatesArray != NULL) {
    const int holesCoordinatesArraySize = _holesCoordinatesArray->size();
    for (int j = 0; j < holesCoordinatesArraySize; j++) {
      const std::vector<Geodetic2D*>* holeCoordinates = _holesCoordinatesArray->at(j);

      const int holeCoordinatesCount = holeCoordinates->size();
      for (int i = 0; i < holeCoordinatesCount; i++) {
        const Geodetic2D* holeCoordinate = holeCoordinates->at(i);

        delete holeCoordinate;
      }

      delete holeCoordinates;
    }
    delete _holesCoordinatesArray;
  }
#endif

#ifdef JAVA_CODE
  super.dispose();
#endif

}


void GEOPolygonRasterSymbol::rawRasterize(ICanvas*                   canvas,
                                          const GEORasterProjection* projection) const {
  const bool rasterSurface  = _surfaceStyle.apply(canvas);
  const bool rasterBoundary = _lineStyle.apply(canvas);

  rasterPolygon(_coordinates,
                _holesCoordinatesArray,
                rasterSurface,
                rasterBoundary,
                canvas,
                projection);
}
