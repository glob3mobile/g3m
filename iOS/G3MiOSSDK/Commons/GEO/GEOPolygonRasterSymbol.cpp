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
GEORasterSymbol(minTileLevel, maxTileLevel ),
_polygonData(polygonData),
_lineStyle(lineStyle),
_surfaceStyle(surfaceStyle)
{
  if (_polygonData != NULL) {
    _polygonData->_retain();
  }
}

GEOPolygonRasterSymbol::~GEOPolygonRasterSymbol() {
  if (_polygonData != NULL) {
    _polygonData->_release();
  }
#ifdef JAVA_CODE
  super.dispose();
#endif
}

void GEOPolygonRasterSymbol::rawRasterize(ICanvas*                   canvas,
                                          const GEORasterProjection* projection) const {
  const bool rasterSurface  = _surfaceStyle.apply(canvas);
  const bool rasterBoundary = _lineStyle.apply(canvas);

  rasterPolygon(_polygonData,
                rasterSurface,
                rasterBoundary,
                canvas,
                projection);
}

const Sector* GEOPolygonRasterSymbol::getSector() const {
  return (_polygonData == NULL) ? NULL : _polygonData->getSector();
}
