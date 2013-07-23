//
//  GEORasterPolygonSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/23/13.
//
//

#include "GEORasterPolygonSymbol.hpp"

GEORasterPolygonSymbol::GEORasterPolygonSymbol(const std::vector<Geodetic2D*>*               coordinates,
                                               const std::vector<std::vector<Geodetic2D*>*>* holesCoordinatesArray,
                                               const GEO2DLineRasterStyle&                   lineStyle,
                                               const GEO2DSurfaceRasterStyle&                surfaceStyle) :
GEORasterSymbol( calculateSectorFromCoordinates(coordinates) ),
_coordinates( copyCoordinates(coordinates) ),
_holesCoordinatesArray( copyCoordinatesArray(holesCoordinatesArray) ),
_lineStyle(lineStyle),
_surfaceStyle(surfaceStyle)
{

}

void GEORasterPolygonSymbol::rasterize(ICanvas*                   canvas,
                                       const GEORasterProjection* projection) const {
  const bool rasterizeSurface  = _surfaceStyle.apply(canvas);
  const bool rasterizeBoundary = _lineStyle.apply(canvas);

  rasterPolygonSurface(_coordinates,
                       _holesCoordinatesArray,
                       canvas,
                       projection);

//  if (rasterizeSurface) {
//    if (rasterizeBoundary) {
//      rasterPolygonSurfaceAndBoundary(_coordinates,
//                                      _holesCoordinatesArray,
//                                      canvas,
//                                      projection);
//    }
//    else {
//      rasterPolygonSurface(_coordinates,
//                           _holesCoordinatesArray,
//                           canvas,
//                           projection);
//    }
//  }
//  else if (rasterizeBoundary) {
//    rasterPolygonBoundary(_coordinates,
//                          _holesCoordinatesArray,
//                          canvas,
//                          projection);
//  }
}
