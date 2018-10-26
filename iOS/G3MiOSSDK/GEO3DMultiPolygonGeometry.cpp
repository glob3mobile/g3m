//
//  GEO3DMultiPolygonGeometry.cpp
//  G3MiOSSDK
//
//  Created by Nico on 24/10/2018.
//


#include "GEO3DMultiPolygonGeometry.hpp"

#include "GEOSymbolizer.hpp"
#include "GEO3DPolygonData.hpp"
#include "GEORasterSymbolizer.hpp"
#include "Sector.hpp"


GEO3DMultiPolygonGeometry::~GEO3DMultiPolygonGeometry() {
  if (_polygonsData != NULL) {
    const size_t polygonsDataSize = _polygonsData->size();
    for (size_t i = 0; i < polygonsDataSize; i++) {
      GEO3DPolygonData* polygonData = _polygonsData->at(i);
      if (polygonData != NULL) {
        polygonData->_release();
      }
    }
    delete _polygonsData;
  }
  
#ifdef JAVA_CODE
  super.dispose();
#endif
}

std::vector<GEOSymbol*>* GEO3DMultiPolygonGeometry::createSymbols(const GEOSymbolizer* symbolizer) const {
  return symbolizer->createSymbols(this);
}

std::vector<GEORasterSymbol*>* GEO3DMultiPolygonGeometry::createRasterSymbols(const GEORasterSymbolizer* symbolizer) const {
  return symbolizer->createSymbols(this);
}

std::vector<GEO3DPolygonData*>* GEO3DMultiPolygonGeometry::copy(const std::vector<GEO3DPolygonData*>* polygonsData) {
  if (polygonsData == NULL) {
    return NULL;
  }
  std::vector<GEO3DPolygonData*>* result = new std::vector<GEO3DPolygonData*>();
  const size_t size = polygonsData->size();
  for (size_t i = 0; i < size; i++) {
    GEO3DPolygonData* each = polygonsData->at(i);
    if (each != NULL) {
      each->_retain();
    }
    result->push_back(each);
  }
  return result;
}

GEO3DMultiPolygonGeometry* GEO3DMultiPolygonGeometry::deepCopy() const {
  return new GEO3DMultiPolygonGeometry(copy(_polygonsData));
}

bool GEO3DMultiPolygonGeometry::contain(const Geodetic3D& point) const {
  if (_polygonsData == NULL) {
    return false;
  }
  const size_t polygonsDataSize = _polygonsData->size();
  for (size_t i = 0; i < polygonsDataSize; i++) {
    GEO3DPolygonData* polygonData = _polygonsData->at(i);
    if (polygonData->contains(point)) {
      return true;
    }
  }
  
  return false;
}

const Sector* GEO3DMultiPolygonGeometry::calculateSector() const {
  if (_polygonsData == NULL) {
    return NULL;
  }
  
  const size_t polygonsDataSize = _polygonsData->size();
  if (polygonsDataSize == 0) {
    return NULL;
  }
  
  const Sector* sector0 = _polygonsData->at(0)->getSector();
  
  double minLatRad = sector0->_lower._latitude._radians;
  double maxLatRad = sector0->_upper._latitude._radians;
  
  double minLonRad = sector0->_lower._longitude._radians;
  double maxLonRad = sector0->_upper._longitude._radians;;

  for (size_t i = 1; i < polygonsDataSize; i++) {
    const Sector* sector = _polygonsData->at(i)->getSector();

    const double lowerLatRad = sector->_lower._latitude._radians;
    if (lowerLatRad < minLatRad) {
      minLatRad = lowerLatRad;
    }
    const double upperLatRad = sector->_upper._latitude._radians;
    if (upperLatRad > maxLatRad) {
      maxLatRad = upperLatRad;
    }

    const double lowerLonRad = sector->_lower._longitude._radians;
    if (lowerLonRad < minLonRad) {
      minLonRad = lowerLonRad;
    }
    const double upperLonRad = sector->_upper._longitude._radians;
    if (upperLonRad > maxLonRad) {
      maxLonRad = upperLonRad;
    }
  }

  const double lowerLatRadians = (minLatRad == maxLatRad) ? minLatRad - 0.0001 : minLatRad;
  const double upperLatRadians = (minLatRad == maxLatRad) ? maxLatRad + 0.0001 : maxLatRad;

  const double lowerLonRadians = (minLonRad == maxLonRad) ? minLonRad - 0.0001 : minLonRad;
  const double upperLonRadians = (minLonRad == maxLonRad) ? maxLonRad + 0.0001 : maxLonRad;
  
  return Sector::newFromRadians(lowerLatRadians, lowerLonRadians,
                                upperLatRadians, upperLonRadians);
}

