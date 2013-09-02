//
//  GEO2DPolygonGeometry.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/23/13.
//
//

#include "GEO2DPolygonGeometry.hpp"

#include "GEOSymbolizer.hpp"
#include "GEO2DPolygonData.hpp"

const std::vector<Geodetic2D*>* GEO2DPolygonGeometry::getCoordinates() const {
  return _polygonData->getCoordinates();
}

const std::vector<std::vector<Geodetic2D*>*>* GEO2DPolygonGeometry::getHolesCoordinatesArray() const {
  return _polygonData->getHolesCoordinatesArray();
}

GEO2DPolygonGeometry::~GEO2DPolygonGeometry() {
  delete _polygonData;
  
//  const int coordinatesCount = _coordinates->size();
//  for (int i = 0; i < coordinatesCount; i++) {
//    Geodetic2D* coordinate = _coordinates->at(i);
//    delete coordinate;
//  }
//  delete _coordinates;
//
//
//  if (_holesCoordinatesArray != NULL) {
//    const int holesCoordinatesArraySize = _holesCoordinatesArray->size();
//    for (int j = 0; j < holesCoordinatesArraySize; j++) {
//      const std::vector<Geodetic2D*>* holeCoordinates = _holesCoordinatesArray->at(j);
//
//      const int holeCoordinatesCount = holeCoordinates->size();
//      for (int i =0; i < holeCoordinatesCount; i++) {
//        const Geodetic2D* holeCoordinate = holeCoordinates->at(i);
//
//        delete holeCoordinate;
//      }
//
//      delete holeCoordinates;
//    }
//    delete _holesCoordinatesArray;
//  }

#ifdef JAVA_CODE
  super.dispose();
#endif

}


std::vector<GEOSymbol*>* GEO2DPolygonGeometry::createSymbols(const GEOSymbolizer* symbolizer) const {
  return symbolizer->createSymbols(this);
}
