//
//  GEORectangleRasterSymbol.cpp
//  G3MiOSSDK
//
//  Created by fpulido on 30/06/14.
//
//

#include "GEORectangleRasterSymbol.hpp"
#include "GEO2DPolygonData.hpp"


GEORectangleRasterSymbol::GEORectangleRasterSymbol(const GEO2DPolygonData*        polygonData,
                                                   const Vector2F rectangleSize,
                                                   const GEO2DLineRasterStyle&    lineStyle,
                                                   const GEO2DSurfaceRasterStyle& surfaceStyle,
                                                   const int minTileLevel,
                                                   const int maxTileLevel) :
GEORasterSymbol(minTileLevel, maxTileLevel),
_polygonData(polygonData),
_rectangleSize(rectangleSize),
_lineStyle(lineStyle),
_surfaceStyle(surfaceStyle)
{
    if (_polygonData != NULL) {
        _polygonData->_retain();
    }
}

GEORectangleRasterSymbol::~GEORectangleRasterSymbol() {
    if (_polygonData != NULL) {
        _polygonData->_release();
    }
#ifdef JAVA_CODE
    super.dispose();
#endif
}

void GEORectangleRasterSymbol::rawRasterize(ICanvas*                   canvas,
                                            const GEORasterProjection* projection) const {
    const bool rasterSurface  = _surfaceStyle.apply(canvas);
    const bool rasterBoundary = _lineStyle.apply(canvas);
    
    rasterRectangle(_polygonData,
                    _rectangleSize,
                    rasterSurface,
                    rasterBoundary,
                    canvas,
                    projection);
}

const Sector* GEORectangleRasterSymbol::getSector() const {
    return (_polygonData == NULL) ? NULL : _polygonData->getSector();
}
