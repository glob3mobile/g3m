//
//  GEO2DCoordinatesData.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 5/11/14.
//
//

#include "GEO2DCoordinatesData.hpp"

#include "Geodetic2D.hpp"
#include "Sector.hpp"

GEO2DCoordinatesData::~GEO2DCoordinatesData() {
#ifdef C_CODE
  const size_t coordinatesCount = _coordinates->size();
  for (size_t i = 0; i < coordinatesCount; i++) {
    Geodetic2D* coordinate = _coordinates->at(i);
    delete coordinate;
  }
  delete _coordinates;
#endif
  delete _sector;
#ifdef JAVA_CODE
  super.dispose();
#endif
}

const Sector* GEO2DCoordinatesData::calculateSector() const {
  const size_t size = _coordinates->size();
  if (size == 0) {
    return NULL;
  }

  const Geodetic2D* coordinate0 = _coordinates->at(0);

  double minLatRad = coordinate0->_latitude._radians;
  double maxLatRad = minLatRad;

  double minLonRad = coordinate0->_longitude._radians;
  double maxLonRad = minLonRad;

  for (size_t i = 1; i < size; i++) {
    const Geodetic2D* coordinate = _coordinates->at(i);

    const double latRad = coordinate->_latitude._radians;
    if (latRad < minLatRad) {
      minLatRad = latRad;
    }
    if (latRad > maxLatRad) {
      maxLatRad = latRad;
    }

    const double lonRad = coordinate->_longitude._radians;
    if (lonRad < minLonRad) {
      minLonRad = lonRad;
    }
    if (lonRad > maxLonRad) {
      maxLonRad = lonRad;
    }
  }

  const double lowerLatRadians = (minLatRad == maxLatRad) ? minLatRad - 0.0001 : minLatRad;
  const double upperLatRadians = (minLatRad == maxLatRad) ? maxLatRad + 0.0001 : maxLatRad;

  const double lowerLonRadians = (minLonRad == maxLonRad) ? minLonRad - 0.0001 : minLonRad;
  const double upperLonRadians = (minLonRad == maxLonRad) ? maxLonRad + 0.0001 : maxLonRad;

  return Sector::newFromRadians(lowerLatRadians, lowerLonRadians,
                                upperLatRadians, upperLonRadians);
}

const Sector* GEO2DCoordinatesData::getSector() const {
  if (_sector == NULL) {
    _sector = calculateSector();
  }
  return _sector;
}

long long GEO2DCoordinatesData::getCoordinatesCount() const {
  return (_coordinates == NULL) ? 0 : _coordinates->size();
}
