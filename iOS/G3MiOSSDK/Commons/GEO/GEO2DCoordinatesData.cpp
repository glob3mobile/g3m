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

Sector* GEO2DCoordinatesData::calculateSector() const {
  const size_t size = _coordinates->size();
  if (size == 0) {
    return NULL;
  }

  const Geodetic2D* coordinate0 = _coordinates->at(0);

  double minLatInRadians = coordinate0->_latitude._radians;
  double maxLatInRadians = minLatInRadians;

  double minLonInRadians = coordinate0->_longitude._radians;
  double maxLonInRadians = minLonInRadians;

  for (size_t i = 1; i < size; i++) {
    const Geodetic2D* coordinate = _coordinates->at(i);

    const double latInRadians = coordinate->_latitude._radians;
    if (latInRadians < minLatInRadians) {
      minLatInRadians = latInRadians;
    }
    if (latInRadians > maxLatInRadians) {
      maxLatInRadians = latInRadians;
    }

    const double lonInRadians = coordinate->_longitude._radians;
    if (lonInRadians < minLonInRadians) {
      minLonInRadians = lonInRadians;
    }
    if (lonInRadians > maxLonInRadians) {
      maxLonInRadians = lonInRadians;
    }
  }

  Sector* result = new Sector(Geodetic2D::fromRadians(minLatInRadians - 0.0001, minLonInRadians - 0.0001),
                              Geodetic2D::fromRadians(maxLatInRadians + 0.0001, maxLonInRadians + 0.0001));

  //  int __REMOVE_DEBUG_CODE;
  //  for (int i = 0; i < size; i++) {
  //    const Geodetic2D* coordinate = coordinates->at(i);
  //    if (!result->contains(*coordinate)) {
  //      printf("xxx\n");
  //    }
  //  }
  
  return result;
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

