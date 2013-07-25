//
//  GEORasterPolygonSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/23/13.
//
//

#include "GEORasterPolygonSymbol.hpp"

#include "GEO2DPolygonData.hpp"


GEORasterPolygonSymbol::GEORasterPolygonSymbol(const GEO2DPolygonData*        polygonData,
                                               const GEO2DLineRasterStyle&    lineStyle,
                                               const GEO2DSurfaceRasterStyle& surfaceStyle) :
GEORasterSymbol( calculateSectorFromCoordinates(polygonData->getCoordinates()) ),
_coordinates( copyCoordinates(polygonData->getCoordinates()) ),
_holesCoordinatesArray( copyCoordinatesArray(polygonData->getHolesCoordinatesArray()) ),
_lineStyle(lineStyle),
_surfaceStyle(surfaceStyle)
{

}


void GEORasterPolygonSymbol::rasterize(ICanvas*                   canvas,
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
