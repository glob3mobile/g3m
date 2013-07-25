//
//  GEORasterMultiPolygonSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/25/13.
//
//

#include "GEORasterMultiPolygonSymbol.hpp"

#include "GEO2DPolygonData.hpp"

//
//GEORasterMultiPolygonSymbol::GEORasterMultiPolygonSymbol(const std::vector<GEO2DPolygonData*>* polygonsData,
//                                                         const GEO2DLineRasterStyle&           lineStyle,
//                                                         const GEO2DSurfaceRasterStyle&        surfaceStyle) :
//GEORasterSymbol( calculateSectorFromPolygonsData(polygonsData) ),
//_polygonsData( copyPolygonsData(_polygonsData) ),
//_lineStyle(lineStyle),
//_surfaceStyle(surfaceStyle)
//{
//
//}
//
//GEORasterMultiPolygonSymbol::~GEORasterMultiPolygonSymbol() {
//  const int polygonsDataSize = _polygonsData->size();
//
//  for (int i = 0; i < polygonsDataSize; i++) {
//    GEO2DPolygonData* polygonData = _polygonsData->at(i);
//    delete polygonData;
//  }
//
//  delete _polygonsData;
//}
//
//void GEORasterMultiPolygonSymbol::rasterize(ICanvas*                   canvas,
//                                            const GEORasterProjection* projection) const {
//  aa;
//}
