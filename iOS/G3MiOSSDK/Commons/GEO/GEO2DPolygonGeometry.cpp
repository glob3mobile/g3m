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
#include "GEORasterSymbolizer.hpp"

const std::vector<Geodetic2D*>* GEO2DPolygonGeometry::getCoordinates() const {
  return _polygonData->getCoordinates();
}

const std::vector<std::vector<Geodetic2D*>*>* GEO2DPolygonGeometry::getHolesCoordinatesArray() const {
  return _polygonData->getHolesCoordinatesArray();
}

GEO2DPolygonGeometry::~GEO2DPolygonGeometry() {
  if (_polygonData) {
    _polygonData->_release();
  }
#ifdef JAVA_CODE
  super.dispose();
#endif
}

std::vector<GEOSymbol*>* GEO2DPolygonGeometry::createSymbols(const GEOSymbolizer* symbolizer) const {
  return symbolizer->createSymbols(this);
}

std::vector<GEORasterSymbol*>* GEO2DPolygonGeometry::createRasterSymbols(const GEORasterSymbolizer* symbolizer) const {
  return symbolizer->createSymbols(this);
}

long long GEO2DPolygonGeometry::getCoordinatesCount() const {
  return (_polygonData == NULL) ? 0 : _polygonData->getCoordinatesCount();
}

GEO2DPolygonGeometry* GEO2DPolygonGeometry::deepCopy() const {
  if (_polygonData != NULL) {
    _polygonData->_retain();
  }
  return new GEO2DPolygonGeometry(_polygonData);
}

bool GEO2DPolygonGeometry::contain(const Geodetic2D& point) const {
  if (_polygonData) {
    return _polygonData->contains(point);
  }
  return false;
}

