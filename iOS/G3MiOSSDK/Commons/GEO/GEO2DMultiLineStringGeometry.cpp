//
//  GEO2DMultiLineStringGeometry.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/30/12.
//
//

#include "GEO2DMultiLineStringGeometry.hpp"

#include "Geodetic2D.hpp"
#include "GEOSymbolizer.hpp"
#include "GEORasterSymbolizer.hpp"
#include "GEO2DCoordinatesArrayData.hpp"

GEO2DMultiLineStringGeometry::GEO2DMultiLineStringGeometry(std::vector<std::vector<Geodetic2D*>*>* coordinatesArray)
{
  _coordinatesArrayData = (coordinatesArray == NULL) ? NULL : new GEO2DCoordinatesArrayData(coordinatesArray);
}

GEO2DMultiLineStringGeometry::GEO2DMultiLineStringGeometry(const GEO2DCoordinatesArrayData* coordinatesArrayData) :
_coordinatesArrayData(coordinatesArrayData)
{
  if (_coordinatesArrayData != NULL) {
    _coordinatesArrayData->_retain();
  }
}

GEO2DMultiLineStringGeometry::~GEO2DMultiLineStringGeometry() {
  if (_coordinatesArrayData != NULL) {
    _coordinatesArrayData->_release();
  }

#ifdef JAVA_CODE
  super.dispose();
#endif
}

std::vector<GEOSymbol*>* GEO2DMultiLineStringGeometry::createSymbols(const GEOSymbolizer* symbolizer) const {
  return symbolizer->createSymbols(this);
}

std::vector<GEORasterSymbol*>* GEO2DMultiLineStringGeometry::createRasterSymbols(const GEORasterSymbolizer* symbolizer) const {
  return symbolizer->createSymbols(this);
}

long long GEO2DMultiLineStringGeometry::getCoordinatesCount() const {
  return (_coordinatesArrayData == NULL) ? 0 : _coordinatesArrayData->getCoordinatesCount();
}

GEO2DMultiLineStringGeometry* GEO2DMultiLineStringGeometry::deepCopy() const {
  return new GEO2DMultiLineStringGeometry(_coordinatesArrayData);
}
