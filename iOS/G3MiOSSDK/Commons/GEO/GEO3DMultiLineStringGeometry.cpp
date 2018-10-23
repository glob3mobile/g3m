//
//  GEO3DMultiLineStringGeometry.cpp
//  G3MiOSSDK
//
//  Created by Nico on 23/10/2018.
//

#include "GEO3DMultiLineStringGeometry.hpp"

#include "Geodetic3D.hpp"
#include "GEOSymbolizer.hpp"
#include "GEORasterSymbolizer.hpp"
#include "GEO3DCoordinatesArrayData.hpp"

GEO3DMultiLineStringGeometry::GEO3DMultiLineStringGeometry(std::vector<std::vector<Geodetic3D*>*>* coordinatesArray)
{
  _coordinatesArrayData = (coordinatesArray == NULL) ? NULL : new GEO3DCoordinatesArrayData(coordinatesArray);
}

GEO3DMultiLineStringGeometry::GEO3DMultiLineStringGeometry(const GEO3DCoordinatesArrayData* coordinatesArrayData) :
_coordinatesArrayData(coordinatesArrayData)
{
  if (_coordinatesArrayData != NULL) {
    _coordinatesArrayData->_retain();
  }
}

GEO3DMultiLineStringGeometry::~GEO3DMultiLineStringGeometry() {
  if (_coordinatesArrayData != NULL) {
    _coordinatesArrayData->_release();
  }
  
#ifdef JAVA_CODE
  super.dispose();
#endif
}

std::vector<GEOSymbol*>* GEO3DMultiLineStringGeometry::createSymbols(const GEOSymbolizer* symbolizer) const {
  return symbolizer->createSymbols(this);
}

std::vector<GEORasterSymbol*>* GEO3DMultiLineStringGeometry::createRasterSymbols(const GEORasterSymbolizer* symbolizer) const {
  return symbolizer->createSymbols(this);
}

long long GEO3DMultiLineStringGeometry::getCoordinatesCount() const {
  return (_coordinatesArrayData == NULL) ? 0 : _coordinatesArrayData->getCoordinatesCount();
}

GEO3DMultiLineStringGeometry* GEO3DMultiLineStringGeometry::deepCopy() const {
  return new GEO3DMultiLineStringGeometry(_coordinatesArrayData);
}
