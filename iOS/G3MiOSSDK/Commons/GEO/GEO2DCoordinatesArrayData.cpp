//
//  GEO2DCoordinatesArrayData.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 5/11/14.
//
//

#include "GEO2DCoordinatesArrayData.hpp"

#include "Geodetic2D.hpp"
#include "GEO2DCoordinatesData.hpp"
#include "Sector.hpp"

GEO2DCoordinatesArrayData::GEO2DCoordinatesArrayData(std::vector<std::vector<Geodetic2D*>*>* coordinatesArray) :
_sector(NULL)
{
  if (coordinatesArray == NULL) {
    _coordinatesArray = NULL;
  }
  else {
    _coordinatesArray = new std::vector<const GEO2DCoordinatesData*>;
    const size_t size = coordinatesArray->size();
    for (size_t i = 0; i < size; i++) {
      std::vector<Geodetic2D*>* coordinates = coordinatesArray->at(i);
      if (coordinates != NULL) {
        _coordinatesArray->push_back( new GEO2DCoordinatesData(coordinates) );
      }
    }
  }
}


GEO2DCoordinatesArrayData::~GEO2DCoordinatesArrayData() {
  if (_coordinatesArray != NULL) {
    const size_t coordinatesArrayCount = _coordinatesArray->size();
    for (size_t i = 0; i < coordinatesArrayCount; i++) {
      const GEO2DCoordinatesData* coordinates = _coordinatesArray->at(i);
      //if (coordinates != NULL) {
        coordinates->_release();
      //}
    }
#ifdef C_CODE
    delete _coordinatesArray;
#endif
  }

  delete _sector;

#ifdef JAVA_CODE
  super.dispose();
#endif
}

Sector* GEO2DCoordinatesArrayData::calculateSector() const {
  const IMathUtils* mu = IMathUtils::instance();

  const double maxDouble = mu->maxDouble();
  const double minDouble = mu->minDouble();

  double minLatInRadians = maxDouble;
  double maxLatInRadians = minDouble;

  double minLonInRadians = maxDouble;
  double maxLonInRadians = minDouble;

  const size_t coordinatesArrayCount = _coordinatesArray->size();
  for (size_t i = 0; i < coordinatesArrayCount; i++) {
    const GEO2DCoordinatesData* coordinates = _coordinatesArray->at(i);
    const size_t coordinatesCount = coordinates->size();
    for (size_t j = 0; j < coordinatesCount; j++) {
      const Geodetic2D* coordinate = coordinates->get(j);

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
  }

  if ((minLatInRadians == maxDouble) ||
      (maxLatInRadians == minDouble) ||
      (minLonInRadians == maxDouble) ||
      (maxLonInRadians == minDouble)) {
    return NULL;
  }

  Sector* result = new Sector(Geodetic2D::fromRadians(minLatInRadians - 0.0001, minLonInRadians - 0.0001),
                              Geodetic2D::fromRadians(maxLatInRadians + 0.0001, maxLonInRadians + 0.0001));

  //  int __REMOVE_DEBUG_CODE;
  //  for (int i = 0; i < coordinatesArrayCount; i++) {
  //    std::vector<Geodetic2D*>* coordinates = coordinatesArray->at(i);
  //    const int coordinatesCount = coordinates->size();
  //    for (int j = 0; j < coordinatesCount; j++) {
  //      const Geodetic2D* coordinate = coordinates->at(j);
  //      if (!result->contains(*coordinate)) {
  //        printf("xxx\n");
  //      }
  //    }
  //  }
  
  return result;
}

const Sector* GEO2DCoordinatesArrayData::getSector() const {
  if (_sector == NULL) {
    _sector = calculateSector();
  }
  return _sector;
}

long long GEO2DCoordinatesArrayData::getCoordinatesCount() const {
  long long result = 0;
  if (_coordinatesArray != NULL) {
    const size_t coordinatesArrayCount = _coordinatesArray->size();
    for (size_t i = 0; i < coordinatesArrayCount; i++) {
      const GEO2DCoordinatesData* coordinates = _coordinatesArray->at(i);
      result += coordinates->getCoordinatesCount();
    }
  }
  return result;
}
